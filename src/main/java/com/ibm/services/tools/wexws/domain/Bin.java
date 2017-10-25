package com.ibm.services.tools.wexws.domain;

import javax.xml.bind.annotation.XmlAttribute;

public class Bin {
	
	private String state;
	private String label;
	private int ndocs;
	private int cndocs;
	
	
	public String getState() {
		return state;
	}
	
	@XmlAttribute(name="state")
	public void setState(String state) {
		this.state = state;
	}
	
	public String getLabel() {
		return label;
	}
	@XmlAttribute(name="label")
	public void setLabel(String label) {
		this.label = label;
	}
	
	public int getNdocs() {
		return ndocs;
	}
	
	@XmlAttribute(name="ndocs")
	public void setNdocs(int ndocs) {
		this.ndocs = ndocs;
	}
	public int getCndocs() {
		return cndocs;
	}
	
	@XmlAttribute(name="cndocs")
	public void setCndocs(int cndocs) {
		this.cndocs = cndocs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cndocs;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ndocs;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bin other = (Bin) obj;
		if (cndocs != other.cndocs)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (ndocs != other.ndocs)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
	

}
