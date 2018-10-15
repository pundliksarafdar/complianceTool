package com.compli.db.bean.migration.v2;

public class ActivityBean {
	String activityId,companyId,remark,assignedUser;
	boolean isComplied,isComplianceApproved,isProofRequired,isComplianceRejected,isComplainceDelayed;

	public ActivityBean() {
	
	}

	public ActivityBean(String activityId, String companyId, String remark,
			String assignedUser, boolean isComplied,
			boolean isComplianceApproved, boolean isProofRequired,
			boolean isComplianceRejected, boolean isComplainceDelayed) {
		this.activityId = activityId;
		this.companyId = companyId;
		this.remark = remark;
		this.assignedUser = assignedUser;
		this.isComplied = isComplied;
		this.isComplianceApproved = isComplianceApproved;
		this.isProofRequired = isProofRequired;
		this.isComplianceRejected = isComplianceRejected;
		this.isComplainceDelayed = isComplainceDelayed;
	}

	public String getActivityId() {
		return activityId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getRemark() {
		return remark;
	}

	public String getAssignedUser() {
		return assignedUser;
	}

	public boolean isComplied() {
		return isComplied;
	}

	public boolean isComplianceApproved() {
		return isComplianceApproved;
	}

	public boolean isProofRequired() {
		return isProofRequired;
	}

	public boolean isComplianceRejected() {
		return isComplianceRejected;
	}

	public boolean isComplainceDelayed() {
		return isComplainceDelayed;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}

	public void setComplied(boolean isComplied) {
		this.isComplied = isComplied;
	}

	public void setComplianceApproved(boolean isComplianceApproved) {
		this.isComplianceApproved = isComplianceApproved;
	}

	public void setProofRequired(boolean isProofRequired) {
		this.isProofRequired = isProofRequired;
	}

	public void setComplianceRejected(boolean isComplianceRejected) {
		this.isComplianceRejected = isComplianceRejected;
	}

	public void setComplainceDelayed(boolean isComplainceDelayed) {
		this.isComplainceDelayed = isComplainceDelayed;
	}
	
	
	
	}
