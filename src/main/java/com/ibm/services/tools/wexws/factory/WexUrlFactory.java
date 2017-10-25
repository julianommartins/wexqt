package com.ibm.services.tools.wexws.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.WexWsConstants;
import com.ibm.services.tools.wexws.bo.RequestWexURLTranslator;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.SortBy;
import com.ibm.services.tools.wexws.domain.SortOrder;
import com.ibm.services.tools.wexws.domain.WexQueryModifierURL;
import com.ibm.services.tools.wexws.domain.WexQueryParseURL;
import com.ibm.services.tools.wexws.domain.WexRestfulURL;
import com.ibm.services.tools.wexws.wql.WQL;

public class WexUrlFactory {
		
	private WexConfiguration config;
			
	public WexUrlFactory(WexConfiguration config) {
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	public  Object[] getRestfulURL(WQL wql, String queryObjectSearchTerms, boolean debug, boolean profile, boolean nlq, boolean smartCondition, boolean ontolection, String smartConditionOperator, Map<String, Set<String>> collectionServerMap, boolean bold, boolean stemming, boolean spelling, boolean japaneseSearch) throws Exception{
		Object[] arrayReturn = {"",""};
		Object[] queriesResponse = getQueries(collectionServerMap, wql, queryObjectSearchTerms, debug, profile, nlq, smartCondition, ontolection, smartConditionOperator, bold, stemming, spelling, japaneseSearch);
		List<String> restfulUrls = (List<String>)queriesResponse[0];
		String smartConditions = (String)queriesResponse[1];
		
		//return restfulUrls;
		arrayReturn[0] = restfulUrls;
		arrayReturn[1] = smartConditions;
		
		return arrayReturn;
		
	}
	
	private Object[] getQueries(
			Map<String, Set<String>> collectionServerMap, WQL wql, String queryObjectSearchTerms, boolean debug, boolean profile, boolean nlq, boolean smartCondition, boolean ontolection, String smartConditionOperator, boolean bold, boolean stemming, boolean spelling, boolean japaneseSearch) {
		Object[] arrayReturn = {"",""};
		
		String smartConditions = "";
		List<String> queries = new ArrayList<String>();
		
		List<String> fields = wql.getRequestedFields();
		if(debug) show("Fields",fields);
		
		List<String> facets = wql.getRequestedFacets();
		if(debug) show("Facets",facets);
		
		String collection = wql.getCollection();
		if(debug) show("Collections",collection);
		
		String arena = config.getArena(collection);
		if(debug) show("Arena",arena);

		String textSearch  = cleanTextSearch(wql.getTextSearch());
		if(debug) show("TextSearch",textSearch);
		
		String criteria  = wql.getCriteria();
		if(debug) show("Criteria",criteria);
		
		int startAt = wql.getStartAt();
		if(debug) show("Start",startAt);
		
		int max = wql.getNum();
		if(debug) show("Num",max);
		
		String sortField = wql.getSortFieldName();
		if(debug) show("SortBy",sortField);
		String sortOrder = wql.getSortOrder();
		if(debug) show("SortBy Order",sortOrder);
		
		SortBy sortBy = null;
		if (sortField != null && sortField.length() > 0){
			SortOrder order = SortOrder.ASCENDING;
			if (sortOrder != null && sortOrder.toLowerCase().startsWith("desc")){
				order = SortOrder.DESCENDING;
			}
			sortBy = new SortBy(sortField, order);
		}
		
//		if(debug){
//			for(Entry<String, Set<String>> entry : collectionServerMap.entrySet()){
//			    System.out.println("WEX Server: "+entry.getKey());
//			    for(String col : entry.getValue()){
//				  System.out.println("Wex collection: "+col);
//			    }
//			}
//		}
		
		String ontolectionName = null;
		if (ontolection){
			ontolectionName = config.getOntolectionNameByCollection(collection);
		}
		
		int numberOfShards = collectionServerMap.keySet().size();
		
		for(String server : collectionServerMap.keySet()){
		    Set<String> collectionsSet = collectionServerMap.get(server);
			WexRestfulURL wexRestfulurl = new WexRestfulURL(server,config,fields,facets,arena,collectionsSet,textSearch,criteria,queryObjectSearchTerms, startAt,max,numberOfShards,sortBy,profile,nlq, stemming, spelling,japaneseSearch);
			
			Object obj[] = wexRestfulurl.getQuery(smartCondition, ontolectionName, smartConditionOperator, bold);
			queries.add((String)obj[0]);
			smartConditions = (String)obj[1];
		}
		
//		if(debug){
//			for (String string : queries) {
//				System.out.println("URL: " + string);
//			}
//		}
		
		//return queries;
		arrayReturn[0] = queries;
		arrayReturn[1] = smartConditions;
		
		return arrayReturn;
	}
	
	/**
	 * Write the URL
	 * @param collectionServerMap
	 * @param searchRequest
	 * @return
	 */
	public List<String> getQueries(Map<String, Set<String>> collectionServerMap, Request searchRequest) {
		List<String> queries = new ArrayList<String>(4);
		int numberOfShards = collectionServerMap.keySet().size();
		String arena = config.getArena(searchRequest.getCollection());
		
		for(String server : collectionServerMap.keySet()){
		    Set<String> collectionsSet = collectionServerMap.get(server);
			WexRestfulURL wexRestfulurl = new WexRestfulURL(server,config,arena,collectionsSet, numberOfShards, new RequestWexURLTranslator(searchRequest, config));
			String queryURL = wexRestfulurl.getQuery();
			queries.add(queryURL);
		}
		return queries;
	}

	public String getQueryModifierURL(String searchQuery, String context){
		WexQueryModifierURL url = new WexQueryModifierURL(config.getServers().get(0).getAddress(), config, context, searchQuery);
		return url.getQueryModifierURL();
	}
	
	public String getQueryParserURL(String server, String searchQuery){
		WexQueryParseURL url = new WexQueryParseURL(server, config, searchQuery);
		return url.getQueryParseURL();
	}
	
	private void show(String name, List<String> values) {
		System.out.print(name+": ");
		
		for(int i=0; i<values.size() ; i++){
			if(i>0) System.out.print(",");
			System.out.print(values.get(i));
		}
	}
	
	/**
	 * Remove stopWords before send the request to query modifier / wex
	 * and convert AND to and to be used as syntax operators, avoiding replace content inside keywords like "Test and validation"
	 * @param textSearch
	 * @return
	 */
	private String cleanTextSearch(String textSearch){
		System.out.println("AT cleanTextSearch");
		
		String cleanTextSearch = textSearch;
		try{
			if (null != textSearch){
				//System.out.println("Ill clean --> " + textSearch); //.replaceAll("\\.", "")
				cleanTextSearch = textSearch.replaceAll(",", "").replaceAll("'", "").replaceAll("(\\r|\\n)", "").replaceAll("\\s+", " ");
				ArrayList<String> cleanTextSearchList = new ArrayList<String>(Arrays.asList(cleanTextSearch.toLowerCase().split(" ")));

				cleanTextSearchList.removeAll(WexWsConstants.stopWords);
				
				cleanTextSearch = cleanTextSearchList.toString().replace(",", "").replace("[", "").replace("]", "").trim();
				
				System.out.println("Clean 1 --> " + cleanTextSearch);
				// Fixing to make syntax operators work and do not change it - we can have cases like "A and B" as keyword
				cleanTextSearch = cleanTextSearch.replaceAll(" and ", " AND ");
				cleanTextSearch = cleanTextSearch.replaceAll(" or ", " OR ");
				
				System.out.println("Clean 2 --> " + cleanTextSearch);
				cleanTextSearch = cleanTextSearch.replaceAll(" AND \"", " and \""); // when using priority ex: "java"^20 and "ionic"^100 
				cleanTextSearch = cleanTextSearch.replaceAll(" OR \"", " or \""); // when using priority ex: "java"^20 or "ionic"^100 
				
				System.out.println("Clean 3 --> " + cleanTextSearch);
				cleanTextSearch = cleanTextSearch.replaceAll("\" AND \"", "\" and \"");
				cleanTextSearch = cleanTextSearch.replaceAll("\" OR \"", "\" or \"");
				
				System.out.println("Clean 4 --> " + cleanTextSearch);
				cleanTextSearch = cleanTextSearch.replaceAll("\\) OR \\(", "\\) or \\(");
				cleanTextSearch = cleanTextSearch.replaceAll("\\) AND \\(", "\\) and \\(");
				
				System.out.println("Final Clean --> " + cleanTextSearch);
			}
			return cleanTextSearch;	
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Error while cleanning text Search-->" + ex.getMessage());
			return textSearch;
		}
	}

	private void show(String name, String value) {
		System.out.println(name+": "+value);
	}
	
	private void show(String name, int value) {
		System.out.println(name+": "+value);
	}

	public WexConfiguration getConfig() {
		return config;
	}
}