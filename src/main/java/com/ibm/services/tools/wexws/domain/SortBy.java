package com.ibm.services.tools.wexws.domain;

public class SortBy {
	
	private final  String fieldXpath;
	private final SortOrder order;
	
	public SortBy(String fieldXpath, SortOrder order) {
		super();
		this.fieldXpath = fieldXpath;
		this.order = order;
	}

	public String getFieldXpath() {
		return fieldXpath;
	}

	public SortOrder getOrder() {
		return order;
	}
	
}
