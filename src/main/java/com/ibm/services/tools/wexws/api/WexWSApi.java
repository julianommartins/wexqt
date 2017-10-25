package com.ibm.services.tools.wexws.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.WexWsConstants;
import com.ibm.services.tools.wexws.bo.QueryBO;
import com.ibm.services.tools.wexws.bo.SmartConditionBuilder;
import com.ibm.services.tools.wexws.domain.FacetRequest;
import com.ibm.services.tools.wexws.domain.FacetSelection;
import com.ibm.services.tools.wexws.domain.KeywordClassification;
import com.ibm.services.tools.wexws.domain.KeywordFilter;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.exception.WexWSApiException;
import com.ibm.services.tools.wexws.factory.WexRestfulFactory;
import com.ibm.services.tools.wexws.factory.WexSmartConditionDataProviderFactory;
import com.ibm.services.tools.wexws.helper.CustomKeywordsExtractor;
import com.ibm.services.tools.wexws.helper.WexRequestBuilders;

/**
 * This is the controller that will get requests from applications and send to wex, using an embebed WEX-WS code
 * 
 * @author julianom
 *
 */
public class WexWSApi {
	private static ArrayList<String> combinacoes = new ArrayList<String>();
	private QueryBO queryBO;
	private WexConfiguration wexConfiguration;
	private SmartConditionBuilder smartConditionBuilder;
	private static String env = "";
		
	public WexWSApi(String environment) throws WexWSApiException{
		this(environment, true);
	}
	
	public WexWSApi(String environment, boolean initSmartConditionDataProvider) throws WexWSApiException{
		if (null == environment || environment.length() == 0){
			throw new WexWSApiException("Need to set Environment. Ex: sbox, dev");
		}
		env = environment;
		wexConfiguration = new WexConfiguration(env);
		queryBO = new QueryBO(WexRestfulFactory.getInstance(environment));
		if (initSmartConditionDataProvider) {
			smartConditionBuilder = new SmartConditionBuilder(WexSmartConditionDataProviderFactory.getWexDataProviderInstance(environment));
		}
	}
	
