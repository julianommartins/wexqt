package com.ibm.services.tools.wexws.domain;

import java.util.List;

/**
 * This class represents a search keyword used in the WEX query and its synonyms. It is populated from the WEX response
 * @author Daniel Paganotti
 */
public class SearchKeywordResponse {
	private String value;
	private String field;
	private String weight;
	private List<SearchKeywordResponse> synonymList;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<SearchKeywordResponse> getSynonymList() {
		return synonymList;
	}
	public void setSynonymList(List<SearchKeywordResponse> synonymList) {
		this.synonymList = synonymList;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((synonymList == null) ? 0 : synonymList.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchKeywordResponse other = (SearchKeywordResponse) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (synonymList == null) {
			if (other.synonymList != null)
				return false;
		} else if (!synonymList.equals(other.synonymList))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "SearchKeywordResponse [value=" + value + ", field=" + field + ", synonymList=" + synonymList + "]";
	}
}
