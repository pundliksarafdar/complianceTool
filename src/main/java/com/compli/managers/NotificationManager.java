package com.compli.managers;

import com.compli.bean.notification.Notification;
import com.compli.db.dao.NotificationDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

public class NotificationManager {
	NotificationDao notificationDao;

	ApplicationContext ctx;
	public NotificationManager(){
		this.ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
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

	public boolean saveNotification(Notification notification){
		Date expirationDate = getExpirationDate(notification.getExpiry());
		int notificationId = this.notificationDao.saveNotification(notification,expirationDate);
		int cyear = Calendar.getInstance().get(Calendar.YEAR);
		return assigneNotificationForUserOfLocation(notification.getLocations(),cyear-1,notificationId);
	}

	public boolean assigneNotificationForUserOfLocation(List<String>location,int year, int notificationId){
		return this.notificationDao.assigneNotificationForUserOfLocation(location,year,notificationId);
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
}
