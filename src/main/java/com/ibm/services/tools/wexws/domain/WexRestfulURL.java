package com.ibm.services.tools.wexws.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ibm.services.tools.wexws.WexConfiguration;
import com.ibm.services.tools.wexws.WexWsConstants;
import com.ibm.services.tools.wexws.bo.SmartConditionBuilder;
import com.ibm.services.tools.wexws.bo.WexRestURLTranslator;
import com.ibm.services.tools.wexws.factory.WexSmartConditionDataProviderFactory;
import com.ibm.services.tools.wexws.utils.XMLUtil;

public class WexRestfulURL extends WexBaseURL {

	static final Logger logger = Logger.getLogger(WexRestfulURL.class);
	
	private String arena;
	private Set<String> collections;
	private List<String> fields;
	private List<String> facets;
	private List<BinningSet> binningSetsRequest;
	private List<String> facetSelections;
	private String textSearch;
	private String criteria;
	private String queryObjectSearchTerms;
	private int startAt;
	private int max;
	private int numberOfShards;
	private List<SortBy> sortByList;
	private String authorizationRights;
	private boolean profile;
	private boolean stemming;
	private boolean spelling;
	private boolean japaneseSearch;
	private boolean nlq;
	private boolean bold;
	private String boldRootClassName;
	private List<String> fieldsToBold;	
	private String ontolectionName;
	private boolean browse;
	
	public WexRestfulURL(String server, WexConfiguration configuration, List<String> fields, List<String> facets, String arena, Set<String> collections, String textSearch, String criteria, String queryObjectSearchTerms, int startAt, int max, int numberOfShards, SortBy sortBy, boolean profile, boolean nlq, boolean stemming, boolean spelling, boolean japaneseSearch) {
		super(server, configuration);
		this.fields = fields;
		this.facets = facets;
		this.arena = arena;
		this.collections = collections;
		this.textSearch = textSearch;
		this.criteria = criteria;
		this.queryObjectSearchTerms = queryObjectSearchTerms;
		this.startAt = startAt;
		this.max = max;
		this.numberOfShards = numberOfShards;
		if (sortBy != null){
			this.sortByList = Arrays.asList(sortBy);
		}
		this.profile = profile;
		this.nlq = nlq;
		this.stemming = stemming;
		this.spelling = spelling;
		this.japaneseSearch = japaneseSearch;
		this.setNlq(nlq);
	}

