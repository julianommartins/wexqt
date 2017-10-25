package com.ibm.services.tools.wexws.dao;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.WexWsConstants;
import com.ibm.services.tools.wexws.bo.DataCollector;
import com.ibm.services.tools.wexws.collections.CollectionShard;
import com.ibm.services.tools.wexws.collections.Server;
import com.ibm.services.tools.wexws.domain.AddedSource;
import com.ibm.services.tools.wexws.domain.KeywordCombination;
import com.ibm.services.tools.wexws.domain.KeywordCombinations;
import com.ibm.services.tools.wexws.domain.KeywordFilter;
import com.ibm.services.tools.wexws.domain.KeywordFilterLogic;
import com.ibm.services.tools.wexws.domain.KeywordCombinationResult;
import com.ibm.services.tools.wexws.domain.QueryModifierResult;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.domain.WexQueryResults;
import com.ibm.services.tools.wexws.factory.WexUrlFactory;
import com.ibm.services.tools.wexws.helper.JapaneseQueryExtractor;
import com.ibm.services.tools.wexws.helper.WexRequestBuilders;
import com.ibm.services.tools.wexws.loadbalance.CollectionServerRoundRobin;
import com.ibm.services.tools.wexws.utils.HttpRequest;
import com.ibm.services.tools.wexws.utils.HttpRequestCallable;
import com.ibm.services.tools.wexws.utils.ThreadExecutorMaganer;
import com.ibm.services.tools.wexws.wql.WQL;

public class WexRestfulDAO implements AccessLayerDAO {
	
	private static Logger logger = Logger.getLogger(WexRestfulDAO.class);
	
	private WexUrlFactory urlFactory;
	private CollectionServerRoundRobin serverRoundRobin;
	private CollectionServerRoundRobin queryParserServerRoundRobin;
	private JAXBContext wexResultJaxbContext;
	private boolean debug = true;

