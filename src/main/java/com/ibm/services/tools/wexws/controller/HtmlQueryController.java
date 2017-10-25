package com.ibm.services.tools.wexws.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.ibm.services.tools.wexws.domain.Document;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.domain.FacetValue;
import com.ibm.services.tools.wexws.domain.KeywordFilter;
import com.ibm.services.tools.wexws.domain.KeywordFilterLogic;
import com.ibm.services.tools.wexws.domain.OpenSeat;
import com.ibm.services.tools.wexws.domain.Practitioner;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.domain.emFilter;

public abstract class HtmlQueryController extends HtmlController {

	protected String getRestfulRequestsHtml(Response response) {
		if (response.getRestfulRequests() == null)
			return "";
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (String restful : response.getRestfulRequests()) {
			sb.append("<a href=\"").append(restful).append("\">Request #").append(++count).append("</a><br>");
		}

		return sb.toString();
	}
	
	protected String getHtml(Response response, boolean printResume, boolean printScore, boolean mysa, boolean os) {
		StringBuilder sb = new StringBuilder();
		
		if (mysa && response.getKeywordsString().length() == 0) {
			if (os){
				sb.append("<h3><img src=watson_logo_75.png alt=Watson Logo>I did not find any keywords or set any filters based on your query.</h3>");
				return sb.toString();
			}
		}
		
		sb.append("<h4>Total matching records: " + response.getTotalNumberOfDocuments() + " -  Query time (ms): " + response.getQueryTime() + "</h4>");

		if (null != response.getSmartConditions() && response.getSmartConditions() != "" && response.getSmartConditions().trim().length() != 0) {
			sb.append("<h5>Smart Conditions added: " + response.getSmartConditions() + "</h5>");
		}
		
		String partition = response.getEmPartition();
		if (null != partition){
			OpenSeat openSeat = response.getOpenSeat();
			Practitioner practitioner = response.getPractitioner();
			if (null != openSeat){
				sb.append("<h5>Open Seat details: " + openSeat.toString());
				sb.append(" - <a target=_blank href=https://w3beta-2.toronto.ca.ibm.com/services/tools/marketplace/displayOpenSeatProfile.wss?seatID=" + openSeat.getOsID() + ">See in ProM</a>");
				sb.append("</h5>");
			}
			if (null != practitioner){
				sb.append("<h5>Practitioner details: " + practitioner.toString());
				sb.append(" - <a target=_blank href=https://w3beta-2.toronto.ca.ibm.com/services/tools/marketplace/displayPractProfile.wss?R=" + practitioner.getPractitionerId() + "&fp=true>See in ProM</a>");
				sb.append("</h5>");
			}
			
			sb.append("<h5>EM Filters for detected partition: " + response.getEmPartition() + "</h5>");
			
			ArrayList<emFilter> emFilters = (ArrayList<emFilter>) response.getEmFilters();
			sb.append("<table class=\"table-condensed\"><tr>");
			
			for (emFilter emFilter : emFilters) {
				sb.append("<td valign=top>");
				sb.append("<font size=\"1\"><table class=\"table-striped table-bordered table-hover table-condensed\">");
				sb.append("<tr>");
				sb.append("<th colspan='1'>").append(emFilter.getName()).append("</th>");
				sb.append("</tr>");
				sb.append("<tr><td>").append(emFilter.getValues().toString()).append("</td></tr>");
//				sb.append(emFilter.getName() + " - " + emFilter.getValues().toString() + " | ");
				sb.append("</table></font>");
				sb.append("</td>");
			}
			
			sb.append("</tr></table>");
		}
		
		boolean printedKW = false;
		String mustHaveKW = response.getKeywordFiltersMustHaveString().toString();
		if (mustHaveKW.length() > 2){
			sb.append("<h5>Must Have Keywords: " + mustHaveKW + "</h5>");
			printedKW = true;
		}
		
		String niceToHaveKW = response.getKeywordFiltersNiceToHaveString().toString();
		if (niceToHaveKW.length() > 2){
			sb.append("<h5>Nice to Have Keywords: " + niceToHaveKW + "</h5>");
			printedKW = true;
		}
		
		if (!printedKW || mysa){
			String allKeywords = response.getKeywordsString();
			if (allKeywords.length() > 1){
				sb.append("<h5>All Keywords: " + allKeywords + "</h5>");
			}
		}

		try {
			// if (response.getFacets() != null && response.getFacets().size() > 0) {
			if (response.getFacets().entrySet().size() > 0) {
				sb.append("<h4>Facet result(s):</h4>");
				sb.append("<table class=\"table-condensed\"><tr>");
				for (Entry<String, Facet> entry : response.getFacets().entrySet()) {
					sb.append("<td valign=top>");
					sb.append("<font size=\"1\"><table class=\"table-striped table-bordered table-hover table-condensed\">");
					String facetFieldName = entry.getKey();
					Facet facet = entry.getValue();
					sb.append("<tr>");
					sb.append("<th colspan='2'>").append(facetFieldName).append("</th>");
					sb.append("</tr>");
					for (FacetValue fv : facet.getValues()) {
						sb.append("<tr><td>").append(fv.getLabel()).append("</td>").append("<td align='right'>").append(fv.getCount()).append("</td></tr>\n");
					}
					sb.append("</table></font>");
					sb.append("</td>");
				}
				sb.append("</tr></table>");
			}
		} catch (Exception ex) {
			sb.append("<h2>Problem to get FACET. Probably it is null</h2>");
		}

		try {
			if (response.getDocuments().size() > 0) {
				sb.append("<hr>");
				sb.append("<h4>Matching records subset:</h4>");
				sb.append("<table class=\"table table-striped table-bordered table-hover table-condensed\">");
				sb.append("<tr>");
				sb.append("<th>#</th>");
				sb.append("<th>Score</th>");
				sb.append("<th><label title='Keys/Syn Found at fields.' data-toggle='tooltip'>Keys/Syn Found</label></th>");
				sb.append("<th><label title='Count of the keys/Syn Found at fields.' data-toggle='tooltip'>#</label></th>");

				if (printScore) {
					sb.append("<th><label title='Count of the keys/Syn Found at fields, considering repeated keywords.' data-toggle='tooltip'>#T</label></th>");
					sb.append("<th><label title='Fields where we found keywords.' data-toggle='tooltip'>Fields</label></th>");
				}

				for (String fieldName : response.getRequestedFields()) {
					if (printResume) {
						sb.append("<th>").append(fieldName).append("</th>");
					} else {
						if (!fieldName.equalsIgnoreCase("RESUME_TEXT") && !fieldName.equalsIgnoreCase("SKILLS") && !fieldName.equalsIgnoreCase("snippet")) {
							sb.append("<th>").append(fieldName).append("</th>");
						}
					}
				}

				sb.append("</tr>\n");

				int count = 0;
				for (Document doc : response.getDocuments()) {
					count++;
					sb.append("<tr>");
					sb.append("<td>").append(count).append("</td>");
					sb.append("<td>").append(doc.getScore()).append("</td>");
					List<String> keysFound = response.getKeywordsAndSynonyms();
					sb.append("<td>").append(doc.getKeyFound(response.getKeywords(), response.getKeywordFiltersMustHaveString(), response.getKeywordFiltersNiceToHaveString(), response.getSearchKeywordResponseList())).append("</td>");
					sb.append("<td>").append(doc.getNumKeyFoundWeb(keysFound)).append("</td>");

					if (printScore) {
						sb.append("<td>").append(doc.getNumKeyFoundTotalWeb(keysFound)).append("</td>");
						sb.append("<td>").append(doc.getKeyFoundandFieldsWeb(keysFound)).append("</td>");
					}
					for (String fieldName : response.getRequestedFields()) {
						if (printResume) {
							if (fieldName.equals("PRACTITIONER_ID")) {
								sb.append("<td><a target=_blank href=https://w3beta-sso.toronto.ca.ibm.com:444/services/tools/marketplace/displayPractProfile.wss?R=" + doc.getFieldValue(fieldName) + "&fp=true>").append(doc.getFieldValue(fieldName)).append("</a></td>");
							} else if (fieldName.equals("RESUME_TEXT")) { // To print snippets - passar keywords junto
								sb.append("<td>").append(doc.getFieldValue(fieldName, keysFound)).append("</td>");
							} else {
								sb.append("<td>").append(doc.getFieldValue(fieldName)).append("</td>");
							}
						} else {
							if (!fieldName.equalsIgnoreCase("RESUME_TEXT") && !fieldName.equalsIgnoreCase("SKILLS") && !fieldName.equalsIgnoreCase("snippet")) {
								if (fieldName.equals("PRACTITIONER_ID")) { // just to debug
									// https://w3beta-sso.toronto.ca.ibm.com:444/services/tools/marketplace/displayPractProfile.wss?R=AURM6BS2CCQS&fp=true
									sb.append("<td><a target=_blank href=https://w3beta-sso.toronto.ca.ibm.com:444/services/tools/marketplace/displayPractProfile.wss?R=" + doc.getFieldValue(fieldName) + "&fp=true>").append(doc.getFieldValue(fieldName)).append("</a></td>");
								} else {
									sb.append("<td>").append(doc.getFieldValue(fieldName)).append("</td>");
								}
							}
						}
					}
					sb.append("\r\n");
					sb.append("</tr>");
				}

				sb.append("</table>");
			}

		} catch (Exception ex) {
			sb.append("<h2>Problem to get FACET. Probably it is null</h2>");
		}

		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	protected String getJson(Response response) {
		JSONArray arr = new JSONArray();
		try {
			HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
			int i = 0;

			JSONObject json2 = new JSONObject();
			json2.put("Documents", response.getTotalNumberOfDocuments());
			map.put("el" + i, json2);
			arr.add(map.get("el" + i));
			i++;

			JSONObject json3 = new JSONObject();
			json3.put("Query time", response.getQueryTime());
			map.put("el" + i, json3);
			arr.add(map.get("el" + i));
			i++;

			JSONObject json1 = new JSONObject();
			json1.put("Smart Conditions", response.getSmartConditions());
			map.put("el" + i, json1);
			arr.add(map.get("el" + i));
			i++;

			StringBuilder keywordsHTML = new StringBuilder();
			if (response.getKeywordsAndSynonyms() != null) {
				for (String keyword : response.getKeywordsAndSynonyms()) {
					if (keywordsHTML.length() > 2) {
						keywordsHTML.append(",").append(keyword);
					} else {
						keywordsHTML.append(keyword);
					}
				}
			}

			JSONObject json4 = new JSONObject();
			json4.put("Keywords", keywordsHTML.toString());
			map.put("el" + i, json4);
			arr.add(map.get("el" + i));
			i++;

			for (Document doc : response.getDocuments()) {
				JSONObject json = new JSONObject();
				for (String fieldName : response.getRequestedFields()) {
					String fieldValue = doc.getFieldValue(fieldName).replaceAll("</br>", ",").replaceAll("<b>", ",").replaceAll("</b>", ",");
					json.put(fieldName, fieldValue);
				}
				json.put("Keys Found", doc.getKeyFound(response.getKeywordsAndSynonyms()));
				json.put("Num Keys Found", doc.getNumKeyFound(response.getKeywordsAndSynonyms()));
				json.put("Score", doc.getScore());

				map.put("el" + i, json);
				arr.add(map.get("el" + i));
				i++;
			}
		} catch (Exception ex) {
			return ("{'error':'Unable to get JSon - " + ex.getMessage() + "'}");
		}
		return arr.toJSONString();
	}
	

	protected String insertDoubleQuotes(String textSearch) {
		boolean isTextSearch = false;
		String cleanTextSearch = textSearch;
		StringBuffer finalCleanTextSearch = new StringBuffer();
		try {
			if (null != textSearch) {
				ArrayList<String> cleanTextSearchList = new ArrayList<String>(Arrays.asList(cleanTextSearch.split(" ")));
				for (String string : cleanTextSearchList) {
					if (string.contains("TEXTSEARCH")) {
						isTextSearch = true; // if we are handling with TEXTSEARCH
					}
					if (((string.contains("-") || string.contains("&")) && !string.contains("\"") && isTextSearch)) {
						string = "\"" + string + "\"";
					}
					finalCleanTextSearch.append(string);
					finalCleanTextSearch.append(" ");
				}
			}
			return finalCleanTextSearch.toString().trim();
		} catch (Exception ex) {
			System.out.println("ERROR at WexWSAPI - cleanTextSearch-> " + ex.getMessage());
			return textSearch;
		}
	}
	
	protected List<KeywordFilter> buildKeywordFilters(String mustHaveKeys, String niceToHaveKeys) {
		ArrayList<String> mustHaveKeysList = null;
		ArrayList<String> niceToHaveKeysList = null;
		List<KeywordFilter> keywordFiltersList = new ArrayList<KeywordFilter>();
		//String keywordFilters = "";
		try {
			if (!"".equalsIgnoreCase(mustHaveKeys) || !"".equalsIgnoreCase(niceToHaveKeys)) {
				if (!"".equalsIgnoreCase(mustHaveKeys)) {
					mustHaveKeysList = new ArrayList<String>(Arrays.asList(mustHaveKeys.split(",")));
					for (String keyword : mustHaveKeysList) {
						keywordFiltersList.add(new KeywordFilter(keyword.trim() , KeywordFilterLogic.MUST_HAVE));
					}
				}
				if (!"".equalsIgnoreCase(niceToHaveKeys)) {
					niceToHaveKeysList = new ArrayList<String>(Arrays.asList(niceToHaveKeys.split(",")));
					for (String keyword : niceToHaveKeysList) {
						keywordFiltersList.add(new KeywordFilter(keyword.trim(), KeywordFilterLogic.NICE_TO_HAVE));
					}
				}
				//keywordFilters = buildKeywordsSearchString(keywordFiltersList);
			}
		} catch (Exception ex) {
			System.out.println("Error trying to parse filters:" + ex.getMessage());
			//keywordFilters = "";
		}
		
		return keywordFiltersList;
	}
	
	protected List<String> encodeFacetSelectionValues(String value) {
		List<String> values = new ArrayList<String>();
		
		for (String entry : value.split(",")) {
			entry = entry.trim();
			if (!entry.isEmpty()) {
				values.add(entry);
			}
		}
		return values;
	}

}
