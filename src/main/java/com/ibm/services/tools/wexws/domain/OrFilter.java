package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

public class OrFilter extends LogicOperationFilter{
	
	public OrFilter(Filter...filters) {
		super(filters);
	}
	
	@Override
	public void accept(QueryFilterVisitor visitor) {
		visitor.preVisit(this);
		visitChildren(visitor);
		visitor.visit(this);
	}
}
