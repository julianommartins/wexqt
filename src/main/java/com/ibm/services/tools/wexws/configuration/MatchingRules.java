package com.ibm.services.tools.wexws.configuration;

public class MatchingRules {
	
	private boolean personInScope;
	private boolean jobInScope;
	private boolean matchResourceType;
	private boolean matchLob;
	private boolean matchWorkLocation;
	private boolean personAvailabilityByDate;
	private boolean matchLateArrival;
	private boolean matchBand;
	private boolean matchLanguages;
	private boolean matchMustHaveSkills;
	private boolean matchNiceToHaveSkills;
	
	public boolean isPersonInScope() {
		return personInScope;
	}
	void setPersonInScope(boolean personInScope) {
		this.personInScope = personInScope;
	}
	public boolean isJobInScope() {
		return jobInScope;
	}
	void setJobInScope(boolean jobInScope) {
		this.jobInScope = jobInScope;
	}
	public boolean isMatchResourceType() {
		return matchResourceType;
	}
	void setMatchResourceType(boolean matchResourceType) {
		this.matchResourceType = matchResourceType;
	}
	public boolean isMatchLob() {
		return matchLob;
	}
	void setMatchLob(boolean matchLob) {
		this.matchLob = matchLob;
	}
	public boolean isMatchWorkLocation() {
		return matchWorkLocation;
	}
	void setMatchWorkLocation(boolean matchWorkLocation) {
		this.matchWorkLocation = matchWorkLocation;
	}
	public boolean isPersonAvailabilityByDate() {
		return personAvailabilityByDate;
	}
	void setPersonAvailabilityByDate(boolean personAvailabilityByDate) {
		this.personAvailabilityByDate = personAvailabilityByDate;
	}
	public boolean isMatchLateArrival() {
		return matchLateArrival;
	}
	void setMatchLateArrival(boolean matchLateArrival) {
		this.matchLateArrival = matchLateArrival;
	}
	public boolean isMatchBand() {
		return matchBand;
	}
	void setMatchBand(boolean matchBand) {
		this.matchBand = matchBand;
	}
	boolean isMatchLanguages() {
		return matchLanguages;
	}
	void setMatchLanguages(boolean matchLanguages) {
		this.matchLanguages = matchLanguages;
	}
	public boolean isMatchMustHaveSkills() {
		return matchMustHaveSkills;
	}
	void setMatchMustHaveSkills(boolean matchMustHaveSkills) {
		this.matchMustHaveSkills = matchMustHaveSkills;
	}
	public boolean isMatchNiceToHaveSkills() {
		return matchNiceToHaveSkills;
	}
	void setMatchNiceToHaveSkills(boolean matchNiceToHaveSkills) {
		this.matchNiceToHaveSkills = matchNiceToHaveSkills;
	}

}
