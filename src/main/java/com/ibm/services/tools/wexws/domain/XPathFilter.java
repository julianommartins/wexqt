package com.ibm.services.tools.wexws.domain;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

public class XPathFilter implements Filter {

	private final String xpath;

	public XPathFilter(String xpath) {
		super();
		this.xpath = xpath;
	}

	@Override
	public void accept(QueryFilterVisitor visitor) {
		visitor.visit(this);
	}

	public String getXpath() {
		return xpath;
	}
	
	@Override
	public String toString() {
		return xpath;
	}

}
