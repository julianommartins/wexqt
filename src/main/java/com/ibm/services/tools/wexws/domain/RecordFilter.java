package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

public class RecordFilter extends FieldFilter {

	private final String value;
	
	public RecordFilter(String fieldName, String value) {
		super(fieldName);
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	@Override
	public void accept(QueryFilterVisitor visitor) {
		visitor.visit(this);
	}
}
