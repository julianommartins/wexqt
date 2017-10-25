package com.ibm.services.tools.wexws.bo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.customfacets.FacetMapper;
import com.ibm.services.tools.wexws.domain.BinningSet;
import com.ibm.services.tools.wexws.domain.CustomFacetRequest;
import com.ibm.services.tools.wexws.domain.FacetRequest;
import com.ibm.services.tools.wexws.domain.FacetSelection;
import com.ibm.services.tools.wexws.domain.Filter;
import com.ibm.services.tools.wexws.domain.KeywordFilterLogic;
import com.ibm.services.tools.wexws.domain.KeywordFilter;
import com.ibm.services.tools.wexws.domain.Request;
import com.ibm.services.tools.wexws.domain.SortBy;
import com.ibm.services.tools.wexws.factory.WexRestfulFactory;
import com.ibm.services.tools.wexws.helper.QueryObjectVisitor;
import com.ibm.services.tools.wexws.helper.WexRequestBuilders;
/**
 * This class is responsible to translate a {@link Request} to the WEX REST API parameters
 * @author Daniel Paganotti
 *
 */
public class RequestWexURLTranslator implements WexRestURLTranslator {

	private Request searchRequest;
	private WexConfiguration configuration;
	private List<String> facetSelections;	
	private List<FacetSelectionCondition> facetSelectionConditions;	
	
	
	public RequestWexURLTranslator(Request searchRequest, WexConfiguration configuration){
		this.searchRequest = searchRequest;
		this.configuration = configuration;
		processFacetSelections();
	}
	
	private class FacetSelectionCondition {
		private String facetSelection;
		private String operator;
		
		public FacetSelectionCondition(String facetSelection, String operator) {
			this.facetSelection = facetSelection;
			this.operator = operator;
		}
		public FacetSelectionCondition(String facetSelection) {
			this.facetSelection = facetSelection;
			this.operator = "and";
		}
		public String getFacetSelection() {
			return facetSelection;
		}
		public String getOperator() {
			return operator;
		}
		public String toString() {
			return facetSelection + " - " + operator;
		}
	}
	
	private void processFacetSelections() {
		if (searchRequest.getFacetSelections() != null){
			facetSelections = new ArrayList<String>();
			facetSelectionConditions = new ArrayList<FacetSelectionCondition>();
			for (FacetSelection selection: searchRequest.getFacetSelections()){
				FacetRequest facetRequest = getFacetRequestByName(selection.getName());
				if (facetRequest != null && facetRequest instanceof CustomFacetRequest){
					processCustomFacetSelections(selection, facetRequest);
				}else{
					processFieldFacetSelections(selection);
				}
			}
			
			checkAndCreateXPathFilterFromFacetSelections();
		}
	}

	private void processFieldFacetSelections(FacetSelection selection) {
		String prefix = selection.getName()+"==";
		int index = 0;
		for (String selectionValue: selection.getSelections()){
			String operator = (index++>0) ? "or" : "and"; 
			if (selectionValue.startsWith(prefix)) {
				facetSelections.add(selectionValue);
				facetSelectionConditions.add(new FacetSelectionCondition(selectionValue, operator));
			}else {
				facetSelections.add(selection.getName()+"=="+selectionValue);
				facetSelectionConditions.add(new FacetSelectionCondition(selection.getName()+"=="+selectionValue, operator));
			}
		}
	}

