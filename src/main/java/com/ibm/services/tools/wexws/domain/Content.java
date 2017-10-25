package com.ibm.services.tools.wexws.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Content {

	private String name;
	private String indexFastIndex;
	private String value;
	
	@Override
	public String toString() {
		return "[" + name + ": " + value + "]";
	}

	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public String getIndexFastIndex() {
		return indexFastIndex;
	}

	@XmlAttribute(name = "indexed-fast-index")
	public void setIndexFastIndex(String indexFastIndex) {
		this.indexFastIndex = indexFastIndex;
	}

	@XmlValue
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
