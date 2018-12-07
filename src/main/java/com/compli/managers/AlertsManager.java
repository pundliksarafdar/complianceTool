package com.compli.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.SettingsBean;
import com.compli.bean.SettingsScheduleBean;
import com.compli.db.dao.ActivityDao;
import com.notifier.emailbean.PendingActivitiesForMail;
import com.notifier.emailbean.PendingComplainceBean;

public class AlertsManager {
	private ActivityDao activityDao;
	Integer cMDue,arTechDue,cODue,sMDue;
	SettingsBean settingsBean;
	enum USER_TYPE{CM,CO};
	enum REMINDER_TYPE{FIRTS_REMINDER,SECOND_REMINDER,FOLLOWUP};
	public AlertsManager() {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");
		this.settingsBean = SettingsManager.getStaticSettings();
	}
	
	public void SendAlerts(){
		Map<String,List<PendingActivitiesForMail>> activitiesForMails10 = getActivitiesFor10();
		SendMailForPending(activitiesForMails10,REMINDER_TYPE.FIRTS_REMINDER);
		
		Map<String,List<PendingActivitiesForMail>> activitiesForMails5 = getActivitiesFor5();
		SendMailForPending(activitiesForMails5,REMINDER_TYPE.SECOND_REMINDER);
		
		Map<String,List<PendingActivitiesForMail>> activitiesForMailsFollowup = getActivitiesForFollowUp();
		SendMailForPending(activitiesForMailsFollowup,REMINDER_TYPE.FOLLOWUP);
	}
	
	public void SendMailForPending(Map<String,List<PendingActivitiesForMail>> activitiesForMails,REMINDER_TYPE rType){
		List<PendingActivitiesForMail> activities = activitiesForMails.get("cManager");
		List<PendingComplainceBean> complainceBeans = formatObject(activities);
		for(PendingComplainceBean pendingComplainceBean:complainceBeans){
			boolean isEnabled = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CM);
			if(isEnabled){
				String emailToSend = SettingsManager.getCManagerEmail(pendingComplainceBean.getEmail());
				EmailManager.sendMailForReminder(pendingComplainceBean, emailToSend);
			}
		}
		
