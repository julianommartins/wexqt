package com.ibm.services.tools.wexws.domain;

import java.util.List;

/**
 * Represent the professional that EM function uses
 * @author julianom
 *
 */
public class Practitioner {

	private String availabilityDate; //AVAILABILITY_DATE
	private String availabilityDateWithOffSet; //AVAILABILITY_DATE_WITH_OFFSET;
	private String band; //BAND;
	private String bandSort; //BAND_SORT;
	private String cellPhone; //CELL_PHONE;
	private String cnum; // CNUM_ID;
	private String deployability; //DEPLOYABILITY;
	private String email; // EMAIL_ADDRESS;
	private String fullName; // FULL_NAME;
	private String industry; //INDUSTRY;
	private String jobRole; // JOB_ROLE;
	private String jobResponsability; // JOB_RSPNSBLTY;
	private String jobTitle; // JOB_TITLE;
	private String language; // LANGUAGE;
	private String managerValues; // MANAGER_VALUES_MASK;
	private String mysaCostRate; // MYSA_COST_RATE;
	private String mysaPractitionerIndicator; // MYSA_PRCTNR_IND;
	private String mysaTPLob; // MYSA_TALENT_POOL_LOB;
	private String originalBand; // ORIGINAL_BAND;
	private String practitionerId; // PRACTITIONER_ID;
	private String primaryJrss; // PRIMARY_JRSS;
	private String resourceType; // RESOURCE_TYPE;
	private String resumeText; // RESUME_TEXT;
	private String rsaValues; // RSA_VALUES_MASK;
	private String secondaryJrss; // SECONDARY_JRSS;
	private String skills; // SKILLS;
	private String skillSet; // SKILL_SET;
	private String staffingReviewerValues; // STAFFING_REVIEWER_VALUES_MASK;
	private String tpProfessionalId; // TALENT_POOL_PROFESSIONAL_ID;
	private String tpType; // TALENT_POOL_TYPE;
	private String visaCountry; // VISA_COUNTRY;
	private String visaExpirationDate; // VISA_EXP_DATE;
	private String visaStatus; // VISA_STATUS;
	private String visaType; // VISA_TYPE;
	private String workPhoneNumber; // WK_PHN_NUM;
	private String workLocationCity; //WORK_LOCATION_CITY;
	private String workLocationCountry; //WORK_LOCATION_COUNTRY;
	private String workLocationFull; //WORK_LOCATION_FULL;
	private String workLocationIMT; //WORK_LOCATION_IMT;
	private String workLocationIOT; //WORK_LOCATION_IOT;
	private String workLocationState; //WORK_LOCATION_STATE;
	
	private List<KeywordFilter> mustHaveKeywords;
	private List<KeywordFilter> niceToHaveKeywords;
	
	private String partition;
	
	public String getAvailabilityDate() {
		return availabilityDate;
	}

	public void setAvailabilityDate(String availabilityDate) {
		this.availabilityDate = availabilityDate;
	}

	public String getAvailabilityDateWithOffSet() {
		return availabilityDateWithOffSet;
	}

