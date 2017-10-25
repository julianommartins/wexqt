package com.ibm.services.tools.wexws.domain;

import java.util.List;

import com.ibm.services.tools.wexws.utils.DateUtils;

/**
 * Represent the JOB that EM function uses
 * @author julianom
 *
 */
public class OpenSeat {
	// OPEN_SEAT_TITLE
	private String title;

	// OPEN_SEAT_TYPE
	private String type;

	// MYSA_SEAT_LOB
	private String mySASeatLob;

	// JOB_ROLE
	private String jobRole;

	// OPEN_SEAT_ID
	private String osID;

	// START_DATE
	private String startDate;

	// MYSA_OPNSET_IND
	private int mySAIndicator = 0;

	// MYSA_CONTRACT_OWNING_LOB
	private String mySAContractOwningLob;

	// MYSA_FULFILLMENT_LOB
	private String mySAFulfillmentLob;

	// MYSA_DELIVERY_LOB
	private String mySADeliveryLob;

	// GR_PROVIDER_COUNTRY_IOT
	private String grProviderCountryIOT;

	// GR_PROVIDER_COUNTRY
	private String grProviderCountry;

	// REQUESTED_BAND_LOW
	private String requestedBandLow;

	// REQUESTED_BAND_HIGH
	private String requestedBandHigh;

	// REQUESTED_LANGUAGE
	private String requestedLanguage;

	// REQUIRED_SKILLS
	private String requiredSkills;

	// SKILL_SET
	private String skillSet;

	// NICE_TO_HAVE_SKILLS
	private String niceToHaveSkills;

	// WORK_LOCATION_IOT
	private String workLocationIOT;

	// WORK_LOCATION_IMT
	private String workLocationIMT;

	// WORK_LOCATION_COUNTRY
	private String workLocationCountry;

	// REQUESTED_BAND_HIGH_SORT
	private String requestedBandHighSort;

	// REQUESTED_BAND_LOW_SORT
	private String requestedBandLowSort;

	private List<KeywordFilter> mustHaveKeywords;
	private List<KeywordFilter> niceToHaveKeywords;
	
	private String partition;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMySASeatLob() {
		return mySASeatLob;
	}

	public void setMySASeatLob(String lob) {
		this.mySASeatLob = lob;
	}

