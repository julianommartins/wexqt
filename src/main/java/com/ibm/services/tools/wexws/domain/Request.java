package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the "VO" object that will se sent to the API as parameter
 * 
 * @author julianom
 *
 */
public class Request {
	private String query;
	private List<Filter> filters;
	private List<FacetSelection> facetSelections;
	private List<KeywordFilter> keywordFilters;
	private List<String> fields;
	private List<FacetRequest> facets;
	private String collection;
	private boolean useFacetSelectionAsCriteria = true;
	private boolean profile;
	private boolean nlq;
	private boolean smartCondition;
	private boolean ontolection;
	private boolean stemming;
	private boolean spelling;
	private boolean japaneseSearch;
	private String smartConditionOperator = "and";
	private String emPartition = "undefined";
	private List<emFilter> emFilters = new ArrayList<emFilter>();

	private boolean bold;
	private String boldRootClassName;
	private List<String> fieldsToBold;
	
	private List<String> aclTokens;
	private int numberOfRequestedRecords = 1;
	private int offSet;
	
	private boolean browse;
	
	private List<SortBy> sortList;
	
	private int tooFew = -1;
	private int tooMany = -1;
	
	public Request() {
		super();
	}

	public Request(String collection) {
		super();
		this.collection = collection;
	}

	public String getQuery() {
		return query;
	}

	/**
	 * Its the query, can be NLQ or just keywords. NOT REQUIRED
	 * 
	 * @param query
	 *            Example 1: Im searching for a Java Developer <br>
	 *            Example 2: java websphere spring
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	public List<FacetSelection> getFacetSelections() {
		return facetSelections;
	}

	public void addFacetRequest(FacetRequest facetRequest){
		if (facets == null){
			facets = new ArrayList<FacetRequest>();
		}
		facets.add(facetRequest);
	}
	
	/**
	 * Represent the conditional facets - NOT REQUIRED
	 * 
	 * @param cf
	 */
	public void addFacetSelection(FacetSelection cf) {
		if (facetSelections == null){
			facetSelections = new ArrayList<FacetSelection>();
		}
		facetSelections.add(cf);
	}

	public void addFilter(Filter filter) {
		if (filters == null) {
			filters = new ArrayList<Filter>();
		}
		filters.add(filter);
	}
	
	public String getCollection() {
		return collection;
	}

	/**
	 * Represents the collection that will receive the query. REQUIRED
	 * 
	 * @param collection
	 *            example: PMP_Practitioner
	 */
	public void setCollection(String collection) {
		this.collection = collection;
	}

	public boolean isNlq() {
		return nlq;
	}

	/**
	 * If true, WEX will understand the query as a NLQ query and will interpret with query modifier in order to filter stopwords, find phrases and apply disconjunctfy (AND or OR) If false, WEX will
	 * understand all words like keywords.
	 * 
	 * @param nlq
	 *            true or false
	 */
	public void setNlq(boolean nlq) {
		this.nlq = nlq;
	}

	public boolean isSmartCondition() {
		return smartCondition;
	}

	/**
	 * If true, will make WEX-WS parse the query and find for possible FACET filters If not checked, band and 8 will be used as keywords.
	 * 
	 * @param smartCondition
	 */
	public void setSmartCondition(boolean smartCondition) {
		this.smartCondition = smartCondition;
	}

	public boolean isOntolection() {
		return ontolection;
	}

	/**
	 * If true, will make WEX use ontolection to search for synonyms, substitution words, etc. Configured at our thesaurus file
	 * 
	 * @param ontolection
	 */
	public void setOntolection(boolean ontolection) {
		this.ontolection = ontolection;
	}

	public String getSmartConditionOperator() {
		return smartConditionOperator;
	}

	/**
	 * Set the operator that will be used beetwen Smart Conditions and Facets, example BAND=8 and JOB_ROLE=XYZ
	 * 
	 * @param smartConditionOperator
	 *            and or
	 */
	public void setSmartConditionOperator(String smartConditionOperator) {
		this.smartConditionOperator = smartConditionOperator;
	}

