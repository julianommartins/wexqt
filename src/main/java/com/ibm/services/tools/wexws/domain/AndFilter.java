package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

public class AndFilter extends LogicOperationFilter{

	public AndFilter(Filter...filters) {
		super(filters);
	}
	
	@Override
	public void accept(QueryFilterVisitor visitor) {
		visitor.preVisit(this);
		visitChildren(visitor);
		visitor.visit(this);
	}
}
