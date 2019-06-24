package com.compli.bean;

import java.util.List;

public class CompanyLocations {
	String companyId;
	List<String>locationIds;
	
	public String getCompanyId() {
		return companyId;
	}
	public List<String> getLocationIds() {
		return locationIds;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public void setLocationIds(List<String> locationIds) {
		this.locationIds = locationIds;
	}
	
	
}
