package com.compli.db.bean.migration.v2;

public class ActivityMasterBean {
	String activityId;
	String companyName;
	String companyAbbr;
	String companyLocation;
	String compalinceArea;
	String activityName;
	String activityDesc;
	String lawDesc;
	String risk;
	String consequences;
	String dueDate;
	String periodicity;
	String lawId;
	String periodicityId;
	String periodicityDateId;
	
	public ActivityMasterBean() {
	}
	
	public ActivityMasterBean(String activityId, String companyName,
			String companyAbbr, String companyLocation, String compalinceArea,
			String activityName, String activityDesc, String lawDesc,
			String risk, String consequences, String dueDate,
			String periodicity, String lawId, String periodicityId,
			String periodicityDateId) {
		super();
		this.activityId = activityId;
		this.companyName = companyName;
		this.companyAbbr = companyAbbr;
		this.companyLocation = companyLocation;
		this.compalinceArea = compalinceArea;
		this.activityName = activityName;
		this.activityDesc = activityDesc;
		this.lawDesc = lawDesc;
		this.risk = risk;
		this.consequences = consequences;
		this.dueDate = dueDate;
		this.periodicity = periodicity;
		this.lawId = lawId;
		this.periodicityId = periodicityId;
		this.periodicityDateId = periodicityDateId;
	}
	
	public String getActivityId() {
		return activityId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getCompanyAbbr() {
		return companyAbbr;
	}
	public String getCompanyLocation() {
		return companyLocation;
	}
	public String getCompalinceArea() {
		return compalinceArea;
	}
	public String getActivityName() {
		return activityName;
	}
	public String getActivityDesc() {
		return activityDesc;
	}
	public String getLawDesc() {
		return lawDesc;
	}
	public String getRisk() {
		return risk;
	}
	public String getConsequences() {
		return consequences;
	}
	public String getDueDate() {
		return dueDate;
	}
	public String getPeriodicity() {
		return periodicity;
	}
	public String getLawId() {
		return lawId;
	}
	public String getPeriodicityId() {
		return periodicityId;
	}
	public String getPeriodicityDateId() {
		return periodicityDateId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setCompanyAbbr(String companyAbbr) {
		this.companyAbbr = companyAbbr;
	}
	public void setCompanyLocation(String companyLocation) {
		this.companyLocation = companyLocation;
	}
	public void setCompalinceArea(String compalinceArea) {
		this.compalinceArea = compalinceArea;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}
	public void setLawDesc(String lawDesc) {
		this.lawDesc = lawDesc;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public void setConsequences(String consequences) {
		this.consequences = consequences;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
	}
	public void setLawId(String lawId) {
		this.lawId = lawId;
	}
	public void setPeriodicityId(String periodicityId) {
		this.periodicityId = periodicityId;
	}
	public void setPeriodicityDateId(String periodicityDateId) {
		this.periodicityDateId = periodicityDateId;
	}

	

}
