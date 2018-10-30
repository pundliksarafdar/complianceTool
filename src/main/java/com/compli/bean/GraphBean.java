package com.compli.bean;

public class GraphBean {
	String risk;
	int openActivitiesCount;
	int compliedActivitiesCount;
	
	public GraphBean() {
	
	}
	
	public GraphBean(String risk, int openActivitiesCount,
			int compliedActivitiesCount) {
		this.risk = risk;
		this.openActivitiesCount = openActivitiesCount;
		this.compliedActivitiesCount = compliedActivitiesCount;
	}
	
	public String getRisk() {
		return risk;
	}
	public int getOpenActivitiesCount() {
		return openActivitiesCount;
	}
	public int getCompliedActivitiesCount() {
		return compliedActivitiesCount;
	}
	public void setRisk(String risk) {
		this.risk = risk;
	}
	public void setOpenActivitiesCount(int openActivitiesCount) {
		this.openActivitiesCount = openActivitiesCount;
	}
	public void setCompliedActivitiesCount(int compliedActivitiesCount) {
		this.compliedActivitiesCount = compliedActivitiesCount;
	}
	
	
}