	private void processCustomFacetSelections(FacetSelection selection, FacetRequest facetRequest) {
		FacetMapper mapper = ((CustomFacetRequest)facetRequest).getFacetMapper();
		Collection<Object> generatedSelections = mapper.generateSelection(selection.getSelections());
		for (Object generatedSelection: generatedSelections) {
			if (generatedSelection instanceof String) {
				facetSelections.add((String)generatedSelection);
				facetSelectionConditions.add(new FacetSelectionCondition((String)generatedSelection));
			}else if (generatedSelection instanceof Filter){
				searchRequest.addFilter((Filter)generatedSelection);
			}
		}
	}
	
	
	private void checkAndCreateXPathFilterFromFacetSelections() {
		String QOSmart = "";
		try{
			if (searchRequest.isUseFacetSelectionAsCriteria() && facetSelectionConditions != null && facetSelectionConditions.size() > 0) {
				QOSmart += "(";
				for (FacetSelectionCondition selectionConditions: facetSelectionConditions) {
					String selection = selectionConditions.getFacetSelection();
					if (selection.contains("AVAILABILITY_DATE_WITH_OFFSET")){
						// Today, 7 days, 14, 21, 30, 60, 90, &gt; 90, No Availability Date
						if (selection.contains("No Availability Date")){
							selection = "not($AVAILABILITY_DATE_WITH_OFFSET)";
						} else if (selection.contains("&gt")){
							selection = "$" + selection.replaceAll("==&gt; ", ">='") + "'";
						} else {
							selection = "$" + selection.replaceAll("==", "<='") + "'";
						}
					}else if (selection.contains("&gt")){
						selection = "$" + selection.replaceAll("==&gt;", ">='") + "'";
					} else if (selection.contains("&lt")) {
						selection = "$" + selection.replaceAll("==&lt;", "<='") + "'";
					} else {
						selection = "$" + selection.replaceAll("==", "='") + "'";
					}
					if (!selection.contains("not($AVAILABILITY_DATE_WITH_OFFSET)") && "or".equals(selectionConditions.getOperator())){
						QOSmart = QOSmart.substring(0, QOSmart.length() - 1); // removing the )
						QOSmart += " or " + selection ;
						QOSmart += ")";
					} else {
						if (QOSmart.length() > 2){
							QOSmart += " and (";
						}
						QOSmart += selection + ")"; // se for o primeiro, nao tem o and
					}
				}
			}
		} catch (Exception ex){
			System.err.println("ERROR at appendQueryObjectSmart ---> " + ex.getMessage());
		}
		if (QOSmart.length() > 2){
			searchRequest.addFilter(WexRequestBuilders.xpathFilter(QOSmart));
			facetSelections = null;
		}
	}
	
	@Override
	public List<String> getRequestedFields() {
		return searchRequest.getFields();
	}

	@Override
	public List<String> getFieldsToBold(){
		if (searchRequest.getFieldsToBold() != null) {
			return searchRequest.getFieldsToBold();
		}else {
			return searchRequest.getFields();
		}	
	}
	
	@Override
	public List<BinningSet> getRequestedFacets() {
		List<FacetRequest> facetRequestList = searchRequest.getFacets();
		List<BinningSet> binningSetRequestList = null;
		if (facetRequestList != null){
			binningSetRequestList = new ArrayList<BinningSet>(facetRequestList.size());
			for (FacetRequest facetRequest: facetRequestList){
				if (facetRequest instanceof CustomFacetRequest){
					FacetMapper mapper = ((CustomFacetRequest)facetRequest).getFacetMapper();
					binningSetRequestList.addAll(mapper.toNative());
				}else{
					BinningSet binningSet = buildBinningSetFromFacetRequest(facetRequest);
					binningSetRequestList.add(binningSet);
				}
			}
		}
		return binningSetRequestList;
	}

	private BinningSet buildBinningSetFromFacetRequest(FacetRequest facetRequest) {
		BinningSet binningSet = new BinningSet();
		binningSet.setId(facetRequest.getFacetName());
		binningSet.setLogic(facetRequest.getLogic());
		if (facetRequest.getSelectXPath() != null){
			binningSet.setSelectXPath(facetRequest.getSelectXPath());
		}else{
			binningSet.setSelectXPath("$"+facetRequest.getFacetName());
		}
		binningSet.setMaxBins(facetRequest.getMaximumNumberOfValues());
		return binningSet;
	}

	@Override
	public String getTextSearch() {
		// Juliano: em uma evntualidade de tratar NLQ + Keywords, este é o ponto
//		if (keywordFilters != null && keywordFilters.size() > 0){
//			if (textSearch.length() > 0){
//				textSearch = "(" + textSearch + ") and " + buildKeywordsSearchString(keywordFilters);
//			} else {
//				textSearch = buildKeywordsSearchString(keywordFilters);
//			}
//		}
		String textSearch = searchRequest.getQuery();
		List<KeywordFilter> keywordFilters = searchRequest.getKeywordFilters();
		if (keywordFilters != null && keywordFilters.size() > 0){
			textSearch = buildKeywordsSearchString(keywordFilters);
		}
		return textSearch;
	}

