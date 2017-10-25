package com.ibm.services.tools.wexws.bo;

import java.util.ArrayList;
import java.util.List;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.customfacets.FacetMapper;
import com.ibm.services.tools.wexws.domain.AddedSource;
import com.ibm.services.tools.wexws.domain.Bin;
import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.CustomFacetRequest;
import com.ibm.services.tools.wexws.domain.Facet;
import com.ibm.services.tools.wexws.domain.FacetRequest;
import com.ibm.services.tools.wexws.domain.FacetValue;
import com.ibm.services.tools.wexws.domain.Operator;
import com.ibm.services.tools.wexws.domain.QuerySearchKeywordTerm;
import com.ibm.services.tools.wexws.domain.QueryWEXElement;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.Response;
import com.ibm.services.tools.wexws.domain.SearchKeywordResponse;
import com.ibm.services.tools.wexws.domain.WexQueryResults;

/**
 * This class is responsible to build a single Response object by merging multiple {@link WexQueryResults} instances, 
 * one instance per collection shard.
 * @author Cesar
 *
 */
public class DataCollector {

	private Response response;
	private Request request;
	private WexConfiguration configuration;

	public DataCollector(Response response, Request request, WexConfiguration configuration) {
		this.response = response;
		this.request = request;
		this.configuration = configuration;
	}

	public void collect(WexQueryResults results) {

		collectTotalResults(results);

		collectFacets(results);

		collectDocuments(results);
		
		collectSearchKeywords(results);

	}

	private void collectDocuments(WexQueryResults results) {
		if (results.getMatchesList() != null){
			response.addAllDocuments(results.getMatchesList().getDocuments());
		}
	}

	private void collectFacets(WexQueryResults results) {
		if (results.getBinning() == null || results.getBinning().getBinningSet() == null)
			return;
		
		List<Facet> facets = getCustomFacets(results);
		facets.addAll(getFacets(results));
		
		for (Facet facet : facets) {
			Facet existingFacet = response.getFacetByName(facet.getName());
			if (existingFacet == null) {
				response.putFacet(facet);
			}else{
				mergeFacet(existingFacet, facet);
			}
		}
	}

	private void mergeFacet(Facet existingFacet, Facet facet) {
		List<FacetValue> facetValues = facet.getValues();
		List<FacetValue> existingValues = existingFacet.getValues();
		for (FacetValue existingValue: existingValues) {
			FacetValue value = getFacetValueByLabel(facetValues, existingValue.getLabel());
			if (value != null) {
				int v1 = existingValue.getCount();
				int v2 = value.getCount();
				int total = v1 + v2;
				existingValue.setCount(total);
			}
		}
		facetValues.removeAll(existingValues);
		existingValues.addAll(facetValues);
		
	}

	private FacetValue getFacetValueByLabel(List<FacetValue> values, String label) {
		FacetValue facetValue = null;
		for (FacetValue fv : values) {
			if (fv.getLabel().equals(label)){
				facetValue = fv;
				break;
			}
		}
		return facetValue;
	}

	private void collectTotalResults(WexQueryResults results) {
		if (results.getAddedSourceList() != null){
			String ontolectionName = configuration.getOntolectionNameByCollection(request.getCollection());
			for (AddedSource source : results.getAddedSourceList()) {
				if (source.getName().equals(ontolectionName)){
					continue;
				}
				response.setTotalNumberOfDocuments(response.getTotalNumberOfDocuments() + source.getTotalResults());
			}	
		}
	}
	
	private void collectSearchKeywords(WexQueryResults results) {
		if (results.getQueryWEXElementList() != null){
			for (QueryWEXElement queryElement: results.getQueryWEXElementList()){
				if (queryElement.getOperators() != null){
					for (Operator operatorElement: queryElement.getOperators()){
						collectKeywordsFromOperatorElement(operatorElement);
					}
				}
			}
		}
	}
	
	private void collectKeywordsFromOperatorElement(Operator operatorElement){
		if (operatorElement.getQuerySearchKeywordTermList() != null){
			List<QuerySearchKeywordTerm> termList = operatorElement.getQuerySearchKeywordTermList();
			List<SearchKeywordResponse> userInputtedKeywords = getUserInputtedKeywordList(termList);
			
			if (userInputtedKeywords != null && userInputtedKeywords.size() == 1) {
				SearchKeywordResponse userInputtedKeyword = userInputtedKeywords.get(0);
				userInputtedKeyword.setSynonymList(getSynonymList(termList));
				
				response.addSearchKeywordResponse(userInputtedKeyword);
			}
		}
		if (operatorElement.getNestedOperators() != null){
			for (Operator nestedOperator: operatorElement.getNestedOperators()){
				collectKeywordsFromOperatorElement(nestedOperator);
			}
		}
	}
	
	private List<SearchKeywordResponse> getUserInputtedKeywordList(List<QuerySearchKeywordTerm> termList){
		List<SearchKeywordResponse> userInputtedKeywordList = null;
		for (QuerySearchKeywordTerm term: termList){
			if ("user".equalsIgnoreCase(term.getInputType())){
				if (userInputtedKeywordList == null){
					userInputtedKeywordList = new ArrayList<SearchKeywordResponse>(5);
				}
				userInputtedKeywordList.add(buildKeywordResponseFromTermElement(term));
			}
		}
		return userInputtedKeywordList;
	}
	
	private List<SearchKeywordResponse> getSynonymList(List<QuerySearchKeywordTerm> termList){
		List<SearchKeywordResponse> synonymList = null;
		for (QuerySearchKeywordTerm term: termList){
			if ("synonym".equalsIgnoreCase(term.getRelationType()) || "stemming".equalsIgnoreCase(term.getRelationType())){
				if (synonymList == null){
					synonymList = new ArrayList<SearchKeywordResponse>(5);
				}
				synonymList.add(buildKeywordResponseFromTermElement(term));
			}
		}
		return synonymList;
	}
	
	private SearchKeywordResponse buildKeywordResponseFromTermElement(QuerySearchKeywordTerm term){
		SearchKeywordResponse synonym = new SearchKeywordResponse();
		synonym.setField(term.getField());
		synonym.setValue(term.getValue());
		synonym.setWeight(term.getWeight());
		return synonym;
	}

	private List<Facet> getCustomFacets(WexQueryResults wexQueryResults){
		List<Facet> facets = new ArrayList<Facet>();
		if (request != null && request.getFacets() != null){
			for (FacetRequest facetRequest: request.getFacets()){
				if (facetRequest instanceof CustomFacetRequest){
					FacetMapper mapper = ((CustomFacetRequest)facetRequest).getFacetMapper();
					facets.add(mapper.fromNative(wexQueryResults.getBinning().getBinningSet()));
				}
			}
		}
		return facets;
	}
	
	private List<Facet> getFacets(WexQueryResults wexQueryResults){
		List<Facet> facets = new ArrayList<Facet>();
		if (wexQueryResults.getBinning() != null && wexQueryResults.getBinning().getBinningSet() != null){
			for (BinningSet binningSet: wexQueryResults.getBinning().getBinningSet()){
				if (binningSet.getBins() != null){
					Facet facet = new Facet(binningSet.getId());
					for (Bin bin: binningSet.getBins()){
						FacetValue value = new FacetValue(bin.getLabel(), bin.getNdocs(), bin.getLabel());
						facet.addFacetValue(value);
					}
					facets.add(facet);
				}
			}
		}
		return facets;
	}
}
