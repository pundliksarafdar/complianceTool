package com.compli.db.bean.migration.v2;

public class LocationBean {
	String locationId;
	String locationName;
	
	public LocationBean(String locationId, String locationName) {
		this.locationId = locationId;
		this.locationName = locationName;
	}
	
	public String getLocationId() {
		return locationId;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((locationId == null) ? 0 : locationId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationBean other = (LocationBean) obj;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;
		return true;
	}
	
	
}
