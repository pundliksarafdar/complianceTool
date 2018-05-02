package com.compli.db.bean;

import java.util.Date;

import com.compli.annotation.Table;

@Table(tableName="location")
public class LocationBean {
	String locationId;
	String locationName;
	
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	
}
