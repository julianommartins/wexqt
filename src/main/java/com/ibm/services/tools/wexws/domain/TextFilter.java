package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

public class TextFilter extends FieldFilter {

	private final String textFilter;
	
	public TextFilter(String fieldName, String textFilter) {
		super(fieldName);
		this.textFilter = textFilter;
	}

	public String getTextFilter() {
		return textFilter;
	}

	@Override
	public void accept(QueryFilterVisitor visitor) {
		visitor.visit(this);
	}
}
