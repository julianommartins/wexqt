package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

public abstract class LogicOperationFilter implements Filter{
	private final Filter[] filters;

	protected LogicOperationFilter(Filter... filters) {
		this.filters = filters;
	}

	public Filter[] getFilters() {
		return filters;
	}
	
	protected void visitChildren(QueryFilterVisitor visitor) {
		for (Filter filter : getFilters()) {
			filter.accept(visitor);
		}
	}
}
