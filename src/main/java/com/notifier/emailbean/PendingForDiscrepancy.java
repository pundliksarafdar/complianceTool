package com.notifier.emailbean;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.compli.managers.SettingsManager;

public class PendingForDiscrepancy {
	String user;
	String email;
	String activityName;
	String month;
	Date periodictyDate;
	String serverUrl;
	
	public String getServerUrl() {
		this.serverUrl = SettingsManager.getStaticSettings().getServerName();
		return this.serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	public String getUser() {
		return user;
	}
	public String getActivityName() {
		return activityName;
	}
	public String getMonth() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
		return simpleDateFormat.format(periodictyDate);
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public Date getPeriodictyDate() {
		return periodictyDate;
	}
	public void setPeriodictyDate(Date periodictyDate) {
		this.periodictyDate = periodictyDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
