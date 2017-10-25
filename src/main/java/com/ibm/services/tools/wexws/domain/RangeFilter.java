package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

public class RangeFilter extends FieldFilter {

	private final RangeOperator operator;
	private final Number firstValue;
	private final Number secondValue;
	
	public RangeFilter(String fieldName, Number firstValue, Number secondValue, RangeOperator operator) {
		super(fieldName);
		this.operator = operator;
		this.firstValue = firstValue;
		this.secondValue = secondValue;
	}

	public RangeOperator getOperator() {
		return operator;
	}

	public Number getFirstValue() {
		return firstValue;
	}

	public Number getSecondValue() {
		return secondValue;
	}
	
	@Override
	public void accept(QueryFilterVisitor visitor) {
		visitor.visit(this);
	}

}
