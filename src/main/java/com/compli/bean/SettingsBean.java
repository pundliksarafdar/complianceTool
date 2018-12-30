package com.compli.bean;

import java.util.List;


public class SettingsBean {
	public String mailId,mailPass,smtpHost,smtpPort;
	public String testingmode,complainceManagerEmail,complainceOwnerEmail,superManagerEmail,arTechEmail;
	public String cManagerDueDate,arTechUserDueDate,cOwnerDueDate,sManagerDueDate;
	public List<SettingsScheduleBean> settingsScheduleBean;
	public String serverName;
	public String accessToken;
	public String refreshToken;
	public String expirationTime;
	
	
	public String getAccessToken() {
		return accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public String getExpirationTime() {
		return expirationTime;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public List<SettingsScheduleBean> getSettingsScheduleBean() {
		return settingsScheduleBean;
	}
	public void setSettingsScheduleBean(
			List<SettingsScheduleBean> settingsScheduleBean) {
		this.settingsScheduleBean = settingsScheduleBean;
	}
	public String getMailId() {
		return mailId;
	}
	public String getMailPass() {
		return mailPass;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public String getSmtpPort() {
		return smtpPort;
	}
	public String getTestingmode() {
		return testingmode;
	}
	public String getComplainceManagerEmail() {
		return complainceManagerEmail;
	}
	public String getComplainceOwnerEmail() {
		return complainceOwnerEmail;
	}
	public String getSuperManagerEmail() {
		return superManagerEmail;
	}
	public String getArTechEmail() {
		return arTechEmail;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public void setMailPass(String mailPass) {
		this.mailPass = mailPass;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	public void setTestingmode(String testingmode) {
		this.testingmode = testingmode;
	}
	public void setComplainceManagerEmail(String complainceManagerEmail) {
		this.complainceManagerEmail = complainceManagerEmail;
	}
	public void setComplainceOwnerEmail(String complainceOwnerEmail) {
		this.complainceOwnerEmail = complainceOwnerEmail;
	}
	public void setSuperManagerEmail(String superManagerEmail) {
		this.superManagerEmail = superManagerEmail;
	}
	public void setArTechEmail(String arTechEmail) {
		this.arTechEmail = arTechEmail;
	}
	public String getcManagerDueDate() {
		return cManagerDueDate;
	}
	public String getArTechUserDueDate() {
		return arTechUserDueDate;
	}
	public String getcOwnerDueDate() {
		return cOwnerDueDate;
	}
	public String getsManagerDueDate() {
		return sManagerDueDate;
	}
	public void setcManagerDueDate(String cManagerDueDate) {
		this.cManagerDueDate = cManagerDueDate;
	}
	public void setArTechUserDueDate(String arTechUserDueDate) {
		this.arTechUserDueDate = arTechUserDueDate;
	}
	public void setcOwnerDueDate(String cOwnerDueDate) {
		this.cOwnerDueDate = cOwnerDueDate;
	}
	public void setsManagerDueDate(String sManagerDueDate) {
		this.sManagerDueDate = sManagerDueDate;
	}
	
	
}
