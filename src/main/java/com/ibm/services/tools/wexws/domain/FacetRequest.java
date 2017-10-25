package com.ibm.services.tools.wexws.domain;

public class FacetRequest {
	private String facetName;
	private String selectXPath;
	private int maximumNumberOfValues = 10;
	private String logic = "or";
	private FacetSortingMode sortingMode;
	
	public String getFacetName() {
		return facetName;
	}
	public FacetRequest withFacetName(String facetName) {
		this.facetName = facetName;
		return this;
	}
	public String getSelectXPath() {
		return selectXPath;
	}
	public FacetRequest withSelectXPath(String selectXPath) {
		this.selectXPath = selectXPath;
		return this;
	}
	public int getMaximumNumberOfValues() {
		return maximumNumberOfValues;
	}
	public FacetRequest withMaximumNumberOfValues(int maximumNumberOfValues) {
		this.maximumNumberOfValues = maximumNumberOfValues;
		return this;
	}
	public FacetSortingMode getSortingMode() {
		return sortingMode;
	}
	public FacetRequest withSortingMode(FacetSortingMode sortingMode) {
		this.sortingMode = sortingMode;
		return this;
	}
	public String getLogic() {
		return logic;
	}
	public FacetRequest withLogic(String logic) {
		this.logic = logic;
		return this;
	}
	
	@Override
	public String toString() {
		return "\"" + facetName + "\"";
	}
	
}