	public WexRestfulDAO(WexUrlFactory wexURLFactory) {
		this.urlFactory = wexURLFactory;
		this.serverRoundRobin = new CollectionServerRoundRobin(wexURLFactory.getConfig());
		this.queryParserServerRoundRobin = new CollectionServerRoundRobin(wexURLFactory.getConfig());
		try {
			this.wexResultJaxbContext = JAXBContext.newInstance(WexQueryResults.class);
		} catch (JAXBException e) {
			String message = "Error while creating JAXBContext for WexQueryResults.class";
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	@Override
	public Response executeQuery(WQL wql, boolean profile, boolean nlq, boolean smartCondition, boolean ontolection, 
			boolean japaneseSearch, String smartConditionOperator, boolean bold, boolean stemming, boolean spelling) throws Exception {
		
		List<CollectionShard> shards = urlFactory.getConfig().getCollectionByName(wql.getCollection()).getShards();
		Map<String,Set<String>> collectionServerMap = getLoadBalancedCollectionServerMap(shards);

		String queryObjectSearchTerms = null;
		
		//if japanese search, run query modifier to extract keywords and use them instead
		if (wql != null && wql.getTextSearch() != null && wql.getTextSearch().length() > 0){
			if (new JapaneseQueryExtractor().isJapaneseQuery(wql.getTextSearch())){
				nlq = false;
				//ontolection = false; // temporary disabling ontolection when JP
				japaneseSearch = true;
				queryObjectSearchTerms = executeQueryModifier(wql.getTextSearch());
			} 
		}
		
		Object[] queriesResponse = this.urlFactory.getRestfulURL(wql, queryObjectSearchTerms, debug, profile, nlq, smartCondition, ontolection, smartConditionOperator, collectionServerMap, bold, stemming, spelling,japaneseSearch);
		@SuppressWarnings("unchecked")
		List<String> restfulUrls = (List<String>) queriesResponse[0];
		Response response = runQueriesAndCollect(restfulUrls, shards, wql.getCollection());
		response.setRequestedFields(wql.getRequestedFields());
		response.setSmartConditions((String) queriesResponse[1]);
		response.sortBy();
		return response;
	}

	@Override
	public Response executeQuery(Request searchRequest) throws Exception {
		String jpQuery = "";
		
		//if japanese search, run query modifier to extract keywords and use them instead
		if (searchRequest.getQuery() != null ){
			if (searchRequest.getQuery().length() > 0 && new JapaneseQueryExtractor().isJapaneseQuery(searchRequest.getQuery())) {
				jpQuery = executeQueryModifier(searchRequest.getQuery());
			}
		}
		
		if (jpQuery.length() > 0){
			searchRequest.setQuery(jpQuery);
		} 
		
		if (searchRequest.getKeywordFilters() != null && searchRequest.getKeywordFilters().size()>0) {
			for (KeywordFilter kf : searchRequest.getKeywordFilters()) {
				kf.applyDoubleQuotes();				
			}
		}
		
		Response response = executeQueryUntilSuccess(searchRequest);
		response.setRequestedFields(searchRequest.getFields());
		
		List<KeywordFilter> listKF = searchRequest.getKeywordFilters();
		if (null != listKF){
			for (KeywordFilter keywordFilter : listKF) {
				if (keywordFilter.getFilterLogic() ==  KeywordFilterLogic.MUST_HAVE){
					response.addKeywordFiltersMustHave(keywordFilter);
				} else {
					response.addKeywordFiltersNiceToHave(keywordFilter);
				}
			}
		}
		response.sortBy();
		return response;
	}
	

	private Response executeQueryUntilSuccess(Request searchRequest) throws Exception {
		long startedTime = System.currentTimeMillis();
		
		List<CollectionShard> shardList = this.urlFactory.getConfig().getCollectionByName(searchRequest.getCollection()).getShards();
		Map<String,Set<String>> loadBalancedShardServerMap = getLoadBalancedCollectionServerMap(shardList);
		List<String> restfulUrls = this.urlFactory.getQueries(loadBalancedShardServerMap, searchRequest);
		
		List<WexQueryResults> wexResults = executeQueries(restfulUrls);
		
		Map<String, List<String>> failedShardsByServerMap = validateAndGetFailedShardsByServer(shardList, wexResults, loadBalancedShardServerMap);
		if (failedShardsByServerMap != null && failedShardsByServerMap.size() > 0){
			Map<String,Set<String>> alternativeCollectionServerMap = getAlternativeCollectionServerMap(failedShardsByServerMap);
			List<String> alternativeRestfulUrls = this.urlFactory.getQueries(alternativeCollectionServerMap, searchRequest);
			wexResults.addAll(executeQueries(alternativeRestfulUrls));
			restfulUrls.addAll(alternativeRestfulUrls);
		}
		long elapsedTime = System.currentTimeMillis() - startedTime;
		
		Response response = new Response(elapsedTime, restfulUrls);
		populateErrorMessages(shardList, wexResults, response);
		collectWexResultsData(wexResults, searchRequest, response);
		return response;
	}

	private void collectWexResultsData(List<WexQueryResults> wexResults, Request searchRequest, Response response) {
		DataCollector collector = new DataCollector(response, searchRequest, urlFactory.getConfig());
		for (WexQueryResults wexResult: wexResults){
			collector.collect(wexResult);
		}
	}

	private void populateErrorMessages(List<CollectionShard> shardList, List<WexQueryResults> wexResults, Response response) {
		List<String> failedShards = determineShardsHaveResults(shardList, wexResults);
		if (failedShards != null && failedShards.size() > 0){
			Set<String> errorMessages = new HashSet<String>();
			for (WexQueryResults wexResult: wexResults){
				if (wexResult.getExecutionException() != null){
					errorMessages.add(wexResult.getExecutionException().getMessage());
				}
				if (wexResult.getAddedSourceList() != null){
					for (AddedSource source: wexResult.getAddedSourceList()){
						if (source.getErrorMessagesFromSource() != null){
							errorMessages.addAll(source.getErrorMessagesFromSource());
						}
					}
				}
			}
			if (errorMessages.size() > 0){
				response.setHasError(true);
				response.setExceptionMessages(errorMessages);
			}
		}
	}
	
	private Map<String, List<String>> validateAndGetFailedShardsByServer(List<CollectionShard> shardList, 
			List<WexQueryResults> wexResults, Map<String,Set<String>> loadBalancedShardServerMap){
		
		Map<String, List<String>> failedShardsByServerMap = null;
		List<String> failedShards = determineShardsHaveResults(shardList, wexResults);
				
		if (failedShards != null && failedShards.size() > 0){
			failedShardsByServerMap = new HashMap<String, List<String>>();
			for (Map.Entry<String,Set<String>> entry: loadBalancedShardServerMap.entrySet()){
				String server = entry.getKey();
				for (String shard: entry.getValue()){
					if (failedShards.contains(shard)){
						List<String> failedShardsByServer = failedShardsByServerMap.get(server);
						if (failedShardsByServer == null){
							failedShardsByServer = new ArrayList<String>(4);
							failedShardsByServerMap.put(server, failedShardsByServer);
						}
						failedShardsByServer.add(shard);
					}
				}
			}
		}
		return failedShardsByServerMap;
	}

	private List<String> determineShardsHaveResults(List<CollectionShard> shardList, List<WexQueryResults> wexResults) {
		List<String> failedShards = null;
		for (CollectionShard shard: shardList){
			boolean shardQueriedSuccessfully = false;
			
			wexResultsLoop:
			for (WexQueryResults wexResult: wexResults){
				if (wexResult.getAddedSourceList() != null){
					for (AddedSource addedSource: wexResult.getAddedSourceList()){
						if (shard.getShardName().equals(addedSource.getName()) && !addedSource.hasErrors()){
							shardQueriedSuccessfully = true;
							break wexResultsLoop;
						}
					}
				}
			}
			
			if (!shardQueriedSuccessfully){
				if (failedShards == null){
					failedShards = new ArrayList<String>(4);
				}
				failedShards.add(shard.getShardName());
			}
		}
		return failedShards;
	}
	
	private Response runQueriesAndCollect(List<String> restfulUrls, List<CollectionShard> shardList, String collectionName) throws JAXBException {
		long startedTime = System.currentTimeMillis();
		List<WexQueryResults> wexResults = executeQueries(restfulUrls);
		long elapsedTime = System.currentTimeMillis() - startedTime;
				
		Response response = new Response(elapsedTime, restfulUrls);
		populateErrorMessages(shardList, wexResults, response);
		collectWexResultsData(wexResults, WexRequestBuilders.searchRequest(collectionName), response);
		return response;
	}

	private List<WexQueryResults> executeQueries(List<String> restfulUrls) throws JAXBException{
		List<WexQueryResults> results = new ArrayList<WexQueryResults>(restfulUrls.size());
		Unmarshaller unmarshaller = wexResultJaxbContext.createUnmarshaller();
		
		if (restfulUrls.size() > 1) {
			ThreadExecutorMaganer tem = new ThreadExecutorMaganer(WexWsConstants.MAX_THREADS, WexWsConstants.TIMEOUT_IN_SECONDS);
			for (String url : restfulUrls) {
				HttpRequest httpRequest = new HttpRequest(url, urlFactory.getConfig(), debug);
				tem.add(new HttpRequestCallable(httpRequest));
			}
			List<Future<String>> futureResponses = tem.start();
			for (Future<String> futureResponse : futureResponses) {
				try {
					String resp = futureResponse.get();
					results.add(buildWexQueryResult(unmarshaller, resp));
				}catch (ExecutionException ex){
					logger.error("ExecutionException while getting WEX response="+ex.getCause().getMessage());
					results.add(new WexQueryResults(ex));
				}catch (Exception e) {
					logger.error("Fail to query WEX server", e);
					results.add(new WexQueryResults(e));
				}
			}
		}else {
			try {
				HttpRequest httpRequest = new HttpRequest(restfulUrls.get(0), urlFactory.getConfig(), debug);
				String resp = httpRequest.doGet();
				results.add(buildWexQueryResult(unmarshaller, resp));
			} catch (Exception e) {
				logger.error("Fail to query WEX server", e);
				results.add(new WexQueryResults(e));
			}
		}
		
		return results;
	}

	private WexQueryResults buildWexQueryResult(Unmarshaller unmarshaller, String resp)
			throws JAXBException, Exception {
		if (resp != null) {
			// here we get the result (the XML) and unmarshal
			WexQueryResults wexQueryResults = (WexQueryResults) unmarshaller.unmarshal(new StringReader(resp));
			return wexQueryResults;
		} else {
			logger.error("WEX Response is null");
			throw new Exception("WEX Response is null");
		}
	}
	
	private Map<String, Set<String>> getAlternativeCollectionServerMap(Map<String, List<String>> failedShardsByServerMap) {
		Map<String,Set<String>> collectionServerMap = new HashMap<String,Set<String>>();
				
		for (Map.Entry<String, List<String>> failedShardMapEntry: failedShardsByServerMap.entrySet()){
			String failedServer = failedShardMapEntry.getKey();
			List<String> shardNames = failedShardMapEntry.getValue();
			
			for (String shardName: shardNames){
				Server alternativeServerForShard = determineAlternativeServerForShard(failedServer, shardName);
				if(alternativeServerForShard!=null){
					Set<String> collectionSet = collectionServerMap.get(alternativeServerForShard.getAddress());
					if(collectionSet==null){
						collectionSet = new HashSet<String>(5);
						collectionServerMap.put(alternativeServerForShard.getAddress(), collectionSet);
					}
					collectionSet.add(shardName);
				}
			}
		}
		return collectionServerMap;
	}

	private Server determineAlternativeServerForShard(String failedServerAddress, String shardName) {

		Server failedServer = this.urlFactory.getConfig().getServerByAddress(failedServerAddress);
		CollectionShard collectionShard = this.urlFactory.getConfig().getCollectionShardByName(shardName);
		return collectionShard.getAlternativeServer(failedServer);
	}
	
	private Map<String, Set<String>> getLoadBalancedCollectionServerMap(List<CollectionShard> collectionShards) {
		Map<String,Set<String>> collectionServerMap = new HashMap<String,Set<String>>();
		
		for(CollectionShard shard : collectionShards){
			Server server = serverRoundRobin.getNextServer(shard);
			if(server!=null){
				Set<String> collectionSet = collectionServerMap.get(server.getAddress());
				if(collectionSet==null){
					collectionSet = new HashSet<String>(5);
					collectionServerMap.put(server.getAddress(), collectionSet);
				}
				collectionSet.add(shard.getShardName());
			}
		}
		return collectionServerMap;
	}
	
	@Override
	public String executeQueryModifier(String query) {
		
		query = query.replaceAll("[Aa][Nn][Dd]", "And").replaceAll("\n", " ").replaceAll("\r", " ").replaceAll("\t", " ").replaceAll("\\. ", WexWsConstants.QUERY_MODIFIER_SEPARATOR).replaceAll("\\.$",  WexWsConstants.QUERY_MODIFIER_SEPARATOR).replaceAll(",", WexWsConstants.QUERY_MODIFIER_SEPARATOR);
		
		String keysFound = "";
		JapaneseQueryExtractor jpExtractor = new JapaneseQueryExtractor();
		if (jpExtractor.isJapaneseQuery(query)) {
			
			String jpQuery = jpExtractor.getJapaneseQuery(query);				
			QueryModifierResult response = this.executeQueryModifier(jpQuery, "japanese_context");
			keysFound = response.getXmlTermsAsString();
			
			query = jpExtractor.getNonJapaneseQuery(query);
		}

		if (!query.trim().isEmpty()) {
			QueryModifierResult response = this.executeQueryModifier(query, "");
			keysFound += (!keysFound.isEmpty() ? "," : "") + response.getXmlTermsAsString();
		}
		
		return keysFound;
	}

	
	private QueryModifierResult executeQueryModifier(String searchQuery, String context){
		String url  = this.urlFactory.getQueryModifierURL(searchQuery, context);
		HttpRequest httpRequest = new HttpRequest(url, urlFactory.getConfig(), debug);
		String response = httpRequest.doGet();
		
		Matcher matcher = WexWsConstants.DEFAULT_QUERY_MODIFIER_PATTERN.matcher(response);
		if (context.equals("japanese_context")){
			matcher = WexWsConstants.JAPANESE_QUERY_MODIFIER_PATTERN.matcher(response);
		}
		
		QueryModifierResult result = new QueryModifierResult();
		if (matcher.matches()){
			result.setInterpretationXML(matcher.group(1));
		}
		return result;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	@Override
	public String executeQueryParser(String searchQuery) {
		List<Server> servers = this.urlFactory.getConfig().getServers();
		Server server = queryParserServerRoundRobin.getNextServer(servers);
		String response = runQueryParse(searchQuery, server);
		if (response == null) {
			Server otherServer = getAlternativeServer(server, servers);
			response = runQueryParse(searchQuery, otherServer);
		}
		return response;
	}

	private Server getAlternativeServer(Server failedServer, List<Server> servers) {
		for (Server server: servers) {
			if (server.getId() != failedServer.getId()) {
				return server;
			}
		}
		return failedServer;
	}
	
	private String runQueryParse(String searchQuery, Server server) {
		String response = null;
		try {
			String url  = this.urlFactory.getQueryParserURL(server.getAddress(), searchQuery);
			HttpRequest httpRequest = new HttpRequest(url, urlFactory.getConfig(), debug);
			response = httpRequest.doGet();
		}catch (Exception e) {
			logger.error("Fail to parse query, server="+server.getAddress(), e);
		}
		return response;
	}
}
