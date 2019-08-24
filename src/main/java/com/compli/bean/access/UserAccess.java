package com.compli.bean.access;

public class UserAccess {
	String userId;
	String firstname;
	String lastname;
	boolean dashboardLnk = true;
	Dashboard dashboard = new Dashboard();
	boolean activitiesLnk = true;
	Activities activities = new Activities();
	boolean reportLnk = true;
	boolean repositoriesLnk = true;
	
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public boolean isDashboardLnk() {
		return dashboardLnk;
	}
	public Dashboard getDashboard() {
		return dashboard;
	}
	public boolean isActivitiesLnk() {
		return activitiesLnk;
	}
	public Activities getActivities() {
		return activities;
	}
	public boolean isReportLnk() {
		return reportLnk;
	}
	public boolean isRepositoriesLnk() {
		return repositoriesLnk;
	}
	public void setDashboardLnk(boolean dashboardLnk) {
		this.dashboardLnk = dashboardLnk;
	}
	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}
	public void setActivitiesLnk(boolean activitiesLnk) {
		this.activitiesLnk = activitiesLnk;
	}
	public void setActivities(Activities activities) {
		this.activities = activities;
	}
	public void setReportLnk(boolean reportLnk) {
		this.reportLnk = reportLnk;
	}
	public void setRepositoriesLnk(boolean repositoriesLnk) {
		this.repositoriesLnk = repositoriesLnk;
	}
	
	
}
