package com.compli.db.bean;

public class PeriodicityDateMasterBean {
	String periodicityDateId,dueDate;

	public PeriodicityDateMasterBean() {
	
	}
	
	public PeriodicityDateMasterBean(String periodicityDateId, String dueDate) {
		this.periodicityDateId = periodicityDateId;
		this.dueDate = dueDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((periodicityDateId == null) ? 0 : periodicityDateId
						.hashCode());
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
		PeriodicityDateMasterBean other = (PeriodicityDateMasterBean) obj;
		if (periodicityDateId == null) {
			if (other.periodicityDateId != null)
				return false;
		} else if (!periodicityDateId.equalsIgnoreCase(other.periodicityDateId))
			return false;
		return true;
	}



	public String getPeriodicityDateId() {
		return periodicityDateId;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setPeriodicityDateId(String periodicityDateId) {
		this.periodicityDateId = periodicityDateId;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	
}
