package com.compli.db.bean.migration.v2;

import java.util.Date;

public class FilesBean {
	String activityId,companyId,documentName,dateOfSubmition,documentFile;
	Date createdOn;
	
	public FilesBean(String activityId, String companyId, String documentName,	String dateOfSubmition, String documentFile) {
		this.activityId = activityId;
		this.companyId = companyId;
		this.documentName = documentName;
		this.dateOfSubmition = dateOfSubmition;
		this.documentFile = documentFile;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}


	public String getActivityId() {
		return activityId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public String getDateOfSubmition() {
		return dateOfSubmition;
	}

	public String getDocumentFile() {
		return documentFile;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public void setDateOfSubmition(String dateOfSubmition) {
		this.dateOfSubmition = dateOfSubmition;
	}

	public void setDocumentFile(String documentFile) {
		this.documentFile = documentFile;
	}
	
	
	
}
