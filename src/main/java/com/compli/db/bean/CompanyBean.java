package com.compli.db.bean;

import java.util.Date;

import com.compli.annotation.Table;
import com.compli.util.UUID;

@Table(tableName="company")
public class CompanyBean {
	String companyId = UUID.getUID();
	String name;
	String abbriviation;
	String function;
	boolean isDeleted = false;
	String createdBy;
	Date cratedOn;
	String modifiedBy;
	Date modifiedOn;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbriviation() {
		return abbriviation;
	}
	public void setAbbriviation(String abbriviation) {
		this.abbriviation = abbriviation;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
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
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	
	
}
