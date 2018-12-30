package com.notifier.emailbean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PendingActivitiesForMail {
	 String activityId,activityName,userId,firstname,userTypeId,email,dueDateParsed,companyId,googleId;
	 String locationName,complianceArea,description,desc1;
	 Date dueDate;
	 
	 public String getDueDateParsed() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-YYYY");
		 return dateFormat.format(dueDate);
	}
	public void setDueDateParsed(String dueDateParsed) {
		this.dueDateParsed = dueDateParsed;
	}
	
	 
	public String getGoogleId() {
		return googleId;
	}
	public String getDesc1() {
		return desc1;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public void setDesc1(String desc) {
		this.desc1 = desc;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getActivityId() {
		return activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public String getUserId() {
		return userId;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getUserTypeId() {
		return userTypeId;
	}
	public String getEmail() {
		return email;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getLocationName() {
		return locationName;
	}
	public String getComplianceArea() {
		return complianceArea;
	}
	public String getDescription() {
		return description;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public void setComplianceArea(String complianceArea) {
		this.complianceArea = complianceArea;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	 
	 
}
