package com.compli.db.bean;

import java.util.Date;

import com.compli.annotation.Table;
import com.compli.util.UUID;

@Table(tableName="usercompany")
public class UserCompanyBean {
	String companyId;
	String userId;
	boolean isDeleted = false;
	String createdBy;
	Date cratedOn;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCratedOn() {
		return cratedOn;
	}
	public void setCratedOn(Date cratedOn) {
		this.cratedOn = cratedOn;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