	private static String buildKeywordsSearchString(List<KeywordFilter> keywordFilters) {
		String textSearch;
		StringBuilder searchString = new StringBuilder();
		StringBuilder filtersWithOrLogic = new StringBuilder();
		
		for (KeywordFilter filter: keywordFilters){
			if (filter.getFilterLogic() == KeywordFilterLogic.MUST_HAVE){
				if (searchString.length() > 0){
					searchString.append(" and ");
				}
				searchString.append(filter.getKeyword());
			}else{
				if (filtersWithOrLogic.length() > 0){
					filtersWithOrLogic.append(" or ");
				}
				filtersWithOrLogic.append(filter.getKeyword());
			}
		}
		
		if (filtersWithOrLogic.length() > 0){
			if (searchString.length() > 0){
				searchString.insert(0, "(").append(")");
				String filtersWithAndLogic = searchString.toString();
				searchString.append(" or (").append(filtersWithAndLogic).append(" and (").append(filtersWithOrLogic).append("))");
			}else{
				searchString = filtersWithOrLogic;
			}
		}
		textSearch = searchString.toString();
		return textSearch;
	}

	@Override
	public int getStartAt() {
		return searchRequest.getOffSet();
	}

	@Override
	public int getMax() {
		return searchRequest.getNumberOfRequestedRecords();
	}

	@Override
	public boolean getProfile() {
		return searchRequest.isProfile();
	}

	@Override
	public boolean getNlq() {
		return searchRequest.isNlq();
	}

	@Override
	public boolean getSmartConditition() {
		return searchRequest.isSmartCondition();
	}

	@Override
	public String getSmartConditionOperator() {
		return searchRequest.getSmartConditionOperator();
	}

	@Override
	public String getOntolectionName() {
		if (searchRequest.isOntolection()){
			return configuration.getOntolectionNameByCollection(searchRequest.getCollection());
		}
		return null;
	}
	
	public String getQueryObject(){
		String queryObject = null;
		if (searchRequest.getFilters() != null && searchRequest.getFilters().size() > 0) {
			QueryObjectVisitor visitor = new QueryObjectVisitor(WexRestfulFactory.getInstance(configuration.getEnvironmentId()));
			for (Filter filter: searchRequest.getFilters()) {
				filter.accept(visitor);
			}
			queryObject = visitor.generateQueryObjectXML();
		}
		return queryObject;
	}

	@Override
	public String getAuthorizationRights() {
		String authorizationRights = null;
		if (searchRequest.getAclTokens() != null && searchRequest.getAclTokens().size() > 0){
			Character separator = Character.valueOf('\n');
			StringBuilder rights = new StringBuilder();
			for (String acl: searchRequest.getAclTokens()){
				if (rights.length() > 0){
					rights.append(separator.charValue());	
				}
				rights.append(acl);
			}
			authorizationRights = rights.toString();
		}
		return authorizationRights;
	}

	@Override
	public List<String> getFacetSelections() {
		return facetSelections;
	}


	private FacetRequest getFacetRequestByName(String facetName){
		if (searchRequest.getFacets() != null) {
			for (FacetRequest facetRequest: searchRequest.getFacets()){
				if (facetName.equals(facetRequest.getFacetName())){
					return facetRequest;
				}
			}
		}
		return null;
	}
	
	@Override
	public List<SortBy> getSortList() {
		return searchRequest.getSortList();
	}

	@Override
	public boolean getStemming() {
		return searchRequest.isStemming();
	}

	@Override
	public boolean getSpelling() {
		return searchRequest.isSpelling();
	}

	@Override
	public boolean getJapaneseSearch() {
		return searchRequest.isJapaneseSearch();
	}

	@Override
	public boolean getBold() {
		return searchRequest.isBold();
	}

	@Override
	public String getBoldRootClassName() {
		return searchRequest.getBoldRootClassName();
	}

	@Override
	public boolean getBrowse() {
		return searchRequest.isBrowse();
	}
}
