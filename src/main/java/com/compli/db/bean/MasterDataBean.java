package com.compli.db.bean;

public class MasterDataBean {
    private String activityId;
    private String locationId;
    private String complianceArea;
    private String activityname;
    private String lawdescription;
    private String risk;
    private String consequences;
    private String form;
    private String periodicity;
    private String dueDate;
    private boolean disabled;

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getComplianceArea() {
        return complianceArea;
    }

    public void setComplianceArea(String complianceArea) {
        this.complianceArea = complianceArea;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getLawdescription() {
        return lawdescription;
    }

    public void setLawdescription(String lawdescription) {
        this.lawdescription = lawdescription;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getConsequences() {
        return consequences;
    }

    public void setConsequences(String consequences) {
        this.consequences = consequences;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