		List<PendingActivitiesForMail> activitiescOwner = activitiesForMails.get("cOwner");
		List<PendingComplainceBean> complainceBeanscOwner = formatObject(activitiescOwner);
		for(PendingComplainceBean pendingComplainceBean:complainceBeanscOwner){
			boolean isEnabled = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CO);
			if(isEnabled){
				String emailToSend = SettingsManager.getCManagerEmail(pendingComplainceBean.getEmail());
				EmailManager.sendMailForReminder(pendingComplainceBean, emailToSend);
			}
		}
		
		List<PendingActivitiesForMail> activitiescOwnerFollowUp = activitiesForMails.get("cOwner");
		List<PendingActivitiesForMail> activitiescManagerFollowup = activitiesForMails.get("cManager");
		List<PendingComplainceBean> complainceBeanscOwnerFollowup = formatObject(activitiescOwnerFollowUp);
		complainceBeanscOwnerFollowup.addAll(formatObject(activitiescManagerFollowup));
		for(PendingComplainceBean pendingComplainceBean:complainceBeanscOwnerFollowup){
			boolean isEnabledCO = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CO);
			if(isEnabledCO){
				String emailToSend = SettingsManager.getCOwnerEmail(pendingComplainceBean.getEmail());
				EmailManager.sendMailForReminder(pendingComplainceBean, emailToSend);
			}
			
			boolean isEnabledCM = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CM);
			if(isEnabledCM){
				String emailToSend = SettingsManager.getCManagerEmail(pendingComplainceBean.getEmail());
				EmailManager.sendMailForReminder(pendingComplainceBean, emailToSend);
			}
		}
	}
	
	private List<PendingComplainceBean> formatObject(List<PendingActivitiesForMail> activities){
		List<PendingComplainceBean> complainceBeans = new ArrayList<PendingComplainceBean>();
		for(PendingActivitiesForMail activity:activities){
			PendingComplainceBean pendingComplainceBean = new PendingComplainceBean();
			pendingComplainceBean.setEmail(activity.getEmail());
			pendingComplainceBean.setCompanyId(activity.getCompanyId());
			int index = complainceBeans.indexOf(pendingComplainceBean);
			if(index==-1){
				List<PendingActivitiesForMail> pendingEmail = new ArrayList<PendingActivitiesForMail>();
				pendingEmail.add(activity);
				pendingComplainceBean.setPendingEmail(pendingEmail);
				complainceBeans.add(pendingComplainceBean);
			}else{
				complainceBeans.get(index).getPendingEmail().add(activity);
			}
		}
		return complainceBeans;
	}
	
	/*public Map<String,List<PendingActivitiesForMail>> setDelaysAndGetActivities(){
		Map<String,List<PendingActivitiesForMail>> activities = new HashMap<String, List<PendingActivitiesForMail>>();
		SettingsManager settingsManager = new SettingsManager();
		SettingsBean settingsBean = settingsManager.getStaticSettings();
		String cManagerDueDate = settingsBean.getcManagerDueDate();
		String arTechDueDate = settingsBean.getArTechUserDueDate();
		String cOwnerDueDate = settingsBean.getcOwnerDueDate();
		String sManagerDueDate = settingsBean.getsManagerDueDate();
		
		cMDue = cManagerDueDate!=null?Integer.parseInt(cManagerDueDate):-1;
		arTechDue = arTechDueDate!=null?Integer.parseInt(arTechDueDate):-1;
		cODue = cOwnerDueDate!=null?Integer.parseInt(cOwnerDueDate):-1;
		sMDue = sManagerDueDate!=null?Integer.parseInt(sManagerDueDate):-1;
		
		if(cMDue>0){
			List<PendingActivitiesForMail> cManagerActivities = this.activityDao.getOlderActivitiesForMail(cMDue,"cManager");
			activities.put("cManager", cManagerActivities);
		}
		if(cODue>0){
			List<PendingActivitiesForMail> cOwnerActivities = this.activityDao.getOlderActivitiesForMail(cMDue,"cOwner");
			activities.put("cOwner", cOwnerActivities);
		}
		if(arTechDue>0){
			List<PendingActivitiesForMail> arTechUserActivities = this.activityDao.getOlderActivitiesForMail(cMDue,"ArTechUser");
			activities.put("ArTechUser", arTechUserActivities);
		}
		if(sMDue>0){
			List<PendingActivitiesForMail> sManagerActivities = this.activityDao.getOlderActivitiesForMail(cMDue,"sManager");
			activities.put("sManager", sManagerActivities);
		}
		return activities;
	}
	*/
	
	public Map<String,List<PendingActivitiesForMail>> getActivitiesFor10(){
		Map<String,List<PendingActivitiesForMail>> activities = new HashMap<String, List<PendingActivitiesForMail>>();
		List<PendingActivitiesForMail> cManagerActivities = this.activityDao.getOlderActivitiesForMail(10,"cManager");
		activities.put("cManager", cManagerActivities);
		List<PendingActivitiesForMail> cOwnerActivities = this.activityDao.getOlderActivitiesForMail(10,"cOwner");
		activities.put("cOwner", cOwnerActivities);
		/*if(cMDue>0){
			List<PendingActivitiesForMail> cManagerActivities = this.activityDao.getOlderActivitiesForMail(10,"cManager");
			activities.put("cManager", cManagerActivities);
		}
		if(cODue>0){
			List<PendingActivitiesForMail> cOwnerActivities = this.activityDao.getOlderActivitiesForMail(10,"cOwner");
			activities.put("cOwner", cOwnerActivities);
		}*/
		return activities;
	}
	
	public Map<String,List<PendingActivitiesForMail>> getActivitiesFor5(){
		Map<String,List<PendingActivitiesForMail>> activities = new HashMap<String, List<PendingActivitiesForMail>>();
		List<PendingActivitiesForMail> cManagerActivities = this.activityDao.getOlderActivitiesForMail(5,"cManager");
		activities.put("cManager", cManagerActivities);
		List<PendingActivitiesForMail> cOwnerActivities = this.activityDao.getOlderActivitiesForMail(5,"cOwner");
		activities.put("cOwner", cOwnerActivities);
		return activities;
	}
	
	public Map<String,List<PendingActivitiesForMail>> getActivitiesForFollowUp(){
		Map<String,List<PendingActivitiesForMail>> activities = new HashMap<String, List<PendingActivitiesForMail>>();
		List<PendingActivitiesForMail> cManagerActivities = this.activityDao.getOlderActivitiesForMail(-2,"cManager");
		activities.put("cManager", cManagerActivities);
		List<PendingActivitiesForMail> cOwnerActivities = this.activityDao.getOlderActivitiesForMail(-2,"cOwner");
		activities.put("cOwner", cOwnerActivities);
		return activities;
	}
	
	public static void main(String[] args) {
		AlertsManager alertsManager = new AlertsManager();
		alertsManager.SendAlerts();
	}
	
	public boolean isSendEnabled(String companyId,REMINDER_TYPE rType,USER_TYPE uType){
		SettingsScheduleBean scheduleBean = new SettingsScheduleBean();
		scheduleBean.setCompanyId(companyId);
		int index = this.settingsBean.getSettingsScheduleBean().indexOf(scheduleBean);
		if(index!=-1){
			if(rType.equals(REMINDER_TYPE.FIRTS_REMINDER) && uType.equals(uType.CM)){
				return this.settingsBean.getSettingsScheduleBean().get(index).getsOwnerFirstReminderBean().isSendToCm();
			}else if(rType.equals(REMINDER_TYPE.FIRTS_REMINDER) && uType.equals(uType.CO)){
				return this.settingsBean.getSettingsScheduleBean().get(index).getsOwnerFirstReminderBean().isSendToCo();
			}else 
			//Second reminder	
			if(rType.equals(REMINDER_TYPE.SECOND_REMINDER) && uType.equals(uType.CM)){
				return this.settingsBean.getSettingsScheduleBean().get(index).getsOwnerFirstReminderBean().isSendToCm();
			}else if(rType.equals(REMINDER_TYPE.SECOND_REMINDER) && uType.equals(uType.CO)){
				return this.settingsBean.getSettingsScheduleBean().get(index).getsOwnerFirstReminderBean().isSendToCo();
			}else 
				//Followup
			if(rType.equals(REMINDER_TYPE.FOLLOWUP) && uType.equals(uType.CM)){
				return this.settingsBean.getSettingsScheduleBean().get(index).getsOwnerFollowUpReminderBean().isSendToCm();
			}else if(rType.equals(REMINDER_TYPE.FOLLOWUP) && uType.equals(uType.CO)){
					return this.settingsBean.getSettingsScheduleBean().get(index).getsOwnerFollowUpReminderBean().isSendToCo();
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
	

}
