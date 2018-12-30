package com.notifier.emailbean;

import java.util.List;

public class PendingComplainceBean {
	String user;
	String email;
	String companyId;
	List<PendingActivitiesForMail> pendingEmail;
	String serverUrl;
	String googleId;
	
	public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getUser() {
		return user;
	}
	public String getEmail() {
		return email;
	}
	public List<PendingActivitiesForMail> getPendingEmail() {
		return pendingEmail;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPendingEmail(List<PendingActivitiesForMail> pendingEmail) {
		this.pendingEmail = pendingEmail;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.email.equals(((PendingComplainceBean)obj).getEmail());
	}
	
	@Override
	public int hashCode() {
		return this.email.hashCode();
	}
}