	/**
	 * The executeQuery method will receive all required/optional arguments in a Request object, parse and send to WEX-WS engine, that will prepare the query, send to Watson Explorer, parse the
	 * results/error and return an object of type Response
	 * 
	 * @param req
	 *            - the Request object
	 * @return Response - the object with the first 250 documents retrieved from WEX, The requested facets, and some more details
	 * @throws WexWSApiException
	 */
	public Response executeQuery(Request req) throws WexWSApiException {
		validateRequest(req);
		
		String smartConditions = getSmartConditions(req);
		if (smartConditions != null && smartConditions.length() > 0){
			req.addFilter(WexRequestBuilders.xpathFilter(smartConditions));
		}
		
		if (req.isNlq() || req.isSmartCondition()) {
			req.setQuery(clearQueryString(req.getQuery()));
		}
		
		try {
			Response response = queryBO.executeQuery(req);
			if (null != smartConditions && smartConditions.length()>3){
				String[] array = (smartConditions.replaceAll("[()]", "").replaceAll("' and |' or ", "' ,")).split(","); //.replaceAll("\\s+","")
				for (String string : array) {
					FacetSelection fs = new FacetSelection();
					fs.setName(string.substring(1, string.indexOf("=")));
					fs.addSelection(string.substring(string.indexOf("'") + 1, string.lastIndexOf("'")));
					response.addSmartConditionsListItem(fs);
				}
			}
			response.setSmartConditions(smartConditions);
			return response;
		} catch (Exception ex) {
			throw new WexWSApiException("Error while running query: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * This method execute the combination of queries using keywords and returning the number of returned rows per query
	 * @param req
	 * @return
	 * @throws WexWSApiException
	 */
	public Response executeQueryKeywords(Request req) throws WexWSApiException {
		validateRequest(req);
		
		String smartConditions = getSmartConditions(req);
		if (smartConditions != null && smartConditions.length() > 0){
			req.addFilter(WexRequestBuilders.xpathFilter(smartConditions));
		}
		
		req.setQuery(clearQueryString(req.getQuery()));
		//req.setNumberOfRequestedRecords(getResultsNumberCluster(req.getNumberOfRequestedRecords()));
		try {
			Response response = queryBO.executeQuery(req);
			return response;
		} catch (Exception ex) {
			throw new WexWSApiException("Error while running query: " + ex.getMessage());
		}
	}
	
	/**
	 * This method should receive a NLQ request and based on the extracted keywords, classify them as "must have" or "nice to have" 
	 * @param Request - the request with the NLQ query 
	 * @return a KeywordClassifier object with the keywords classification lists
	 */
	public KeywordClassification getClassifiedKeywords(Request req) throws WexWSApiException {
		
		if (!req.isNlq()) {
			throw new WexWSApiException("The classified keywords can only be extracted from a NLQ request.");
		}
		
		String originalQuery = req.getQuery();
		String cleanQuery = insertDoubleQuotes(originalQuery);
		try {
			if (cleanQuery == null || cleanQuery.length() == 0) {
				throw new WexWSApiException("The classified keywords can only be extracted from a non-empty NLQ query.");
			}

			String keysFound = queryBO.executeQueryModifier(cleanQuery);			
			return new KeywordClassification(originalQuery, keysFound);
		
		} catch (Exception ex) {
			throw new WexWSApiException("Error while running query: " + ex.getMessage());
		}
	}
	
	public List<KeywordFilter> extractKeywords(String text) throws WexWSApiException {
		try {
			CustomKeywordsExtractor extractor = CustomKeywordsExtractor.getInstance();
			return extractor.findKeywords(text);
		} catch (Exception ex) {
			throw new WexWSApiException("Error while running query: " + ex.getMessage());
		}
	}

	public String executeQueryParse(String searchQuery) {
		return queryBO.executeQueryParse(searchQuery);
	}
	
	private String clearQueryString(String queryString){
		String cleanQuery = queryString;
		String finalCleanQuery = "";
		try{
			if (null != cleanQuery && !cleanQuery.equals("")){
				// 1- CLEAN like WEX-WS UI
				cleanQuery = cleanQuery.replaceAll(",", " ").replaceAll("\n", " ").replaceAll("\\.", " ").replaceAll("\r", " ").replaceAll("\t", " ");//.replaceAll("&", "%26");
				finalCleanQuery = cleanQuery;
				// 2- Remove WEX-WS stopwords
				cleanQuery = removeStopWords(cleanQuery);
				finalCleanQuery = cleanQuery;
				// 3- Insert double quotes when key have - or &
			    finalCleanQuery = insertDoubleQuotes(finalCleanQuery);
			    finalCleanQuery = finalCleanQuery.replaceAll(" and ", " AND ");
			}
		} catch (Exception ex){
			System.out.println("ERROR when trying to clean QUERY: " + queryString);
		}
		return finalCleanQuery;
	}
	
	private static String insertDoubleQuotes(String textSearch){
		String cleanTextSearch = textSearch;
		StringBuffer finalCleanTextSearch = new StringBuffer();
		try{
			if (null != textSearch){
				ArrayList<String> cleanTextSearchList = new ArrayList<String>(Arrays.asList(cleanTextSearch.split(" ")));
				for (String string : cleanTextSearchList) {
					if ((string.contains("-") || string.contains("&")) && !string.contains("\"")){
						string = "\"" + string + "\"";
					}
					finalCleanTextSearch.append(string);
					finalCleanTextSearch.append(" ");
				}
			}
			return finalCleanTextSearch.toString().trim();	
		}catch(Exception ex){
			System.out.println("ERROR at WexWSAPI - cleanTextSearch-> " + ex.getMessage());
			return textSearch;
		}
	}
	
	private String getSmartConditions(Request req) {
		if (req.isSmartCondition() && req.getQuery() != null && req.getQuery().length() > 0){
			String smartConditionalXPath = smartConditionBuilder.getSmartConditionsXPath(req.getQuery(), 
							req.getSmartConditionOperator(),null);
			
			return smartConditionalXPath;
		}
		return null;		
	}
	
	
	
	
	@SuppressWarnings("unused")
	private String getIOT(Request req){
		String IOT = null;
		try{
			if (null != req.getFacetSelections()) {
				for (FacetSelection fs : req.getFacetSelections()) {
					if (fs.getName().equalsIgnoreCase("WORK_LOCATION_IOT")) {
						IOT = fs.getSelections().get(0);
					}
				}
			}
		} catch(Exception ex){
			System.out.println("ERROR at getIOT:" + ex.getMessage());
		}
		return IOT;
	}
	
	private String removeStopWords(String textSearch){
		String cleanTextSearch = textSearch;
		try{
			if (null != textSearch){
				//.replaceAll("\\.", "")
				cleanTextSearch = textSearch.replaceAll(",", "").replaceAll("'", "").replaceAll("(\\r|\\n)", "").replaceAll("\\s+", " ");
				ArrayList<String> cleanTextSearchList = new ArrayList<String>(Arrays.asList(cleanTextSearch.toLowerCase().split(" ")));
				cleanTextSearchList.removeAll(WexWsConstants.stopWords);
				cleanTextSearch = cleanTextSearchList.toString().replace(",", "").replace("[", "").replace("]", "").trim();
			}
			return cleanTextSearch;	
		}catch(Exception ex){
			System.out.println("ERROR at WexWSAPI - cleanTextSearch-> " + ex.getMessage());
			return textSearch;
		}
	}

	private void validateRequest(Request req){
		validateParameters(req);
		//validateFacetSelections(req);
	}
	
	private void validateParameters(Request req) throws WexWSApiException {
		if (null == req.getCollection() || req.getCollection().length() == 0) {
			throw new WexWSApiException("Need collection parameter. Ex: PMP_Practitioner");
		}
	}


	@SuppressWarnings("unused")
	private void validateFacetSelections(Request req) {
		if (req.getFacetSelections() != null){
			for (FacetSelection selection: req.getFacetSelections()){
				String facetName = selection.getName();
				boolean foundFacetRequest = false;
				if (req.getFacets() != null){
					for (FacetRequest facRequest: req.getFacets()){
						if (facRequest.getFacetName().equals(facetName)){
							foundFacetRequest = true;
							break;
						}
					}
				}
				if (!foundFacetRequest){
					throw new WexWSApiException("The facet selections for facet name = "+facetName+" has no correspondent facet request");
				}
			}
		}
	}
	@SuppressWarnings("unused")
	@Deprecated
	private int getResultsNumberCluster(int numRows){
		try {
			if (numRows == 0){ // the case that we search for IDs
				return 20;
			}
			if (env.equalsIgnoreCase("systest") && numRows >= 5) {
				return(numRows / 2);
			} else if ((env.equalsIgnoreCase("prod") || env.equalsIgnoreCase("dev")) && numRows >= 5) {
				return(numRows / 3);
			}
		} catch (Exception ex) {
			// Change - logger
			System.out.println("ERROR while setting number of requested records based on environment: " + ex.getMessage());
		}
		return 10;
	}
	
	static void combinationUtil(String arr[], String data[], int start, int end, int index, int r) {
		if (index == r) {
			String combinacao = "";
			for (int j = 0; j < r; j++) {
				if (combinacao.length() > 1){
					combinacao += ",";
				}
				combinacao += data[j];
			}
			combinacoes.add(combinacao.trim());
			return;
		}

		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = arr[i];
			combinationUtil(arr, data, i + 1, end, index + 1, r);
		}
	}

	public static void createCombination(String arr[], int n, int r) {
		String data[] = new String[r];
		combinationUtil(arr, data, 0, n - 1, 0, r);
	}

}
