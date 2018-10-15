package com.compli.db.bean.migration.v2;

public class UserCompanyBean {
	String userId,companyId;
	
	public UserCompanyBean() {
	
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((companyId == null) ? 0 : companyId.hashCode());
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
		UserCompanyBean other = (UserCompanyBean) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public UserCompanyBean(String userId, String companyId) {
		this.userId = userId;
		this.companyId = companyId;
	}

	public String getUserId() {
		return userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	
}
