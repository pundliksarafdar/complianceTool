package com.compli.bean.stats;

import java.io.Serializable;
import java.util.List;

public class StatsCount implements Serializable{
	public List<RiskIdCount> riskIdCounts;
	public EmailCounts emailCounts;
	public DriveData driveData;
	
	public DriveData getDriveData() {
		return driveData;
	}

	public void setDriveData(DriveData driveData) {
		this.driveData = driveData;
	}

	public List<RiskIdCount> getRiskIdCounts() {
		return riskIdCounts;
	}

	public void setRiskIdCounts(List<RiskIdCount> riskIdCounts) {
		this.riskIdCounts = riskIdCounts;
	}

	public EmailCounts getEmailCounts() {
		return emailCounts;
	}

	public void setEmailCounts(EmailCounts emailCounts) {
		this.emailCounts = emailCounts;
	}
	
	
}
