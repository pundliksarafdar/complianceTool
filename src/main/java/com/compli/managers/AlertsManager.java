package com.compli.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.SettingsBean;
import com.compli.bean.SettingsScheduleBean;
import com.compli.db.bean.CompanyBean;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.CompanyDao;
import com.compli.services.GoogleServices;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.notifier.builder.EventBuilder;
import com.notifier.emailbean.PendingActivitiesForMail;
import com.notifier.emailbean.PendingComplainceBean;

public class AlertsManager {
	private ActivityDao activityDao;
	private CompanyDao companyDao;
	Integer cMDue,arTechDue,cODue,sMDue;
	SettingsBean settingsBean;
	enum USER_TYPE{CM,CO,ARTEC};
	enum REMINDER_TYPE{FIRTS_REMINDER,SECOND_REMINDER,FOLLOWUP};
	public static String CALENDAR_ID = "primary";
	Map<String, String> allCompanies;
	public AlertsManager() {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");
		this.companyDao = (CompanyDao) ctx.getBean("companyDao");
		this.settingsBean = SettingsManager.getStaticSettings();
		//Loading allCOmpany bean to get companyname agains companyId
		this.allCompanies = formCompanyMap(this.companyDao.getAllCompany());
	}
	
	public void sendCalendarEvents(){
		Map<String,List<PendingActivitiesForMail>> activitiesForEvent10 = getActivitiesFor10();
		sendEvents(activitiesForEvent10);
	}
	
