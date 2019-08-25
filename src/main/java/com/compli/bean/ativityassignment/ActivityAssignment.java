package com.compli.bean.ativityassignment;

import java.util.List;

public class ActivityAssignment {
	List<String> userId;
	String fromDate;
	List<String>laws;
	
	public List<String> getUserId() {
		return userId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public List<String> getLaws() {
		return laws;
	}
	public void setUserId(List<String> userId) {
		this.userId = userId;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public void setLaws(List<String> laws) {
		this.laws = laws;
	}
	
	
}