	public WexRestfulURL(String server, WexConfiguration configuration, String arena, Set<String> collections, 
			int numberOfShards, WexRestURLTranslator restURLTranslator) {
		super(server, configuration);
		this.arena = arena;
		this.collections = collections;
		this.numberOfShards = numberOfShards;
		
		this.fields = restURLTranslator.getRequestedFields();
		this.binningSetsRequest = restURLTranslator.getRequestedFacets();
		this.facetSelections = restURLTranslator.getFacetSelections();
		this.textSearch = restURLTranslator.getTextSearch();
		this.startAt = restURLTranslator.getStartAt();
		this.max = restURLTranslator.getMax();
		this.sortByList = restURLTranslator.getSortList();
		this.authorizationRights = restURLTranslator.getAuthorizationRights();
		this.queryObjectSearchTerms = restURLTranslator.getQueryObject();
		this.profile = restURLTranslator.getProfile();
		this.nlq = restURLTranslator.getNlq();
		this.stemming = restURLTranslator.getStemming();
		this.spelling = restURLTranslator.getSpelling();
		this.japaneseSearch = restURLTranslator.getJapaneseSearch();
		this.ontolectionName = restURLTranslator.getOntolectionName();
		this.bold = restURLTranslator.getBold();
		this.boldRootClassName = restURLTranslator.getBoldRootClassName();
		this.fieldsToBold = restURLTranslator.getFieldsToBold();
		this.browse = restURLTranslator.getBrowse();
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public List<String> getFacets() {
		return facets;
	}

	public void setFacets(List<String> facets) {
		this.facets = facets;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Return the rest query
	 * 
	 * @param smartCondition
	 *            if true, will map possible values to corresponding facets based in an internal dictionary
	 * @param ontolection
	 * 			  if true, will append ontolection xml to query
	 * @param smartConditionOperator
	 * @return Position 0 - the query , position 1 - the smart condition found
	 */
	public String[] getQuery(boolean smartCondition, String ontolectionName, String smartConditionOperator, boolean bold) {
		this.ontolectionName = ontolectionName;
		this.bold = bold;
		String smartConditions = "";
		smartConditionOperator = " " + smartConditionOperator + " ";
		String[] arrayReturn = {"",""}; 
				
		StringBuilder sb = new StringBuilder();
		appendBasicQueryParameters(sb);

		if (this.textSearch != null && this.textSearch.trim().length() != 0) {
			// Build Smart Condition
			if (smartCondition) {
				SmartConditionBuilder smartConditionBuilder = new SmartConditionBuilder(WexSmartConditionDataProviderFactory.getWexDataProviderInstance(configuration.getEnvironmentId()));
				smartConditions = smartConditionBuilder.getSmartConditionsXPath(textSearch, smartConditionOperator,null);
			}
			// removing smart conditions from the search query text
		    Pattern pattern = Pattern.compile("'(.*?)'");
		    Matcher matcher = pattern.matcher(smartConditions);
		    String cleanTextSearch = this.textSearch;
		    while (matcher.find()) {
		    	//System.out.println(matcher.group(1).toLowerCase());
		        cleanTextSearch = cleanTextSearch.replaceAll(matcher.group(1).toLowerCase(), "");
		    }
			
		    if (queryObjectSearchTerms == null || queryObjectSearchTerms.length() == 0){
		    	//System.out.println(cleanTextSearch);
		    	if (cleanTextSearch.contains("'")){
		    		//System.out.println("text search ---> " + encodeValue(cleanTextSearch.substring(1, cleanTextSearch.length() - 1)));
					sb.append("&query=").append(encodeValue(cleanTextSearch.substring(1, cleanTextSearch.length() - 1)));
				} else{
					//System.out.println("text search ---> " + encodeValue(cleanTextSearch));
					sb.append("&query=").append(encodeValue(cleanTextSearch));
				}
		    	
				
			}
		}
		if (this.criteria != null && this.criteria.trim().length() != 0) {
			sb.append("&query-condition-xpath=").append(this.criteria);
			if (smartConditions != null && smartConditions.trim().length() != 0) {
				sb.append(" and " + smartConditions);
			}
		} else if (smartConditions != null && smartConditions.trim().length() != 0) {
			sb.append("&query-condition-xpath=").append(smartConditions);
		}

		if (this.facets.size() > 0) {
			sb.append("&binning-configuration=");
			for (String f : this.facets) {
				sb.append(getBinningSet(f));
			}
		}
		appendQueryObject(sb);
		appendPaginationAndSort(sb);
		appendNLQAndOntolectionConfiguration(sb);

		arrayReturn[0] = sb.toString().replace("\"", "%22").replace(" ", "+").replace("<", "%3C").replace(">", "%3E").replace("$", "%24").replace("(", "%28").replace(")", "%29");
		arrayReturn[1] = smartConditions;
		return arrayReturn;
	}

	/**
	 * Write the query
	 * @return
	 */
	public String getQuery() {
		StringBuilder queryURL = new StringBuilder();
						
		appendBasicQueryParameters(queryURL);

		if (this.textSearch != null && this.textSearch.trim().length() != 0) {
			queryURL.append("&query=").append(encodeValue(this.textSearch));
		}
		appendQueryObject(queryURL);
		appendBinningSetRequest(queryURL);
		appendPaginationAndSort(queryURL);
		appendAuthorizationRights(queryURL);
		appendNLQAndOntolectionConfiguration(queryURL);
		return queryURL.toString();
	}

	private void appendBinningSetRequest(StringBuilder queryURL) {
		if (this.binningSetsRequest != null && this.binningSetsRequest.size() > 0) {
			queryURL.append("&binning-configuration=");
			for (BinningSet binningSet: this.binningSetsRequest) {
				queryURL.append(encodeValue(binningSet.toBinningSetXML()));
			}				
		}
	}

	private void appendQueryObject(StringBuilder queryURL) {
		queryURL.append("&query-object=");
		if (this.facetSelections != null && this.facetSelections.size() > 0) {
			for (String selection: this.facetSelections) {
				selection = XMLUtil.escapeXML(selection);
				String selectionXML = String.format(WexWsConstants.BINNING_SELECTION_XML_TEMPLATE, selection);
				queryURL.append(encodeValue(selectionXML));
			}		
		}
		if (this.queryObjectSearchTerms != null){
			queryURL.append(encodeValue(this.queryObjectSearchTerms));
		}
	}
	
	private void appendBasicQueryParameters(StringBuilder sb) {
		appendBasicServerParameters(sb);
		
		sb.append("&v.indent=true&v.function=query-search&fetch-timeout="+WexWsConstants.TIMEOUT_IN_MILIS).append("&output-display-mode=limited");
		sb.append("&arena=").append(arena);
		
		sb.append("&syntax-operators=");
		sb.append(encodeValue(WexWsConstants.SYNTAX_OPERATORS));
		
		sb.append("&sources=");
		for (String c : this.collections) {
			sb.append(encodeValue(c)).append(encodeValue(" "));
		}
		
		if (this.fields != null && this.fields.size() > 0){
			sb.append("&output-contents-mode=list");
			sb.append("&output-contents=");
			appendFieldList(sb);
			if (bold){
				sb.append("&output-bold-contents=");
				if (this.fieldsToBold != null) {
					for (String f : this.fieldsToBold) {
						sb.append(encodeValue(f)).append(encodeValue(" "));
					}
				}
												
				if (boldRootClassName != null && boldRootClassName.length() > 0) {
					sb.append("&output-bold-class-root=").append(encodeValue(boldRootClassName));	
				}
			}
		}
				
		if (profile) {
			sb.append("&debug=true&profile=true");
		}
	}

	private void appendFieldList(StringBuilder sb) {
		for (String f : this.fields) {
			sb.append(encodeValue(f)).append(encodeValue(" "));
		}
	}

	private void appendNLQAndOntolectionConfiguration(StringBuilder sb) {
		// se for JP, usar outra ontolection
		if (japaneseSearch) {
			ontolectionName = WexWsConstants.ONTOLECTION_NAME_JP;
//			System.out.println("JP");
//		} else {
//			System.out.println("NO JP");
		}
		
		if (nlq && ontolectionName != null && ontolectionName.length() > 0) {
			//System.out.println("NLQ AND Ontolection");
			sb.append("&query-modification-macros=").append(encodeValue("enhance-query-with-querymodifier query-modification-expansion"))
				.append("&extra-xml="+encodeValue(buildOntolectionConfigurationXML()));
		} else if (nlq) {
		//	System.out.println("NLQ and NOT Ontolection");
			sb.append("&query-modification-macros=enhance-query-with-querymodifier");
		} else if (ontolectionName != null && ontolectionName.length() > 0) {
		//	System.out.println("Ontolection and NOT NLQ");
			sb.append("&query-modification-macros=query-modification-expansion&extra-xml="+encodeValue(buildOntolectionConfigurationXML()));
		} 
//		else {
//			System.out.println("NOT Ontolection and NOT NLQ");
//		}
	}

	private String buildOntolectionConfigurationXML(){
		// tratar o japanese
		StringBuilder sourceNames = new StringBuilder();
		for (String c : this.collections) {
			sourceNames.append(c).append(" ");
		}
//		if (japaneseSearch){
//			System.out.println("Eh JP");	
//		} else {
//			System.out.println("NAO Eh JP");	
//		}
		
		if (stemming && spelling) {
			return String.format(WexWsConstants.ONTOLECTION_CONFIGURATION_STEMMING_SPEELING, ontolectionName, sourceNames);
		} else if (stemming && !spelling) {
			return String.format(WexWsConstants.ONTOLECTION_CONFIGURATION_STEMMING, ontolectionName, sourceNames);
		} else if (!stemming && spelling) {
			return String.format(WexWsConstants.ONTOLECTION_CONFIGURATION_SPELLING, ontolectionName, sourceNames);
		}
		return String.format(WexWsConstants.ONTOLECTION_CONFIGURATION, ontolectionName, sourceNames);
		
	}
	
	private void appendPaginationAndSort(StringBuilder sb) {
		if (this.browse) {
			int numberOfRecordsForBrowsing = this.max + this.startAt;
			sb.append("&num-per-source=").append(numberOfRecordsForBrowsing).append("&num=").append(numberOfRecordsForBrowsing);
			sb.append("&browse=true").append("&browse-start=").append(this.startAt);
			sb.append("&browse-num=").append(this.max);
		}else {
			sb.append("&num-per-source=").append(this.max).append("&num=").append(this.max);
			sb.append("&start=").append(this.startAt);
		}
		
		if (this.sortByList != null && this.sortByList.size() > 0) {
			sb.append("&output-sort-keys=true&sort-xpaths=");
			for (SortBy sortBy: this.sortByList){
				String order = "ascending";
				if (sortBy.getOrder() != null) {
					order = sortBy.getOrder().toString().toLowerCase();
				}
				sb.append(encodeValue("<sort xpath=\"")).append(encodeValue(sortBy.getFieldXpath()))
					.append(encodeValue("\" order=\"")).append(encodeValue(order)).append(encodeValue("\" />"));
			}
			
		}
	}

	private void appendAuthorizationRights(StringBuilder sb) {
		if (authorizationRights != null && authorizationRights.length() > 0){
			sb.append("&authorization-rights="+encodeValue(authorizationRights));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("http://");
		sb.append(server).append(":").append(configuration.getPort()).append("/vivisimo/cgi-bin/velocity?v.function=query-search&v.indent=true");

		String smartConditions = "";

		if (this.textSearch != null && this.textSearch.trim().length() != 0) {
			sb.append("&query=").append(encodeValue(this.textSearch.substring(1, this.textSearch.length() - 1)));
		}
		if (this.criteria != null && this.criteria.trim().length() != 0) {
			sb.append("&query-condition-xpath=").append(this.criteria);
			if (smartConditions != null && smartConditions.trim().length() != 0) {
				sb.append(" and " + smartConditions);
			}
		} else if (smartConditions != null && smartConditions.trim().length() != 0) {
			sb.append("&query-condition-xpath=").append(smartConditions);
		}

		sb.append("&v.username=").append(configuration.getUser()).append("&v.password=").append(configuration.getPassword()).append("&sources=");
		for (String c : this.collections) {
			sb.append(c).append(" ");
		}

		sb.append("&arena=").append(arena).append("&output-contents-mode=list").append("&output-contents=");
		if (this.fields != null){
			for (String f : this.fields) {
				sb.append(f).append(" ");
			}
		}
		
		if (this.facets.size() > 0) {
			sb.append("&binning-configuration=");
			for (String f : this.facets) {
				sb.append(getBinningSet(f));
			}
		}

		appendPaginationAndSort(sb);

		if (nlq) {
			sb.append("&query-modification-macros=enhance-query-with-querymodifier");
		}

		sb.append("&fetch-timeout="+WexWsConstants.TIMEOUT_IN_MILIS).append("&output-display-mode=limited").append("&v.app=api-rest");

		if (profile) {
			sb.append("&debug=true&profile=true");
		}
		return sb.toString().replace("\"", "%22").replace(" ", "+").replace("<", "%3C").replace(">", "%3E").replace("$", "%24").replace("(", "%28").replace(")", "%29");

	}

	public Set<String> getCollections() {
		return collections;
	}

	public void setCollections(Set<String> collections) {
		this.collections = collections;
	}

	public String getArena() {
		return arena;
	}

	public void setArena(String arena) {
		this.arena = arena;
	}

	private String getBinningSet(String facetName) {
		int maxBins = 8;
		int idx = 0;
		if ((idx = facetName.indexOf(":")) > -1) {
			String xmaxBins = facetName.substring(idx + 1);
			maxBins = (Integer.parseInt(xmaxBins) + 1) * this.numberOfShards;
			facetName = facetName.substring(0, idx);
		}
		return "<binning-set bs-id=\"" + facetName + "\" logic=\"or\"  max-bins=\"" + maxBins + "\" select=\"$" + facetName + "\" />";
	}

	public String getTextSearch() {
		return textSearch;
	}

	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}

	public boolean isNlq() {
		return nlq;
	}

	public void setNlq(boolean nlq) {
		this.nlq = nlq;
	}

	public boolean isStemming() {
		return stemming;
	}

	public void setStemming(boolean stemming) {
		this.stemming = stemming;
	}
	
	public boolean isSpelling() {
		return spelling;
	}

	public void setSpelling(boolean spelling) {
		this.spelling = spelling;
	}
	
	public boolean isJapaneseSearch() {
		return japaneseSearch;
	}

	public void setJapaneseSearch(boolean japaneseSearch) {
		this.japaneseSearch = japaneseSearch;
	}
	
}