	public String getJobRole() {
		return jobRole;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	public String getOsID() {
		return osID;
	}

	public void setOsID(String osID) {
		this.osID = osID;
	}

	public int getMySAIndicator() {
		return mySAIndicator;
	}

	public void setMySAIndicator(int mySAIndicator) {
		this.mySAIndicator = mySAIndicator;
	}

	public String getMySAContractOwningLob() {
		return mySAContractOwningLob;
	}

	public void setMySAContractOwningLob(String mySAContractOwningLob) {
		this.mySAContractOwningLob = mySAContractOwningLob;
	}

	public String getMySAFulfillmentLob() {
		return mySAFulfillmentLob;
	}

	public void setMySAFulfillmentLob(String mySAFulfillmentLob) {
		this.mySAFulfillmentLob = mySAFulfillmentLob;
	}

	public String getMySADeliveryLob() {
		return mySADeliveryLob;
	}

	public void setMySADeliveryLob(String mySADeliveryLob) {
		this.mySADeliveryLob = mySADeliveryLob;
	}

	public String getGrProviderCountryIOT() {
		return grProviderCountryIOT;
	}

	public void setGrProviderCountryIOT(String grProviderCountryIOT) {
		this.grProviderCountryIOT = grProviderCountryIOT;
	}

	public String getRequestedBandLow() {
		return requestedBandLow;
	}

	public void setRequestedBandLow(String requestedBandLow) {
		this.requestedBandLow = requestedBandLow;
	}

	public String getRequestedBandHigh() {
		return requestedBandHigh;
	}

	public void setRequestedBandHigh(String requestedBandHigh) {
		this.requestedBandHigh = requestedBandHigh;
	}

	public String getRequestedLanguage() {
		return requestedLanguage;
	}

	public void setRequestedLanguage(String requestedLanguage) {
		this.requestedLanguage = requestedLanguage;
	}

	public String getRequiredSkills() {
		return requiredSkills;
	}

	public void setRequiredSkills(String requiredSkills) {
		this.requiredSkills = requiredSkills;
	}

	public String getNiceToHaveSkills() {
		return niceToHaveSkills;
	}

	public void setNiceToHaveSkills(String niceToHaveSkills) {
		this.niceToHaveSkills = niceToHaveSkills;
	}

	public String getWorkLocationIOT() {
		return workLocationIOT;
	}

	public void setWorkLocationIOT(String workLocationIOT) {
		this.workLocationIOT = workLocationIOT;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequestedBandHighSort() {
		return requestedBandHighSort;
	}

	public void setRequestedBandHighSort(String requestedBandHighSort) {
		this.requestedBandHighSort = requestedBandHighSort;
	}

	public String getRequestedBandLowSort() {
		return requestedBandLowSort;
	}

	public void setRequestedBandLowSort(String requestedBandLowSort) {
		this.requestedBandLowSort = requestedBandLowSort;
	}

	public String getGrProviderCountry() {
		return grProviderCountry;
	}

	public void setGrProviderCountry(String grProviderCountry) {
		this.grProviderCountry = grProviderCountry;
	}

	public String getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(String skillSet) {
		this.skillSet = skillSet;
	}

	public String getStartDate() {
		return startDate;
	}
	
	public String getStartDateFormated() {
		try{
			return DateUtils.dateToFormattedString(DateUtils.unixTimeStampToDate(startDate));
		}catch (Exception ex){
			return startDate;
		}
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public List<KeywordFilter> getMustHaveKeywords() {
		return mustHaveKeywords;
	}

	public void setMustHaveKeywords(List<KeywordFilter> mustHaveKeywords) {
		this.mustHaveKeywords = mustHaveKeywords;
	}

	public List<KeywordFilter> getNiceToHaveKeywords() {
		return niceToHaveKeywords;
	}

	public void setNiceToHaveKeywords(List<KeywordFilter> niceToHaveKeywords) {
		this.niceToHaveKeywords = niceToHaveKeywords;
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
	}

	public String getWorkLocationIMT() {
		return workLocationIMT;
	}

	public void setWorkLocationIMT(String workLocationIMT) {
		this.workLocationIMT = workLocationIMT;
	}

	public String getWorkLocationCountry() {
		return workLocationCountry;
	}

	public void setWorkLocationCountry(String workLocationCountry) {
		this.workLocationCountry = workLocationCountry;
	}

	@Override
	public String toString() {
		return "Partition=" + partition + ", ID=" + osID + ", Title=" + title + ", Type=" + type + ", mySASeatLob=" + mySASeatLob + ", Job Role=" + jobRole +
				", startDate=" + this.getStartDateFormated() + ", mySAIndicator=" + mySAIndicator + ", mySAContractOwningLob=" + mySAContractOwningLob + 
				", mySAFulfillmentLob=" + mySAFulfillmentLob + ", mySADeliveryLob=" + mySADeliveryLob + ", grProviderCountryIOT=" + grProviderCountryIOT +
				", grProviderCountry=" + grProviderCountry + ", requestedBandLow=" + requestedBandLow + ", requestedBandHigh=" + requestedBandHigh + 
				", requestedLanguage=" + requestedLanguage + ", <b>requiredSkills</b>=" + requiredSkills + ", <b>skillSet</b>=" + skillSet + 
				", <b>niceToHaveSkills</b>=" + niceToHaveSkills + ", workLocationIOT=" + workLocationIOT + ", workLocationIMT=" + workLocationIMT + 
				", workLocationCountry=" + workLocationCountry + ", requestedBandHighSort=" + requestedBandHighSort + ", requestedBandLowSort=" 
				+ requestedBandLowSort;
	}	
	
}
