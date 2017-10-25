package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
/**
 * Represents the Watson Explorer response
 * @author julianom
 *
 */
public class Response {
/**
 * The total number of documents returned
 */
	private int totalNumberOfDocuments;
	private List<String> requestedFields;
	private List<Document> documents;
	private Map<String, Facet> facets;
	private Set<SearchKeywordResponse> searchKeywordResponseList;
	private List<KeywordFilter> keywordFiltersMustHave = new ArrayList<KeywordFilter>();
	private List<KeywordFilter> keywordFiltersNiceToHave = new ArrayList<KeywordFilter>();
	
	private Collection<String> exceptionMessages;
	private String validationMessage;
	private String smartConditions;
	private List<FacetSelection> smartConditionsList;

	private List<String> restfulRequests;
	
	private String emPartition = null;
	private List<emFilter> emFilters = new ArrayList<emFilter>();
	
	// This will be removed - I have added just for POC purpose - in order to print Open Seat/Practitioner details in the WEX-WS UI
	private OpenSeat openSeat = new OpenSeat();
	private Practitioner practitioner = new Practitioner();

	private boolean hasError;

	private long queryTime;
	
	private Request originalRequest;
	
	public Response(long queryTime, List<String> restfulRequests) {
		this.facets = new HashMap<String, Facet>();
		this.documents = new ArrayList<Document>();
		this.requestedFields = new ArrayList<String>();
		this.queryTime = queryTime;
		this.restfulRequests = restfulRequests;
		this.smartConditionsList = new ArrayList<FacetSelection>();
	}

	public int getTotalNumberOfDocuments() {
		return totalNumberOfDocuments;
	}

	public void setTotalNumberOfDocuments(int totalNumberOfDocuments) {
		this.totalNumberOfDocuments = totalNumberOfDocuments;
	}

