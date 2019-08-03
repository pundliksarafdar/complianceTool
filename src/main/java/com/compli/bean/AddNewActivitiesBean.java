package com.compli.bean;

import java.util.List;

public class AddNewActivitiesBean {
	
	public List<ActivityForAddNewActivity> activities;
	public List<String> companies;
	
	
	public List<ActivityForAddNewActivity> getActivities() {
		return activities;
	}

	public List<String> getCompanies() {
		return companies;
	}

	public void setActivities(List<ActivityForAddNewActivity> activities) {
		this.activities = activities;
	}

	public void setCompanies(List<String> companies) {
		this.companies = companies;
	}


	
}
