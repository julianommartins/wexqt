package com.ibm.services.tools.wexws.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.ibm.services.tools.wexws.helper.QueryFilterVisitor;

@XmlAccessorType(XmlAccessType.FIELD)
public class AllTextFieldsFilter implements Filter {

	@XmlElement(name="name")
	private final String searchCriteria;
	
	public AllTextFieldsFilter() {
		this.searchCriteria = null;
	}
	
	public AllTextFieldsFilter(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getSearchCriteria() {
		return searchCriteria;
	}

	@Override
	public void accept(QueryFilterVisitor visitor) {
		visitor.visit(this);
	}

}
