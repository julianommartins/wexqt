package com.ibm.services.tools.wexws.domain;

import javax.xml.bind.annotation.XmlAttribute;

public class DocumentSortKey {
	private String direction;
	private String type;
	private String value;
	
	public String getDirection() {
		return direction;
	}
	@XmlAttribute
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getType() {
		return type;
	}
	@XmlAttribute
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	@XmlAttribute
	public void setValue(String value) {
		this.value = value;
	}
	
}
