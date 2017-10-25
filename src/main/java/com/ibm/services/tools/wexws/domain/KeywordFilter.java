package com.ibm.services.tools.wexws.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class KeywordFilter {

	private String keyword;
	private KeywordFilterLogic filterLogic;
	private int weight;
	
	public KeywordFilter(){
		
	}
	
	public KeywordFilter(String keyword) {
		super();
		this.keyword = keyword.trim();
		this.filterLogic = KeywordFilterLogic.UNDEFINED;
		this.weight = -1;
	}
	
	public KeywordFilter(String keyword, KeywordFilterLogic filterLogic) {
		super();
		this.keyword = keyword.trim();
		this.filterLogic = filterLogic;
		this.weight = -1;
	}
	
	public KeywordFilter(String keyword, KeywordFilterLogic filterLogic, int weight) {
		super();
		this.keyword = keyword.trim();
		this.filterLogic = filterLogic;
		this.weight = weight;
	}
	
	public String getKeyword() {
			return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public KeywordFilterLogic getFilterLogic() {
		return filterLogic;
	}
	public void setFilterLogic(KeywordFilterLogic filterLogic) {
		this.filterLogic = filterLogic;
	}
	public void applyDoubleQuotes() {
		if (!keyword.startsWith("\"")) {
			this.keyword = "\"" + this.keyword + "\"";
		}
	}
	
	public void removeDoubleQuotes() {
		keyword = keyword.replaceAll("^\"", "");
		keyword = keyword.replaceAll("\"$", "");
	}

	@JsonIgnore
	public boolean isMustHave() {
		return KeywordFilterLogic.MUST_HAVE.equals(filterLogic);
	}
	
	@JsonIgnore
	public boolean isNiceToHave() {
		return KeywordFilterLogic.NICE_TO_HAVE.equals(filterLogic);
	}
	
	@Override
	public String toString() {
		return "KeywordFilter [keyword=" + keyword + ", filterLogic=" + filterLogic + ", weight=" + weight + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		try {
			KeywordFilter other = (KeywordFilter) obj;
			return this.keyword.equalsIgnoreCase(other.keyword);
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.keyword.toLowerCase().hashCode();
	}

	@JsonIgnore
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
