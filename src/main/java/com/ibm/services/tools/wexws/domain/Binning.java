package com.ibm.services.tools.wexws.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Binning {
	
	private List<BinningSet> binningSet;

	public List<BinningSet> getBinningSet() {
		return binningSet;
	}

	@XmlElement(name="binning-set")
	public void setBinningSet(List<BinningSet> binningSet) {
		this.binningSet = binningSet;
	}

}
