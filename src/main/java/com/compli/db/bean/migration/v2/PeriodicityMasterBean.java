package com.compli.db.bean.migration.v2;

public class PeriodicityMasterBean {
	String periodicityId,description;
	
	public PeriodicityMasterBean(String periodicityId, String description) {
		this.periodicityId = periodicityId;
		this.description = description;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((periodicityId == null) ? 0 : periodicityId.hashCode());
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
		if (periodicityId == null) {
			if (other.periodicityId != null)
				return false;
		} else if (!periodicityId.equals(other.periodicityId))
			return false;
		return true;
	}



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
	
}
