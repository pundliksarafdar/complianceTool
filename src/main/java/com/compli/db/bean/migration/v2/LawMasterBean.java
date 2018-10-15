package com.compli.db.bean.migration.v2;

import com.compli.util.UUID;

public class LawMasterBean {
	String lawId,lawName,lawDesc;

	
	public LawMasterBean(String lawName, String lawDesc) {
		super();
		this.lawId = UUID.getUID();
		this.lawName = lawName;
		this.lawDesc = lawDesc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lawDesc == null) ? 0 : lawDesc.hashCode());
		result = prime * result + ((lawName == null) ? 0 : lawName.hashCode());
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
		LawMasterBean other = (LawMasterBean) obj;
		if (lawDesc == null) {
			if (other.lawDesc != null)
				return false;
		} else if (!lawDesc.equals(other.lawDesc))
			return false;
		if (lawName == null) {
			if (other.lawName != null)
				return false;
		} else if (!lawName.equals(other.lawName))
			return false;
		return true;
	}

	public String getLawId() {
		return lawId;
	}

	public String getLawName() {
		return lawName;
	}

	public String getLawDesc() {
		return lawDesc;
	}

	public void setLawId(String lawId) {
		this.lawId = lawId;
	}

	public void setLawName(String lawName) {
		this.lawName = lawName;
	}

	public void setLawDesc(String lawDesc) {
		this.lawDesc = lawDesc;
	}
	
	
}
