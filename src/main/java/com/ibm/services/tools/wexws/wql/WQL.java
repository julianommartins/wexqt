package com.ibm.services.tools.wexws.wql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WQL {

	private static String[] KEYWORDS = { "SELECT", "FIELDS", "FACETS", "FROM",
			"TEXTSEARCH", "WHERE", "START", "NUM", "SORTBY" };

	private String wqlStatement;

	private Map<String, Keyword> keywordMap;
	
	private String validationMessage;

	public WQL(String wqlStatement) {
		this.wqlStatement = wqlStatement.trim();
		init();
	}

	private void init() {
		
		List<Keyword> keywords = getKeywordList();

		sort(keywords);

		assignNextKeywords(keywords);

		createMap(keywords);

	}
	
	private List<Keyword> getKeywordList() {

		List<Keyword> keywords = new ArrayList<Keyword>();

		for (String word : KEYWORDS) {
			Keyword keyword = getKeyword(word);
			if (keyword != null) {
				keywords.add(keyword);
			}
		}

		return keywords;
	}

	private Keyword getKeyword(String word) {
		Keyword keyword = null;

		int idx = this.wqlStatement.indexOf(word + " ");
		if (idx > -1) {
			keyword = new Keyword(word, idx);
		}
		return keyword;
	}

	private void sort(List<Keyword> keywords) {

		Collections.sort(keywords, new Comparator<Keyword>() {

			@Override
			public int compare(Keyword kw1, Keyword kw2) {
				return kw1.getPosition() - kw2.getPosition();
			}

		});

	}

	private void assignNextKeywords(List<Keyword> keywords) {

		Keyword nextKeyword = null;

		for (int i = keywords.size() - 1; i > -1; i--) {
			Keyword keyword = keywords.get(i);
			if (nextKeyword != null) {
				keyword.setNextKeyword(nextKeyword);
			}
			nextKeyword = keyword;
		}

	}

	private void createMap(List<Keyword> keywords) {
		this.keywordMap = new HashMap<String, Keyword>();

		for (Keyword kw : keywords) {
			this.keywordMap.put(kw.getKeyword(), kw);
		}

	}

	public String getKeywordParamValue(String keyword){
		
		String paramValue=null;
		
		Keyword kw1 = this.keywordMap.get(keyword);
		
		if(kw1!=null){
			paramValue = getSubstring(kw1,kw1.getNextKeyword());
		}
		
		
		return paramValue;
	}

	private String getSubstring(Keyword kw1, Keyword kw2) {
		String substring=null;
		
		if(kw1!=null && kw2!=null){
			substring = this.wqlStatement.substring(
									kw1.getPosition()+kw1.getKeyword().length(),
									kw2.getPosition()
								).trim();
		}else
		if(kw1!=null && kw2==null){
			substring = this.wqlStatement.substring(
					kw1.getPosition()+kw1.getKeyword().length()
				).trim();
		}
	
		return substring;
	}

	public String getCollection() {
		return getKeywordParamValue("FROM");
	}

	public List<String> getRequestedFields() {
		List<String> list = getAsList("FIELDS");
		
		return list;
	}
	
	public List<String> getRequestedFacets() {
		List<String> list = getAsList("FACETS");
		
		return list;
	}
	
	private List<String> getAsList(String keyword) {
		List<String> list = new ArrayList<String>();
		String data = getKeywordParamValue(keyword);
		if(data!=null){
			for(String item : data.split(",")){
				list.add(item.trim());
			}
			
		}
		return list;
	}

	public String getTextSearch() {
		return getKeywordParamValue("TEXTSEARCH");
	}

	
	public String getCriteria() {
		return getKeywordParamValue("WHERE");
	}

	public int getStartAt() {
		String xvalue = getKeywordParamValue("START");
		if(xvalue!=null){
			return Integer.parseInt(xvalue);
		}
		return 0;
	}

	public int getNum() {
		String xvalue = getKeywordParamValue("NUM");
		if(xvalue!=null){
			return Integer.parseInt(xvalue);
		}
		return 1;
	}

	public String getSortFieldName() {
		String sortBy = getKeywordParamValue("SORTBY");
		if(sortBy==null) return null;
		
		int idx;
		if((idx=sortBy.indexOf(":"))>-1){
			sortBy = sortBy.substring(0,idx); 
		}
		
		return sortBy.trim();
	}
	
	public String getSortOrder() {
		String sortOrder = getKeywordParamValue("SORTBY");
		if(sortOrder==null) return null;
		
		int idx;
		if((idx=sortOrder.indexOf(":"))>-1){
			sortOrder = sortOrder.substring(idx+1); 
		}else{
			sortOrder = "asc";
		}
		return sortOrder.trim();
	}
	

	public boolean isValid() {
		if (this.keywordMap.get("SELECT") == null) {
			this.validationMessage = "Enter the query.";
			return false;
		}
		if ((getRequestedFields() == null || getRequestedFields().size() == 0) && (getRequestedFacets() == null || getRequestedFacets().size() == 0)) {
			this.validationMessage = "Missing FIELDS or FACETS request";
			return false;
		}
		if (getCollection() == null || getCollection().trim().length() == 0) {
			this.validationMessage = "Missing FROM collection";
			return false;
		}
		return true;
	}

	public String getStatement(){
		return this.wqlStatement;
	}

	public String getValidationMessage() {
		return this.validationMessage;
	}

}
