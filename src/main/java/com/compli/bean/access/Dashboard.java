package com.compli.bean.access;

public class Dashboard {
	boolean serviaritycount = true;
	boolean monthlyOverview  = true;
	boolean complianceOverview  = true;
	boolean snapshot  = true;
	
	public boolean isServiaritycount() {
		return serviaritycount;
	}
	public boolean isMonthlyOverview() {
		return monthlyOverview;
	}
	public boolean isComplianceOverview() {
		return complianceOverview;
	}
	public boolean isSnapshot() {
		return snapshot;
	}
	public void setServiaritycount(boolean serviaritycount) {
		this.serviaritycount = serviaritycount;
	}
	public void setMonthlyOverview(boolean monthlyOverview) {
		this.monthlyOverview = monthlyOverview;
	}
	public void setComplianceOverview(boolean complianceOverview) {
		this.complianceOverview = complianceOverview;
	}
	public void setSnapshot(boolean snapshot) {
		this.snapshot = snapshot;
	}
	
	
}