	public void setAvailabilityDateWithOffSet(String availabilityDateWithOffSet) {
		this.availabilityDateWithOffSet = availabilityDateWithOffSet;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getBandSort() {
		return bandSort;
	}

	public void setBandSort(String bandSort) {
		this.bandSort = bandSort;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCnum() {
		return cnum;
	}

	public void setCnum(String cnum) {
		this.cnum = cnum;
	}

	public String getDeployability() {
		return deployability;
	}

	public void setDeployability(String deployability) {
		this.deployability = deployability;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getJobRole() {
		return jobRole;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	public String getJobResponsability() {
		return jobResponsability;
	}

	public void setJobResponsability(String jobResponsability) {
		this.jobResponsability = jobResponsability;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getManagerValues() {
		return managerValues;
	}

	public void setManagerValues(String managerValues) {
		this.managerValues = managerValues;
	}

	public String getMysaCostRate() {
		return mysaCostRate;
	}

	public void setMysaCostRate(String mysaCostRate) {
		this.mysaCostRate = mysaCostRate;
	}

	public String getMysaPractitionerIndicator() {
		return mysaPractitionerIndicator;
	}

	public void setMysaPractitionerIndicator(String mysaPractitionerIndicator) {
		this.mysaPractitionerIndicator = mysaPractitionerIndicator;
	}

	public String getMysaTPLob() {
		return mysaTPLob;
	}

	public void setMysaTPLob(String mysaTPLob) {
		this.mysaTPLob = mysaTPLob;
	}

	public String getOriginalBand() {
		return originalBand;
	}

	public void setOriginalBand(String originalBand) {
		this.originalBand = originalBand;
	}

	public String getPractitionerId() {
		return practitionerId;
	}

	public void setPractitionerId(String practitionerId) {
		this.practitionerId = practitionerId;
	}

	public String getPrimaryJrss() {
		return primaryJrss;
	}

	public void setPrimaryJrss(String primaryJrss) {
		this.primaryJrss = primaryJrss;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResumeText() {
		return resumeText;
	}

	public void setResumeText(String resumeText) {
		this.resumeText = resumeText;
	}

	public String getRsaValues() {
		return rsaValues;
	}

	public void setRsaValues(String rsaValues) {
		this.rsaValues = rsaValues;
	}

	public String getSecondaryJrss() {
		return secondaryJrss;
	}

	public void setSecondaryJrss(String secondaryJrss) {
		this.secondaryJrss = secondaryJrss;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getSkillSet() {
		return skillSet;
	}

	public void setSkillSet(String skillSet) {
		this.skillSet = skillSet;
	}

	public String getStaffingReviewerValues() {
		return staffingReviewerValues;
	}

	public void setStaffingReviewerValues(String staffingReviewerValues) {
		this.staffingReviewerValues = staffingReviewerValues;
	}

	public String getTpProfessionalId() {
		return tpProfessionalId;
	}

	public void setTpProfessionalId(String tpProfessionalId) {
		this.tpProfessionalId = tpProfessionalId;
	}

	public String getTpType() {
		return tpType;
	}

	public void setTpType(String tpType) {
		this.tpType = tpType;
	}

	public String getVisaCountry() {
		return visaCountry;
	}

	public void setVisaCountry(String visaCountry) {
		this.visaCountry = visaCountry;
	}

	public String getVisaExpirationDate() {
		return visaExpirationDate;
	}

	public void setVisaExpirationDate(String visaExpirationDate) {
		this.visaExpirationDate = visaExpirationDate;
	}

	public String getVisaStatus() {
		return visaStatus;
	}

	public void setVisaStatus(String visaStatus) {
		this.visaStatus = visaStatus;
	}

	public String getVisaType() {
		return visaType;
	}

	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}

	public String getWorkPhoneNumber() {
		return workPhoneNumber;
	}

	public void setWorkPhoneNumber(String workPhoneNumber) {
		this.workPhoneNumber = workPhoneNumber;
	}

	public String getWorkLocationCity() {
		return workLocationCity;
	}

	public void setWorkLocationCity(String workLocationCity) {
		this.workLocationCity = workLocationCity;
	}

	public String getWorkLocationCountry() {
		return workLocationCountry;
	}

	public void setWorkLocationCountry(String workLocationCountry) {
		this.workLocationCountry = workLocationCountry;
	}

	public String getWorkLocationFull() {
		return workLocationFull;
	}

	public void setWorkLocationFull(String workLocationFull) {
		this.workLocationFull = workLocationFull;
	}

	public String getWorkLocationIMT() {
		return workLocationIMT;
	}

	public void setWorkLocationIMT(String workLocationIMT) {
		this.workLocationIMT = workLocationIMT;
	}

	public String getWorkLocationIOT() {
		return workLocationIOT;
	}

	public void setWorkLocationIOT(String workLocationIOT) {
		this.workLocationIOT = workLocationIOT;
	}

	public String getWorkLocationState() {
		return workLocationState;
	}

	public void setWorkLocationState(String workLocationState) {
		this.workLocationState = workLocationState;
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

	@Override
	public String toString() {
		return "Partition=" + partition + ", availabilityDate=" + availabilityDate + ", availabilityDateWithOffSet="
	+ availabilityDateWithOffSet + ", band=" + band + ", bandSort=" + bandSort + ", cellPhone=" + cellPhone + ", cnum=" + cnum 
	+ ", deployability=" + deployability + ", email=" + email + ", fullName=" + fullName + ", industry=" + industry + ", jobRole=" 
	+ jobRole + ", jobResponsability=" + jobResponsability + ", jobTitle=" + jobTitle + ", language=" + language + 
	", mysaCostRate=" + mysaCostRate + ", mysaPractitionerIndicator=" + mysaPractitionerIndicator + ", mysaTPLob=" 
	+ mysaTPLob + ", originalBand=" + originalBand + ", practitionerId=" + practitionerId + ", primaryJrss=" + primaryJrss + ", resourceType="
	+ resourceType + ", secondaryJrss=" + secondaryJrss + ", skills=" + skills 
	+ ", skillSet=" + skillSet + ", tpProfessionalId=" + tpProfessionalId + ", tpType=" 
	+ tpType + ", visaCountry=" + visaCountry + ", visaExpirationDate=" + visaExpirationDate + ", visaStatus=" + visaStatus + ", visaType=" 
	+ visaType + ", workPhoneNumber=" + workPhoneNumber + ", workLocationCity=" + workLocationCity + ", workLocationCountry="
	+ workLocationCountry + ", workLocationIMT=" + workLocationIMT + ", workLocationIOT=" 
	+ workLocationIOT + ", workLocationState=" + workLocationState;
		//", resumeText=" + resumeText +
		//+ ", <b>mustHaveKeywords</b>=" + mustHaveKeywords + ", <b>niceToHaveKeywords</b>=" + niceToHaveKeywords +
	}
	
	
	
}
