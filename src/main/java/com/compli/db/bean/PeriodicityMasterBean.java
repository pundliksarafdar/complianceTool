package com.compli.db.bean;

public class PeriodicityMasterBean {
	String periodicityId,description;

	public String getPeriodicityId() {
		return periodicityId;
	}

	public String getDescription() {
		return description;
	}

	public void setPeriodicityId(String periodicityId) {
		this.periodicityId = periodicityId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
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
		PeriodicityMasterBean other = (PeriodicityMasterBean) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		return true;
	}
	
	
	
}