	public List<String> getFields() {
		return fields;
	}

	/**
	 * This is the String representation of the fields that will be returned by the query. REQUIRED
	 * 
	 * @param fields
	 *            example:
	 *            PRACTITIONER_ID,FULL_NAME,RESOURCE_TYPE,EMPLOYEE_TYPE_DESC,JOB_ROLE,TITLE,BAND,SUB_BAND,DEPLOYABILITY,AVAILABILITY_DATE,PRIMARY_JRSS,SECONDARY_JRSS,INDUSTRY,SECONDARY_INDUSTRY,SECTOR
	 *            ,SECONDARY_SECTOR,SERVICE,SERVICE_AREA,SKILL_SET,WORK_LOCATION_IOT,WORK_LOCATION_IMT,WORK_LOCATION_COUNTRY,WORK_LOCATION_STATE,WORK_LOCATION_CITY
	 */
	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public List<FacetRequest> getFacets() {
		return facets;
	}

	/**
	 * This is the String representation of the facets that will be returned by the query. NOT REQUIRED
	 * 
	 * @param facets
	 *            example: RESOURCE_TYPE, WORK_LOCATION_IOT, JOB_ROLE, BAND, SERVICE, SERVICE_AREA
	 */
	public void setFacets(List<FacetRequest> facets) {
		this.facets = facets;
	}

	public List<String> getAclTokens() {
		return aclTokens;
	}

	public void setAclTokens(List<String> aclTokens) {
		this.aclTokens = aclTokens;
	}

	public int getNumberOfRequestedRecords() {
		return numberOfRequestedRecords;
	}

	public void setNumberOfRequestedRecords(int numberOfRequestedRecords) {
		this.numberOfRequestedRecords = numberOfRequestedRecords;
	}

