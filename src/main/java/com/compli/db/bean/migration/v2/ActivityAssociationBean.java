package com.compli.db.bean.migration.v2;

public class ActivityAssociationBean {
	String associationId,activityId,fyRecordId,locationId;

	public ActivityAssociationBean() {
	
	}
	
	public ActivityAssociationBean(String associationId, String activityId,
			String fyRecordId, String locationId) {
		this.associationId = associationId;
		this.activityId = activityId;
		this.fyRecordId = fyRecordId;
		this.locationId = locationId;
	}

	public String getAssociationId() {
		return associationId;
	}

	public String getActivityId() {
		return activityId;
	}

	public String getFyRecordId() {
		return fyRecordId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setAssociationId(String associationId) {
		this.associationId = associationId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public void setFyRecordId(String fyRecordId) {
		this.fyRecordId = fyRecordId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	@Override
	public String toString() {
		return "ActivityAssociationBean [associationId=" + associationId
				+ ", activityId=" + activityId + ", fyRecordId=" + fyRecordId
				+ ", locationId=" + locationId + "]";
	}
	
	
}
