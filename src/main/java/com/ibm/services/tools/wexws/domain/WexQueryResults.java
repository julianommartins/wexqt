package com.ibm.services.tools.wexws.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement( name = "query-results" )
public class WexQueryResults {

	private Binning binning;
	private List<AddedSource> addedSourceList;
	private MatchesList matchesList;
	private List<QueryWEXElement> queryWEXElementList;
	private String browseFileName;
	
	private Throwable executionException;
	
	public WexQueryResults(){}
	
	public WexQueryResults(Throwable executionException){
		this.executionException = executionException;
	}
	
	public Binning getBinning() {
		return binning;
	}
	
	@XmlElement(name="binning",required=false,nillable=true)
	public void setBinnings(Binning binning) {
		this.binning = binning;
	}


	public List<AddedSource> getAddedSourceList() {
		return addedSourceList;
	}


	@XmlElement(name="added-source")
	public void setAddedSourceList(List<AddedSource> addedSourceList) {
		this.addedSourceList = addedSourceList;
	}


	public void setBinning(Binning binning) {
		this.binning = binning;
	}


	public MatchesList getMatchesList() {
		return matchesList;
	}

	@XmlElement(name="list")
	public void setMatchesList(MatchesList matchesList) {
		this.matchesList = matchesList;
	}

	public List<QueryWEXElement> getQueryWEXElementList() {
		return queryWEXElementList;
	}
	@XmlElement(name="query", required=false)
	public void setQueryWEXElementList(List<QueryWEXElement> queryWEXElementList) {
		this.queryWEXElementList = queryWEXElementList;
	}

	public Throwable getExecutionException() {
		return executionException;
	}

	public void setExecutionException(Throwable executionException) {
		this.executionException = executionException;
	}

	public String getBrowseFileName() {
		return browseFileName;
	}
	@XmlAttribute(name="file", required=false)
	public void setBrowseFileName(String browseFileName) {
		this.browseFileName = browseFileName;
	}	
}
