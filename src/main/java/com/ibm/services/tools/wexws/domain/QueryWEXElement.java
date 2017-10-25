package com.ibm.services.tools.wexws.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * This class represents a <query> element from WEX XML result
 * @author Daniel Paganotti
 *
 */
public class QueryWEXElement {

	private List<Operator> operators;

	public List<Operator> getOperators() {
		return operators;
	}
	@XmlElement(name="op-exp")
	public void setOperators(List<Operator> operators) {
		this.operators = operators;
	}
}
