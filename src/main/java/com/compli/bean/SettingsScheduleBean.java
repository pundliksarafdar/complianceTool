package com.compli.bean;

import com.google.gson.Gson;

public class SettingsScheduleBean {
	String companyName;
	String companyId;
	SettingScheduleOwnerBean sOwnerFirstReminderBean = new SettingScheduleOwnerBean(0, 10, false, true);
	SettingScheduleOwnerBean sOwnerSecondReminderBean = new SettingScheduleOwnerBean(0, 5, false, true);
	SettingScheduleOwnerBean sOwnerFollowUpReminderBean = new SettingScheduleOwnerBean(0, 10, true, true);
	
	public String getCompanyName() {
		return companyName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public SettingScheduleOwnerBean getsOwnerFirstReminderBean() {
		return sOwnerFirstReminderBean;
	}
	public SettingScheduleOwnerBean getsOwnerSecondReminderBean() {
		return sOwnerSecondReminderBean;
	}
	public SettingScheduleOwnerBean getsOwnerFollowUpReminderBean() {
		return sOwnerFollowUpReminderBean;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public void setsOwnerFirstReminderBean(
			SettingScheduleOwnerBean sOwnerFirstReminderBean) {
		this.sOwnerFirstReminderBean = sOwnerFirstReminderBean;
	}
	public void setsOwnerSecondReminderBean(
			SettingScheduleOwnerBean sOwnerSecondReminderBean) {
		this.sOwnerSecondReminderBean = sOwnerSecondReminderBean;
	}
	public void setsOwnerFollowUpReminderBean(
			SettingScheduleOwnerBean sOwnerFollowUpReminderBean) {
		this.sOwnerFollowUpReminderBean = sOwnerFollowUpReminderBean;
	}		
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.companyId.equals(((SettingsScheduleBean)obj).getCompanyId());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}


