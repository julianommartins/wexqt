package com.ibm.services.tools.wexws.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class AddedSource {

	private static final String[] errorStatuses = new String[]{"skipped-unsupported-syntax", "ignore", "disabled", "broken", 
			"unknown", "skipped-missing-variables"};
	
	private String name;
	private String status;
	private int requested;
	private int totalResults;
	private int totalResultsWithDuplicates;
	private int retrieved;

	private AddedSourceParse sourceParse;
	private List<AddedSource> childSources;
	
	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	@XmlAttribute(name = "status")
	public void setStatus(String status) {
		this.status = status;
	}

	public int getRequested() {
		return requested;
	}

	@XmlAttribute(name = "requested")
	public void setRequested(int requested) {
		this.requested = requested;
	}

	public int getTotalResults() {
		int count = 0;
		if (childSources != null && childSources.size() > 0){
			for (AddedSource childSource: childSources){
				count += childSource.getTotalResults();
			}
		}else{
			count = totalResults;
		}
		return count;
	}

	@XmlAttribute(name = "total-results")
	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public int getTotalResultsWithDuplicates() {
		return totalResultsWithDuplicates;
	}

	@XmlAttribute(name = "total-results-with-duplicates")
	public void setTotalResultsWithDuplicates(int totalResultsWithDuplicates) {
		this.totalResultsWithDuplicates = totalResultsWithDuplicates;
	}

	public int getRetrieved() {
		return retrieved;
	}

	@XmlAttribute(name = "retrieved")
	public void setRetrieved(int retrieved) {
		this.retrieved = retrieved;
	}

	public AddedSourceParse getSourceParse() {
		return sourceParse;
	}
	@XmlElement(name="parse")
	public void setSourceParse(AddedSourceParse sourceParse) {
		this.sourceParse = sourceParse;
	}
	
	public boolean hasErrors(){
		boolean hasErrors = false;
		if (sourceParse != null){
			hasErrors = sourceParse.hasErrors();
		}
		if (!hasErrors && status != null){
			for (String errorStatus: errorStatuses){
				if (status.contains(errorStatus)){
					hasErrors = true;
					break;
				}
			}
		}
		if (!hasErrors && childSources != null){
			for (AddedSource childSource: childSources){
				if (childSource.hasErrors()){
					hasErrors = true;
					break;
				}
			}
		}
		return hasErrors;
	}
	
	public List<String> getErrorMessagesFromSource(){
		List<String> errorMessages = null;
		String error = getErrorMessage();
		if (error != null){
			errorMessages = Arrays.asList(error);
		}else{
			if (childSources != null && childSources.size() > 0){
				for (AddedSource source: childSources){
					List<String> errorMessagesFromChild = source.getErrorMessagesFromSource();
					if (errorMessagesFromChild != null){
						if (errorMessages == null){
							errorMessages = new ArrayList<String>(4);
						}
						errorMessages.addAll(errorMessagesFromChild);
					}
				}
			}
		}
		return errorMessages;
	}

	private String getErrorMessage() {
		String error = null;
		if (hasErrors()){
			error = "Error from source name="+name+", message=";
			if (sourceParse != null && sourceParse.hasErrors()){
				error += sourceParse.getStatus();
			}else{
				error += status;
			}
		}
		return error;
	}

	public List<AddedSource> getChildSources() {
		return childSources;
	}
	
	@XmlElement(name="added-source", required=false)
	public void setChildSources(List<AddedSource> childSources) {
		this.childSources = childSources;
	}

}