	public void sendInitialCalendarEvents(String googleId){
		Map<String,List<PendingActivitiesForMail>> activitiesForEvent10 = getActivitiesTill10(googleId);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				sendEvents(activitiesForEvent10);		
			}
		});
		thread.start();
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
		if(rType.equals(REMINDER_TYPE.FIRTS_REMINDER)){
			List<PendingActivitiesForMail> activities = activitiesForMails.get("cManager");
			List<PendingComplainceBean> complainceBeans = formatObject(activities);
			for(PendingComplainceBean pendingComplainceBean:complainceBeans){
				boolean isEnabled = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CM);
				if(isEnabled){
					String emailToSend = SettingsManager.getCManagerEmail(pendingComplainceBean.getEmail());
					EmailManager.sendMailForReminder("Reminder 1 : Pending for complaince ",pendingComplainceBean, emailToSend,pendingComplainceBean.getEmail());
				}
			}
		}
		
		if(rType.equals(REMINDER_TYPE.SECOND_REMINDER)){
			List<PendingActivitiesForMail> activitiescOwner = activitiesForMails.get("cOwner");
			List<PendingComplainceBean> complainceBeanscOwner = formatObject(activitiescOwner);
			for(PendingComplainceBean pendingComplainceBean:complainceBeanscOwner){
				boolean isEnabled = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CO);
				if(isEnabled){
					String emailToSend = SettingsManager.getCManagerEmail(pendingComplainceBean.getEmail());
					EmailManager.sendMailForReminder("Reminder 2 : Pending for complaince ",pendingComplainceBean, emailToSend, pendingComplainceBean.getEmail());
				}
			}
			//Send mail to ArTech user default no need of check
			List<PendingActivitiesForMail> activitiesArTech = activitiesForMails.get("ArTechUser");
			List<PendingComplainceBean> complainceBeansArTech = formatObject(activitiesArTech);
			for(PendingComplainceBean pendingComplainceBean:complainceBeansArTech){
					String emailToSend = SettingsManager.getArtecEmail(pendingComplainceBean.getEmail());
					EmailManager.sendMailForReminder("Reminder 2 : Pending for complaince ",pendingComplainceBean, emailToSend, pendingComplainceBean.getEmail());
			}
		}
		
		if(rType.equals(REMINDER_TYPE.FOLLOWUP)){
			List<PendingActivitiesForMail> activitiescOwnerFollowUp = activitiesForMails.get("cOwner");
			List<PendingActivitiesForMail> activitiescManagerFollowup = activitiesForMails.get("cManager");
			List<PendingComplainceBean> complainceBeanscOwnerFollowup = formatObject(activitiescOwnerFollowUp);
			//Combind cOwner and cManager emails
			complainceBeanscOwnerFollowup.addAll(formatObject(activitiescManagerFollowup));
			for(PendingComplainceBean pendingComplainceBean:complainceBeanscOwnerFollowup){
				boolean isEnabledCO = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CO);
				if(isEnabledCO){
					String emailToSend = SettingsManager.getCOwnerEmail(pendingComplainceBean.getEmail());
					EmailManager.sendMailForReminder("Followup : Pending for complaince",pendingComplainceBean, emailToSend, pendingComplainceBean.getEmail());
				}
				
				boolean isEnabledCM = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CM);
				if(isEnabledCM){
					String emailToSend = SettingsManager.getCManagerEmail(pendingComplainceBean.getEmail());
					EmailManager.sendMailForReminder("Followup : Pending for complaince",pendingComplainceBean, emailToSend, pendingComplainceBean.getEmail());
				}
			}
		}
	}
	
	public void sendEvents(Map<String,List<PendingActivitiesForMail>> activitiesForMails){
		REMINDER_TYPE rType = REMINDER_TYPE.FOLLOWUP;
		List<PendingActivitiesForMail> activitiescOwnerFollowUp = activitiesForMails.get("cOwner");
		List<PendingActivitiesForMail> activitiescManagerFollowup = activitiesForMails.get("cManager");
		List<PendingComplainceBean> complainceBeanscOwnerFollowup = formatObject(activitiescOwnerFollowUp);
		complainceBeanscOwnerFollowup.addAll(formatObject(activitiescManagerFollowup));
		List<String>attendees = new ArrayList<String>();
		for(PendingComplainceBean pendingComplainceBean:complainceBeanscOwnerFollowup){
			boolean isEnabledCO = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CO);
			if(pendingComplainceBean.getGoogleId()!=null){
				if(isEnabledCO){
					String emailToSend = SettingsManager.getCOwnerEmail(pendingComplainceBean.getGoogleId());
					attendees.add(emailToSend);
				}
			}
			boolean isEnabledCM = isSendEnabled(pendingComplainceBean.getCompanyId(), rType, USER_TYPE.CM);
			if(pendingComplainceBean.getGoogleId()!=null){
				if(isEnabledCM){
					String emailToSend = SettingsManager.getCManagerEmail(pendingComplainceBean.getGoogleId());
					attendees.add(emailToSend);
				}
			}
			List<PendingActivitiesForMail> activitiesPending = pendingComplainceBean.getPendingEmail();
			for(PendingActivitiesForMail activitiesForEvt:activitiesPending){
				try {
					sendGoogleEvent(activitiesForEvt.getDesc1(), "Pending activity : "+activitiesForEvt.getActivityName(), activitiesForEvt.getDueDate(), activitiesForEvt.getActivityId(), attendees);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private List<PendingComplainceBean> formatObject(List<PendingActivitiesForMail> activities){
		List<PendingComplainceBean> complainceBeans = new ArrayList<PendingComplainceBean>();
		for(PendingActivitiesForMail activity:activities){
			PendingComplainceBean pendingComplainceBean = new PendingComplainceBean();
			pendingComplainceBean.setEmail(activity.getEmail());
			pendingComplainceBean.setGoogleId(activity.getGoogleId());
			pendingComplainceBean.setCompanyName(this.allCompanies.get(activity.getCompanyId()));
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
	
	private Map<String,String>formCompanyMap(List<CompanyBean>allCompanies){
		Map<String,String> map = new HashMap<String, String>();
		allCompanies.forEach(company->{
			map.put(company.getCompanyId(), company.getName());
		});
		return map;
	}
	
	public Map<String,List<PendingActivitiesForMail>> getActivitiesFor10(){
		Map<String,List<PendingActivitiesForMail>> activities = new HashMap<String, List<PendingActivitiesForMail>>();
		List<PendingActivitiesForMail> cManagerActivities = this.activityDao.getOlderActivitiesForMail(10,"cManager");
		activities.put("cManager", cManagerActivities);
		List<PendingActivitiesForMail> cOwnerActivities = this.activityDao.getOlderActivitiesForMail(10,"cOwner");
		activities.put("cOwner", cOwnerActivities);
		List<PendingActivitiesForMail> arTechActivities = this.activityDao.getOlderActivitiesForMail(10,"ArTechUser");
		activities.put("ArTechUser", arTechActivities);
		return activities;
	}
	
	public Map<String,List<PendingActivitiesForMail>> getActivitiesTill10(String googleId){
		Map<String,List<PendingActivitiesForMail>> activities = new HashMap<String, List<PendingActivitiesForMail>>();
		List<PendingActivitiesForMail> cManagerActivities = this.activityDao.getOlderActivitiesForMailTillDays(10,"cManager",googleId);
		activities.put("cManager", cManagerActivities);
		List<PendingActivitiesForMail> cOwnerActivities = this.activityDao.getOlderActivitiesForMailTillDays(10,"cOwner",googleId);
		activities.put("cOwner", cOwnerActivities);
		return activities;
	}
	
	public Map<String,List<PendingActivitiesForMail>> getActivitiesFor5(){
		Map<String,List<PendingActivitiesForMail>> activities = new HashMap<String, List<PendingActivitiesForMail>>();
		List<PendingActivitiesForMail> cManagerActivities = this.activityDao.getOlderActivitiesForMail(4,"cManager");
		activities.put("cManager", cManagerActivities);
		List<PendingActivitiesForMail> cOwnerActivities = this.activityDao.getOlderActivitiesForMail(4,"cOwner");
		activities.put("cOwner", cOwnerActivities);
		List<PendingActivitiesForMail> arTechActivities = this.activityDao.getOlderActivitiesForMail(4,"ArTechUser");
		activities.put("ArTechUser", arTechActivities);
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
	
	public static void main(String[] args) throws IOException {
		AlertsManager alertsManager = new AlertsManager();
		alertsManager.SendAlerts();
		//sendGoogleEvent();
		/*deleteEvent("1234");
		Event evt = getEvent("1234");
		System.out.println(evt);*/
		
		//AlertsManager alertsManager = new AlertsManager();
		/*alertsManager.sendCalendarEvents();*/
		
		//deleteAllEvents();
		//alertsManager.sendInitialCalendarEvents("pundlikproject@gmail.com");
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
	
	public static void sendGoogleEvent(String activity,String summary,Date date,String activityId,List<String>attendees) throws IOException{
		Calendar calendarService = GoogleServices.getCalendarService();
		EventBuilder eventBuilder = new EventBuilder();
		date.setHours(9);
		date.setMinutes(30);
		Event event = eventBuilder.buildEvent(summary, activity, date, attendees, activityId);
		try{
			event = calendarService.events().insert(CALENDAR_ID, event).execute();
		}catch(Exception e){
			event = calendarService.events().update(CALENDAR_ID, event.getId(),event).execute();
		}
	}
	
	public static void deleteEvent(String eventId) throws IOException{
		Calendar calendarService = GoogleServices.getCalendarService();
		calendarService.events().delete(CALENDAR_ID, eventId).execute();
	}
	
	public static void deleteEventForActivity(String activityId) {
		try {
			deleteEvent(EventBuilder.getGoogleCalendarId(activityId));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void deleteAllEvents() throws IOException{
		Calendar calendarService = GoogleServices.getCalendarService();
		DateTime dateTime0 = new DateTime(1000000);
		 Events evts = calendarService.events().list(CALENDAR_ID).setMaxResults(1000).execute();
		List<Event> evs = evts.getItems();
		int idx = 0;
		int length = evs.size();
		System.out.println("Events length :"+length);
		for(Event evt:evs){
			System.out.println("Index : "+idx++);
			deleteEvent(evt.getId());
		}
	}
	
	public static Event getEvent(String eventId) throws IOException{
		Calendar calendarService = GoogleServices.getCalendarService();
		return calendarService.events().get(CALENDAR_ID, EventBuilder.getGoogleCalendarId(eventId)).execute();
	}

}
