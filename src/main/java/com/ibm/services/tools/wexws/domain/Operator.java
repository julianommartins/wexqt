package com.ibm.services.tools.wexws.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class represents a <op-exp> element from WEX XML result. 
 * It can have child elements with the same structure. 
 * @author Daniel Paganotti
 *
 */
public class Operator {

	private String precedence;
	private String logic;
	private List<QuerySearchKeywordTerm> querySearchKeywordTermList;
	private List<Operator> nestedOperators;
	
	public String getPrecedence() {
		return precedence;
	}
	@XmlAttribute(name="precedence", required=false)
	public void setPrecedence(String precedence) {
		this.precedence = precedence;
	}
		
	public String getLogic() {
		return logic;
	}
	@XmlAttribute(name="logic", required=false)
	public void setLogic(String logic) {
		this.logic = logic;
	}
	
	public List<Operator> getNestedOperators() {
		return nestedOperators;
	}
	@XmlElement(name="op-exp", required=false)
	public void setNestedOperators(List<Operator> nestedOperators) {
		this.nestedOperators = nestedOperators;
	}
	public List<QuerySearchKeywordTerm> getQuerySearchKeywordTermList() {
		return querySearchKeywordTermList;
	}
	@XmlElement(name="term", required=false)
	public void setQuerySearchKeywordTermList(List<QuerySearchKeywordTerm> querySearchKeywordTermList) {
		this.querySearchKeywordTermList = querySearchKeywordTermList;
	}
}
