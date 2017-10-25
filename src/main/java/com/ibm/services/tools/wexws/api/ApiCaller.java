package com.ibm.services.tools.wexws.api;

import java.util.Arrays;
import java.util.Map.Entry;

import com.ibm.services.tools.wexws.domain.Document;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.domain.FacetRequest;
import com.ibm.services.tools.wexws.domain.FacetSelection;
import com.ibm.services.tools.wexws.domain.FacetValue;
import com.ibm.services.tools.wexws.domain.KeywordFilter;
import com.ibm.services.tools.wexws.domain.KeywordFilterLogic;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.exception.WexWSApiException;
import com.ibm.services.tools.wexws.helper.WexRequestBuilders;

public class ApiCaller {

	public static void main(String[] args) {
		// THIS is an example about how to call wex using wex-qt as an API

		Request request = WexRequestBuilders.searchRequest("PMP_Practitioner");
		request.setFields(Arrays.asList("PRACTITIONER_ID","FULL_NAME","RESUME_TEXT","SKILLS","TALENT_POOL_TYPE","snippet"));
		
		request.setNumberOfRequestedRecords(20);
		request.setNlq(true);
		request.setOntolection(true);
		request.setSmartCondition(true);
		request.setStemming(false);
		request.setSpelling(false);
		request.setJapaneseSearch(true);
		
		// Adding FACETS to request
		addFacetRequests(request);
		
		// Adding FACETS as filters
		// request.addFacetSelection(new FacetSelection("WORK_LOCATION_IOT", Arrays.asList("Greater China Group")));
		request.addFacetSelection(new FacetSelection("DEPLOYABILITY", Arrays.asList("Full")));
		// request.addFacetSelection(new FacetSelection("RESOURCE_TYPE", Arrays.asList("GR")));
		// request.addFacetSelection(new FacetSelection("MYSA_TALENT_POOL_LOB", Arrays.asList("GBS")));
		// request.addFacetSelection(new FacetSelection("TALENT_POOL_TYPE", Arrays.asList("Contractor")));
		// request.addFacetSelection(new FacetSelection("MYSA_PRCTNR_IND", Arrays.asList("1")));
		request.addFacetSelection(new FacetSelection("TALENT_POOL_TYPE", Arrays.asList("Affiliate", "IBM Regular")));
		// request.addFacetSelection(new FacetSelection("SERVICE", Arrays.asList("CAM")));
		// request.addFacetSelection(new FacetSelection("SERVICE_AREA", Arrays.asList("CAM-Complex SI &amp; Architecture EC")));
		// request.addFacetSelection(new FacetSelection("ORIGINAL_BAND", Arrays.asList("9")));
		// request.addFacetSelection(new FacetSelection("AVAILABILITY_DATE_WITH_OFFSET", Arrays.asList("1482364800")));
		// request.addFacetSelection(new FacetSelection("AVAILABILITY_DATE_WITH_OFFSET", Arrays.asList("&gt; 1480809600")));
		// request.addFacetSelection(new FacetSelection("BAND_SORT", Arrays.asList("&gt; 102")));
		// request.addFacetSelection(new FacetSelection("BAND_SORT", Arrays.asList("&lt; 130")));
	
		// KEYWORDS
		// request.addKeywordFilter(new KeywordFilter("portuguese", KeywordFilterLogic.NICE_TO_HAVE));
		request.addKeywordFilter(new KeywordFilter("\"java developer\"", KeywordFilterLogic.MUST_HAVE));
		request.addKeywordFilter(new KeywordFilter("\"ionic\"", KeywordFilterLogic.MUST_HAVE));
		// request.addKeywordFilter(new KeywordFilter("xcode", KeywordFilterLogic.MUST_HAVE));
		// request.addKeywordFilter(new KeywordFilter("android", KeywordFilterLogic.MUST_HAVE));
		// request.addKeywordFilter(new KeywordFilter("ionic", KeywordFilterLogic.MUST_HAVE));

		try {
			WexWSApi wexWS = new WexWSApi("prod"); // systest dev sbox
			// Normal query
			Response wexResponseObject = wexWS.executeQuery(request);
			
			// FACETS
			try {
				if (wexResponseObject.getFacets().entrySet().size() > 0) {
					System.out.println("-----------------------------------------");
					System.out.println("###### FACETS ######");
					System.out.println("-----------------------------------------");
					for (Entry<String, Facet> entry : wexResponseObject.getFacets().entrySet()) {
						String facetFieldName = entry.getKey();
						Facet facet = entry.getValue();
						System.out.println("###### " + facetFieldName + " ######");
						for (FacetValue fv : facet.getValues()) {
							System.out.println(fv.getLabel() + " - " + fv.getCount());
						}
						System.out.println("--------------------------------------------");
					}
				}
			} catch (Exception ex) {
				System.out.println("Error!");
			}

			// DOCUMENTS
			if (wexResponseObject.getTotalNumberOfDocuments() > 0) {
				System.out.println("-----------------------------------------");
				System.out.println("###### DOCUMENTS ######");
				System.out.println("-----------------------------------------");
			}
			try {
				int pos = 0;
				for (Document doc : wexResponseObject.getDocuments()) {
					pos++;
					System.out.print("| " + pos + " |");
//					System.out.print("| " + doc.getNumKeyFound(wexResponseObject.getKeywords()) + " ");
//					System.out.print("| " + doc.getKeyFound(wexResponseObject.getKeywords()) + " |");
//					System.out.print("| " + doc.getNumKeyFound(wexResponseObject.getKeywordsAndSynonyms()) + " ");
//					System.out.print("| " + doc.getKeyFound(wexResponseObject.getKeywordsAndSynonyms()) + " |");
					
					System.out.print("| " + doc.getKeyFound(wexResponseObject.getKeywords(),wexResponseObject.getKeywordFiltersMustHaveString(), wexResponseObject.getKeywordFiltersNiceToHaveString(), wexResponseObject.getSearchKeywordResponseList()) + " |");
					System.out.print("| " + doc.getScore() + " |");
					
					for (String fieldName : wexResponseObject.getRequestedFields()) {
						String fieldValue = "";
						if (fieldName.equalsIgnoreCase("RESUME_TEXT")){
							//fieldValue = doc.getFieldValue("RESUME_TEXT",wexResponseObject.getKeywords()).replaceAll("</br>", ",");
							fieldValue = "LIMPEI";
						}else{
							if (!fieldName.equalsIgnoreCase("snippet")){ // JUST TO AVOID PRINT snippet
								fieldValue = doc.getFieldValue(fieldName).replaceAll("</br>", ",");
							}
						}
						System.out.print(" " + fieldName + " - " + fieldValue + " |");	
					}
					System.out.println();
				}
			} catch (Exception ex) {
				System.out.println("Error! " + ex.getMessage());
			}
			
			// After get the object, need to manipulate
			System.out.println("Keywords ->" + wexResponseObject.getSearchKeywordResponseList());
			System.out.println("Keywords List -> " + wexResponseObject.getKeywords());
			System.out.println("Keywords List and Synonyms -> " + wexResponseObject.getKeywordsAndSynonyms());
			System.out.println("Smart Conditions ->" + wexResponseObject.getSmartConditions());
			// you will need to use getSmartConditionsList for the next line, im using PRINT just to debug
			System.out.println("Smart Conditions List ->" + wexResponseObject.printSmartConditionsList());
			
			System.out.println("Nice to Have KW->" + wexResponseObject.getKeywordFiltersNiceToHaveString());
			System.out.println("Must Have KW->" + wexResponseObject.getKeywordFiltersMustHaveString());
			System.out.println("Total # of docs: " + wexResponseObject.getTotalNumberOfDocuments());

			
			wexResponseObject.printSmartConditionsList();
			System.out.println(wexResponseObject.getFacetsString());
			
		} catch (WexWSApiException e) {
			e.printStackTrace();
		}
	}

	private static void addFacetRequests(Request request) {	
		FacetRequest ORIGINAL_BAND = WexRequestBuilders.facetRequest("ORIGINAL_BAND").withMaximumNumberOfValues(5);
		request.addFacetRequest(ORIGINAL_BAND);
		
		FacetRequest BAND_SORT = WexRequestBuilders.facetRequest("BAND_SORT").withMaximumNumberOfValues(5);
		request.addFacetRequest(BAND_SORT);
	}

}
// end