	public List<Document> getDocuments() {
		Collections.sort(documents);
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public Facet getFacetByName(String name) {
		return this.facets.get(name);
	}

	public void putFacet(Facet facet) {
		this.facets.put(facet.getName(), facet);

	}
	
	public Request getOriginalRequest() {
		return originalRequest;
	}
	
	public void setOriginalRequest(Request originalRequest) {
		this.originalRequest = originalRequest;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{\n");

		sb.append("totalDocuments:").append(this.totalNumberOfDocuments).append(",\n");

		sb.append("facets: [\n");

		sb.append(facetsToString());

		sb.append("],\n");

		sb.append("documents: [\n");

		sb.append(documentToString());

		sb.append("]\n");

		sb.append("search keywords: [\n");
		if (searchKeywordResponseList != null){
			sb.append(searchKeywordResponseList.toString());
			sb.append("]\n");
		}
		sb.append("}\n");
		return sb.toString();
	}

	private String documentToString() {
		StringBuilder sb = new StringBuilder();

		int count = 0;
		for (Document doc : this.documents) {
			if (count > 0)
				sb.append(",\n");
			sb.append(doc.toString());
			count++;
		}

		return sb.toString();
	}

	private String facetsToString() {
		StringBuilder sb = new StringBuilder();

		int count = 0;
		for (Facet facet : this.facets.values()) {
			if (count > 0)
				sb.append(",\n");
			sb.append("{");

			sb.append("name:").append(facet.getName()).append(",\n");
			sb.append("values: [");

			int count2 = 0;
			for (FacetValue fv : facet.getValues()) {
				if (count2 > 0)
					sb.append(",\n");
				sb.append(fv.toString());
				count2++;
			}

			sb.append("]\n");

			sb.append("}");

			count++;
		}

		sb.append("\n");

		return sb.toString();
	}

	public void addDocument(Document doc) {
		this.documents.add(doc);

	}

	public void addAllDocuments(List<Document> docs) {
		this.documents.addAll(docs);

	}

	/**
	 * Adding keywords. Even using a HASH, values are duplicating because JP keywords sometimes
	 * comes with bad encoding, so, I created the for/if above
	 * @param keywordResponse
	 */
	public void addSearchKeywordResponse(SearchKeywordResponse keywordResponse){
		if (searchKeywordResponseList == null){
			searchKeywordResponseList = new HashSet<SearchKeywordResponse>();
		}
		boolean found = false;
		for (SearchKeywordResponse skRL: searchKeywordResponseList){
			if (skRL.getValue().equals(keywordResponse.getValue())){
				found = true;
			}
		}
		if (!found){
			searchKeywordResponseList.add(keywordResponse);
		}
		
	}
	
	public List<String> getRequestedFields() {
		return requestedFields;
	}

	public void setRequestedFields(List<String> requestedFields) {
		this.requestedFields = requestedFields;
	}

	public Map<String, Facet> getFacets() {
		return facets;
	}
	
	public String getFacetsString() {
		String facetsString = "";
		if (this.getFacets().entrySet().size() > 0) {
			for (Entry<String, Facet> entry : this.getFacets().entrySet()) {
				String facetFieldName = entry.getKey();
				Facet facet = entry.getValue();
				facetsString += "<b>" + facetFieldName + "</b>|";
				for (FacetValue fv : facet.getValues()) {
					facetsString +=  fv.getLabel() + "-" + fv.getCount() + "|";
				}
			}
		}
		return facetsString;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public boolean isHasError() {
		return hasError;
	}

	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	public Collection<String> getExceptionMessages() {
		return exceptionMessages;
	}

	public void setExceptionMessages(Collection<String> exceptionMessages) {
		this.exceptionMessages = exceptionMessages;
	}

	public void setFacets(Map<String, Facet> facets) {
		this.facets = facets;
	}

	public long getQueryTime() {
		return queryTime;
	}

	public List<String> getRestfulRequests() {
		return restfulRequests;
	}

	public void sortBy() {
		if (documents != null && documents.size() > 0){
			Document firstDocument = documents.get(0);
			if (firstDocument.getSortKeys() != null && firstDocument.getSortKeys().size() > 0){
				
				Collections.sort(this.documents, new Comparator<Document>() {
					@Override
					public int compare(Document doc1, Document doc2) {
						if (doc1 == null)
							return -1;
						if (doc2 == null)
							return 1;
						
						List<DocumentSortKey> sortKeys1 = doc1.getSortKeys();
						List<DocumentSortKey> sortKeys2 = doc2.getSortKeys();
						int compare = 0;
						if (sortKeys1 != null && sortKeys2 != null && sortKeys1.size() == sortKeys2.size()){
							for (int i=0; i < sortKeys1.size(); i++){
								DocumentSortKey sortKey1 = sortKeys1.get(i);
								DocumentSortKey sortKey2 = sortKeys2.get(i);
								String sortKeyValue1 = sortKey1.getValue();
								String sortKeyValue2 = sortKey2.getValue();
								int orderFactor = (sortKey1.getDirection().equals("descending") ? -1 : 1);
								
								if (sortKeyValue1 == null || sortKeyValue1.length() == 0)
									compare =  -1;
								if (sortKeyValue2 == null || sortKeyValue2.length() == 0)
									compare =  1;
								
								if (compare == 0){
									if (sortKey1.getType().equals("number")){
										Double value1 = Double.parseDouble(sortKeyValue1);
										Double value2 = Double.parseDouble(sortKeyValue2);
										compare = value1.compareTo(value2);
									}else{
										compare = sortKeyValue1.compareTo(sortKeyValue2);
									}
								}
								compare = compare * orderFactor;
								if (compare != 0){
									break;
								}
							}
						}
						return compare;
					}

				});
				
			}
		}
			
		

	}

	public String getSmartConditions() {
		return smartConditions;
	}

	public void setSmartConditions(String smartConditions) {
		this.smartConditions = smartConditions;
	}

	public Set<SearchKeywordResponse> getSearchKeywordResponseList() {
		return searchKeywordResponseList;
	}
	
	/**
	 * Retrieve all the keywords found the NLQ query inside a {@link List} of {@link String}
	 * 
	 * @return a list containing all the keywords found in the NLQ query
	 */
	public List<String> getKeywords() {
		List<String> keysFound = null;
		if (searchKeywordResponseList != null) {
			keysFound = new ArrayList<String>(searchKeywordResponseList.size());
			for (SearchKeywordResponse keyword : searchKeywordResponseList) {
				keysFound.add(keyword.getValue());
			}
		}
		return keysFound;
	}
	
	/**
	 * Retrieve all the keywords and synonyms found the NLQ query inside a {@link List} of {@link String}
	 *  
	 * @return a list containing all the keywords and synonyms found in the NLQ query
	 */
	public List<String> getKeywordsAndSynonyms() {
		List<String> keysFound = new ArrayList<String>();
		if (searchKeywordResponseList != null) {
			for (SearchKeywordResponse keyword : searchKeywordResponseList) {
				keysFound.add(keyword.getValue());
				if (keyword.getSynonymList() != null) {
					// synonyms
					for (int i = 0; i < keyword.getSynonymList().size(); i++) {
						keysFound.add(keyword.getSynonymList().get(i).getValue());
					}
				}
			}
		}
		return keysFound;
	}
	
	public String getKeywordsString() {
		String keysFound = "";
		if (searchKeywordResponseList != null) {
			for (SearchKeywordResponse keyword : searchKeywordResponseList) {
				keysFound += " (<b>" + keyword.getValue() + "</b>";
				if (keyword.getSynonymList() != null) {
					keysFound += " [ ";
					for (int i = 0; i < keyword.getSynonymList().size(); i++) {
						keysFound += keyword.getSynonymList().get(i).getValue() + " | ";
					}
					keysFound += "] ";
				}
				keysFound += ") ";
			}
		}
		return keysFound;
	}

	public List<FacetSelection> getSmartConditionsList() {
		return smartConditionsList;
	}
	
	public String printSmartConditionsList() {
		String smartConditionsListString = "";
		for (FacetSelection fs : smartConditionsList) {
			smartConditionsListString += "[FACET: " + fs.getName();
			for (String str : fs.getSelections()) {
				smartConditionsListString += "(Value: " + str + ")";
			}
			smartConditionsListString += "] ";
		}
		
		return smartConditionsListString;
	}

	public void setSmartConditionsList(List<FacetSelection> smartConditionsList) {
		this.smartConditionsList = smartConditionsList;
	}
	
	// IF facet already exist, it append (Yes Alan.. I know that I can use better structures. Time constraint here :( )
	// it also check if the value already exist (Yes Alan, I know.... I can user better structures AGAIN 
	public void addSmartConditionsListItem(FacetSelection smartConditionsItem) {
		for (int i = 0; i < smartConditionsList.size(); i++) {
			if (smartConditionsList.get(i).getName().equals(smartConditionsItem.getName())){
				for (String value: smartConditionsList.get(i).getSelections()){
					if (smartConditionsItem.getSelections().get(0).equalsIgnoreCase(value)){
						return;
					}
				}
				smartConditionsList.get(i).addSelection(smartConditionsItem.getSelections().get(0));
				return;
			}
		}
		this.smartConditionsList.add(smartConditionsItem);
	}
	
	/*
	 * The list of MUST HAVE keywords used
	 */
	public List<KeywordFilter> getKeywordFiltersMustHave() {
		return keywordFiltersMustHave;
	}
	
	public List<String> getKeywordFiltersMustHaveString() {
		List<String> keysRequired = new ArrayList<String>();
		for (KeywordFilter kf: keywordFiltersMustHave){
			keysRequired.add(kf.getKeyword().replace("\"", ""));
		}
		return keysRequired;
	}

	public void addKeywordFiltersMustHave(KeywordFilter keywordFiltersMustHave) {
		if (this.keywordFiltersMustHave == null){
			this.keywordFiltersMustHave = new ArrayList<KeywordFilter>();
		}
		this.keywordFiltersMustHave.add(keywordFiltersMustHave);	
	}

	/*
	 * The list of NICE TO HAVE keywords used
	 */
	public List<KeywordFilter> getKeywordFiltersNiceToHave() {
		return keywordFiltersNiceToHave;
	}
	
	
	public List<String> getKeywordFiltersNiceToHaveString() {
		List<String> keysRequired = new ArrayList<String>();
		for (KeywordFilter kf: keywordFiltersNiceToHave){
			keysRequired.add(kf.getKeyword().replace("\"", ""));
		}
		return keysRequired;
	}

	public void addKeywordFiltersNiceToHave(KeywordFilter keywordFiltersNiceToHave) {
		if (this.keywordFiltersNiceToHave == null){
			this.keywordFiltersNiceToHave = new ArrayList<KeywordFilter>();
		}
		this.keywordFiltersNiceToHave.add(keywordFiltersNiceToHave);
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

	public OpenSeat getOpenSeat() {
		return openSeat;
	}

	public void setOpenSeat(OpenSeat os) {
		this.openSeat = os;
	}

	public Practitioner getPractitioner() {
		return practitioner;
	}

	public void setPractitioner(Practitioner practitioner) {
		this.practitioner = practitioner;
	}

}
