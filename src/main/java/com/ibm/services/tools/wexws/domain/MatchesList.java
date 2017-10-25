package com.ibm.services.tools.wexws.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class MatchesList {

	private int start;
	private int per;
	private int num;
	private List<Document> documents;

	public int getStart() {
		return start;
	}

	@XmlAttribute(name = "start")
	public void setStart(int start) {
		this.start = start;
	}

	public int getPer() {
		return per;
	}

	@XmlAttribute(name = "per")
	public void setPer(int per) {
		this.per = per;
	}

	public int getNum() {
		return num;
	}

	@XmlAttribute(name = "num")
	public void setNum(int num) {
		this.num = num;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	@XmlElement(name = "document")
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

}
