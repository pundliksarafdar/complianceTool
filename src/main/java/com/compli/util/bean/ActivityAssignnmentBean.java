package com.compli.util.bean;

import java.util.Date;

public class ActivityAssignnmentBean {
	String userId;
	String activityId;
	Date modifiedOn;
	
	public ActivityAssignnmentBean() {
	
	}
	public ActivityAssignnmentBean(String userId,String activityId) {
		this.userId = userId;
		this.activityId = activityId;
	}
	
	public String getUserId() {
		return userId;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activityId == null) ? 0 : activityId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		ActivityAssignnmentBean other = (ActivityAssignnmentBean) obj;
		if (activityId == null) {
			if (other.activityId != null)
				return false;
		} else if (!activityId.equals(other.activityId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	
}
