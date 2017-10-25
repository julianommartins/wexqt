package com.ibm.services.tools.wexws.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class BinningSet {
	
	private String BINNING_SET_XML_TEMPLATE = 
			"<binning-set bs-id=\"%s\" logic=\"%s\"  max-bins=\"%d\" select=\"%s\" />";
	
	private String id;
	private String logic = "or";
	private Integer maxBins = 10;
	private String selectXPath;
	
	private List<Bin> bins;

	public String getId() {
		return id;
	}
	
	@XmlAttribute(name="bs-id")
	public void setId(String id) {
		this.id = id;
	}

	public List<Bin> getBins() {
		return bins;
	}
	
	@XmlElement(name="bin")
	public void setBins(List<Bin> bins) {
		this.bins = bins;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bins == null) ? 0 : bins.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		BinningSet other = (BinningSet) obj;
		if (bins == null) {
			if (other.bins != null)
				return false;
		} else if (!bins.equals(other.bins))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public Integer getMaxBins() {
		return maxBins;
	}

	public void setMaxBins(Integer maxBins) {
		this.maxBins = maxBins;
	}

	public String getSelectXPath() {
		return selectXPath;
	}

	public void setSelectXPath(String selectXPath) {
		this.selectXPath = selectXPath;
	}
	
	public String toBinningSetXML(){
		String xml = String.format(BINNING_SET_XML_TEMPLATE, id, logic, maxBins, selectXPath);				
		return xml;
	}

}
