package com.ibm.services.tools.wexws.domain;

public abstract class FieldFilter implements Filter {

	protected final String fieldName;
	
	protected FieldFilter(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