	public int getOffSet() {
		return offSet;
	}

	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}

	public void setFacetSelections(List<FacetSelection> facetSelections) {
		this.facetSelections = facetSelections;
	}

	public boolean isProfile() {
		return profile;
	}

	public void setProfile(boolean profile) {
		this.profile = profile;
	}

	public List<SortBy> getSortList() {
		return sortList;
	}

	public void setSortList(List<SortBy> sortList) {
		this.sortList = sortList;
	}
	
	public void addSort(SortBy sortBy){
		if (this.sortList == null){
			this.sortList = new ArrayList<SortBy>(2);
		}
		this.sortList.add(sortBy);
	}

	public void addKeywordFilter(KeywordFilter keywordFilter){
		if (this.keywordFilters == null){
			this.keywordFilters = new ArrayList<KeywordFilter>();
		}
		this.keywordFilters.add(keywordFilter);
	}
	
	public List<KeywordFilter> getKeywordFilters() {
		return keywordFilters;
	}

	public void setKeywordFilters(List<KeywordFilter> keywordFilters) {
		this.keywordFilters = keywordFilters;
	}
	
	public String getKeywordFiltersString(){
		String kfString = "";
		if (null != keywordFilters){
			for (KeywordFilter kf : keywordFilters) {
				kfString += kf.getKeyword() + " - "+ kf.getFilterLogic().toString() + " | ";
			}
		}
		return kfString;
	}

	@Override
	public String toString() {
		try{
			String facetsList = "|";
			try {
				for (FacetRequest fac : facets) {
					facetsList += fac.getFacetName() + ":" + fac.getLogic() + ":" + fac.getMaximumNumberOfValues() + ":" + fac.getSelectXPath() + " | ";
				}
			} catch (Exception ex) {
				facetsList = "";
			}

			String facetsSelectionList = "|";
			try {
				for (FacetSelection facS : facetSelections) {
					facetsSelectionList += facS.getName() + ":";
					for (String sel : facS.getSelections()) {
						facetsSelectionList += sel + ",";
					}
					facetsSelectionList += "| ";
				}
			} catch (Exception ex) {
				facetsSelectionList = "";
			}

			String kfList = "|";
			try {
				for (KeywordFilter kf : keywordFilters) {
					kfList += kf.getKeyword() + ":" + kf.getFilterLogic() + ",";
				}
				kfList += "| ";
			} catch (Exception ex) {
				kfList = "";
			}

			return "Request [query=" + query +
					", fields=" + fields +
					", facetSelections=" + facetsSelectionList + 
					", facets=" + facetsList +
					", filters=" + filters +
					", keywordFilters=" + kfList  +  
					", collection=" + collection + 
					", profile=" + profile + 
					", nlq=" + nlq + 
					", smartCondition=" + smartCondition + 
					", ontolection=" + ontolection + 
					", stemming=" + stemming + 
					", smartConditionOperator=" + smartConditionOperator + 
					", aclTokens=" + aclTokens + 
					", numberOfRequestedRecords=" + numberOfRequestedRecords + 
					", offSet=" + offSet + 
					", tooFew=" + tooFew + 
					", tooMany=" + tooMany + 
					", sortList=" + sortList + "]";
		} catch (Exception ex){
			return "Err: Request [query=" + query + ", facetSelections=" + facetSelections + ", keywordFilters=" + keywordFilters + ", fields=" + fields + ", facets=" + facets + ", collection=" + collection + ", profile=" + profile + ", nlq=" + nlq + ", smartCondition=" + smartCondition + ", ontolection=" + ontolection + ", smartConditionOperator=" + smartConditionOperator + ", aclTokens=" + aclTokens + ", numberOfRequestedRecords=" + numberOfRequestedRecords + ", offSet=" + offSet + ", sortList=" + sortList + "]";
		}
	}

	public boolean isStemming() {
		return stemming;
	}

	/**
	 * If true, will make WEX stemming
	 * 
	 * @param stemming
	 */
	public void setStemming(boolean stemming) {
		this.stemming = stemming;
	}
	
	
	public boolean isSpelling() {
		return spelling;
	}

	/**
	 * If true, will make WEX spelling 
	 * 
	 * @param spelling
	 */
	public void setSpelling(boolean spelling) {
		this.spelling = spelling;
	}
	
	public boolean isJapaneseSearch() {
		return japaneseSearch;
	}

	/**
	 * If true, will make JP search
	 * 
	 * @param japaneseSearch
	 */
	public void setJapaneseSearch(boolean japaneseSearch) {
		this.japaneseSearch = japaneseSearch;
	}

	public int getTooFew() {
		return tooFew;
	}

	public void setTooFew(int tooFew) {
		this.tooFew = tooFew;
	}

	public int getTooMany() {
		return tooMany;
	}

	public void setTooMany(int tooMany) {
		this.tooMany = tooMany;
	}

	public boolean isUseFacetSelectionAsCriteria() {
		return useFacetSelectionAsCriteria;
	}

	public void setUseFacetSelectionAsCriteria(boolean useFacetSelectionAsCriteria) {
		this.useFacetSelectionAsCriteria = useFacetSelectionAsCriteria;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public String getBoldRootClassName() {
		return boldRootClassName;
	}

	public void setBoldRootClassName(String boldRootClassName) {
		this.boldRootClassName = boldRootClassName;
	}

	public List<String> getFieldsToBold() {
		return fieldsToBold;
	}

	public void setFieldsToBold(List<String> fieldsToBold) {
		this.fieldsToBold = fieldsToBold;
	}

	public boolean isBrowse() {
		return browse;
	}

	public void setBrowse(boolean browse) {
		this.browse = browse;
	}

	public String getEmPartition() {
		return emPartition;
	}

	public void setEmPartition(String emPartition) {
		this.emPartition = emPartition;
	}

	public List<emFilter> getEmFilters() {
		return emFilters;
	}

	public void setEmFilters(List<emFilter> emFilters) {
		this.emFilters = emFilters;
	}
	
	public void addEmFilters(emFilter emfilter) {
		if (this.emFilters == null){
			this.emFilters = new ArrayList<emFilter>();
		}
		this.emFilters.add(emfilter);
	}
}
