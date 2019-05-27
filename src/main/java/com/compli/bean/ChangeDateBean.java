package com.compli.bean;

import java.io.Serializable;
import java.util.List;

public class ChangeDateBean implements Serializable{
	public String date;
	public List<String> activityId;
	public String getDate() {
		return date;
	}
	public List<String> getActivityId() {
		return activityId;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setActivityId(List<String> activityId) {
		this.activityId = activityId;
	}
	
	
}
