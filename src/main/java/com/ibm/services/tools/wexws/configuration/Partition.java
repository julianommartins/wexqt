package com.ibm.services.tools.wexws.configuration;

import java.util.List;

public class Partition {

	private String name;
	private String description;
	private String criteria;
	private MatchingRules matchingRules;
	private int immediateAvailabilityDays;
	private int lateArrivalDays;
	private int maxMustHaveKeywords;
	private int maxNiceToHaveKeywords;
	private int lowBandSlack;
	private int highBandSlack;
	private List<String> jobRetrievedFields;
	private List<String> personRetrievedFields;

	public Partition() {

	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	public String getCriteria() {
		return criteria;
	}

	void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public MatchingRules getMatchingRules() {
		return matchingRules;
	}

	void setMatchingRules(MatchingRules matchingRules) {
		this.matchingRules = matchingRules;
	}
	
	public int getImmediateAvailabilityDays() {
		return immediateAvailabilityDays;
	}

	void setImmediateAvailabilityDays(long immediateAvailabilityDays) {
		this.immediateAvailabilityDays = (int) immediateAvailabilityDays;
	}

	public int getLateArrivalDays() {
		return lateArrivalDays;
	}

	void setLateArrivalDays(long lateArrivalDays) {
		this.lateArrivalDays = (int) lateArrivalDays;
	}

	public int getMaxMustHaveKeywords() {
		return maxMustHaveKeywords;
	}

	public void setMaxMustHaveKeywords(long maxMustHaveKeywords) {
		this.maxMustHaveKeywords = (int) maxMustHaveKeywords;
	}

	public int getMaxNiceToHaveKeywords() {
		return maxNiceToHaveKeywords;
	}

	public void setMaxNiceToHaveKeywords(long maxNiceToHaveKeywords) {
		this.maxNiceToHaveKeywords = (int) maxNiceToHaveKeywords;
	}

	public int getLowBandSlack() {
		return lowBandSlack;
	}

	void setLowBandSlack(long lowBandSlack) {
		this.lowBandSlack = (int) lowBandSlack;
	}

	public int getHighBandSlack() {
		return highBandSlack;
	}

	void setHighBandSlack(long highBandSlack) {
		this.highBandSlack = (int) highBandSlack;
	}

	public List<String> getJobRetrievedFields() {
		return jobRetrievedFields;
	}

	void setJobRetrievedFields(List<String> jobRetrievedFields) {
		this.jobRetrievedFields = jobRetrievedFields;
	}

	public List<String> getPersonRetrievedFields() {
		return personRetrievedFields;
	}

	void setPersonRetrievedFields(List<String> personRetrievedFields) {
		this.personRetrievedFields = personRetrievedFields;
	}
	
	
}
