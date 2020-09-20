package com.compli.managers;

import com.compli.bean.SettingsBean;
import com.compli.bean.notification.*;
import com.compli.db.bean.UserBean;
import com.compli.db.dao.NotificationDao;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class NotificationManager {

	NotificationDao notificationDao;
	static Cache<String, Object> notificationCache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES).build();

	ApplicationContext ctx;
	public NotificationManager(){
		this.ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.notificationDao = (NotificationDao) ctx.getBean("notificationDao");

	}

	public void markRead(String userId){
		this.notificationDao.markAllReadForUser(userId);
	}

	public List<String> getNotification(){
		List<String> notificationList = new ArrayList<String>();
		notificationList.add("You are running trial version, <a href=''>click here</a> for paid version");
		return notificationList;
	}

	public List<Notification> getNotifications(String userId, int from, int size){
		return this.notificationDao.getNotification(userId,from,size);
	}

	public EmailLog getEmailLogs(int from, int size) throws ExecutionException {
		List<EmailLogBean> emailLogBeans = this.notificationDao.getEmailLogs(from, size);
		Object emailLog = notificationCache.get("email_logs", () -> {
			long count = this.notificationDao.getCountOfEmailLog();
			EmailLog emailLogs = new EmailLog(emailLogBeans,count);
			return emailLogs;
		});
		EmailLog mailLogs = (EmailLog)emailLog;
		mailLogs.setData(emailLogBeans);
		return mailLogs;
	}

	public boolean saveNotification(Notification notification){
		Date expirationDate = getExpirationDate(notification.getExpiry());
		int notificationId = this.notificationDao.saveNotification(notification,expirationDate);
		int cyear = Calendar.getInstance().get(Calendar.YEAR);
		return assigneNotificationForUserOfLocation(notification.getLocations(),cyear-1,notificationId);
	}

	public boolean saveNotification(NotificationByLaw notification){
		Date expirationDate = getExpirationDate(notification.getExpiry());
		Notification notificationForSave = new Notification();
		notificationForSave.setTitle(notification.getTitle());
		notificationForSave.setNotification(notification.getNotification());
		notificationForSave.setType(notification.getType());
		notificationForSave.setLocations(notification.getLocations());
		notificationForSave.setSeverity(notification.getSeverity());

		int notificationId = this.notificationDao.saveNotification(notificationForSave,expirationDate);
		int cyear = Calendar.getInstance().get(Calendar.YEAR);
		assignNotificationForUserOfLocationAndLaw(notification.getLawArea(),notification.getLocations(),cyear-1,notificationId,
				notification.getUserType(), notification.isAllLocation());

        List<EmailBean> users = this.notificationDao.getUserForNotification(notification.getLawArea(), notification.getLocations(), cyear - 1, notificationId,
                notification.getUserType(), notification.isAllLocation());

		List<EmailLogBean> emailLogBeans = new ArrayList<>();

        List<EmailBean> userUpdated = new ArrayList<>();
        if ("true".equals(SettingsManager.settingsBean.testingmode)){
            List<EmailBean> finalUserUpdated = userUpdated;
            users.forEach(emailBean -> {
                EmailBean emailBeanUpdated = new EmailBean();
                emailBeanUpdated.setUserType(emailBean.getUserType());
                emailBeanUpdated.setName(emailBean.getName());
                String email = null;
                if("cManager".equals(emailBean.getUserType())){
                    email = SettingsManager.getCOwnerEmail(emailBean.getEmailId());
                }else if("cOwner".equals(emailBean.getUserType())){
                    email = SettingsManager.getCOwnerEmail(emailBean.getEmailId());
                }else if("sManager".equals(emailBean.getUserType())){
                    email = SettingsManager.getCOwnerEmail(emailBean.getEmailId());
                }
                if(email!=null){
                    emailBeanUpdated.setEmailId(email);
                    finalUserUpdated.add(emailBeanUpdated);
                }
				emailLogBeans.add(new EmailLogBean(email+"/"+emailBean.getEmailId(), notification.getTitle(), notification.getNotification()));
            });
            userUpdated = finalUserUpdated;
        }else{
			users.forEach(emailBean -> {
				emailLogBeans.add(new EmailLogBean(emailBean.getEmailId(), notification.getTitle(), notification.getNotification()));
			});
			userUpdated = users;
        }
		if (notification.isSendEmail()){
			this.notificationDao.logEmail(emailLogBeans);
            //EmailManager.sendMailToMultipleUsers(userUpdated,notification.getTitle(), notification.getNotification(),"true".equals(SettingsManager.settingsBean.testingmode));
        }

		return true;
	}


	public boolean assigneNotificationForUserOfLocation(List<String>location,int year, int notificationId){
		return this.notificationDao.assigneNotificationForUserOfLocation(location,year,notificationId);
	}

	public boolean assignNotificationForUserOfLocationAndLaw(String lawId, List<String>location,int year, int notificationId, String userType, Boolean isAllLocation){
		return this.notificationDao.assignNotificationForUserOfLocationAndLaw(lawId, location, year, notificationId,userType,isAllLocation);
	}

	public int getCountOfUnreadMessageForUser(String userId){
		return this.notificationDao.getCountOfUnreadMessageForUser(userId);
	}

	public Date getExpirationDate(String expiry){
		Calendar cal = Calendar.getInstance();
		int currentYearr = cal.get(Calendar.YEAR);
		cal.set(Calendar.YEAR,currentYearr+1);
		return cal.getTime();
	}

	public List<NotificationData> getNotificationorForAdmin(){
		return this.notificationDao.getNotificationorForAdminList();
	}

	public void deleteNotification(int notificationId) {
		this.notificationDao.deleteNotification(notificationId);
	}

	public void cleanUpEmailsLog(int days){
		this.notificationDao.cleanUpEmailsLog(days);
	}
}
