package com.ibm.services.tools.wexws.domain;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * This class represents a <term> element from WEX XML result. 
 * @author Daniel Paganotti
 *
 */
public class QuerySearchKeywordTerm {
	
	private String field;
	private String value;
	private String inputType;
	private String use;
	private String relationType;
	private String weight;
	
	public String getField() {
		return field;
	}
	@XmlAttribute(name="field")
	public void setField(String field) {
		this.field = field;
	}
	public String getValue() {
		return value;
	}
	
	@XmlAttribute(name="str")
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getInputType() {
		return inputType;
	}
	@XmlAttribute(name="input-type", required=false)
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	
	public String getUse() {
		return use;
	}
	@XmlAttribute(name="use", required=false)
	public void setUse(String use) {
		this.use = use;
	}
	
	public String getRelationType() {
		return relationType;
	}
	@XmlAttribute(name="relation", required=false)
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	
	public String getWeight() {
		return weight;
	}
	@XmlAttribute(name="weight", required=false)
	public void setWeight(String weight) {
		this.weight = weight;
	}
}
