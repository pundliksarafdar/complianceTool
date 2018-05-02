package com.compli.managers;

import java.util.ArrayList;
import java.util.List;

public class NotificationManager {
	public List<String> getNotification(){
		List<String> notificationList = new ArrayList<String>();
		notificationList.add("You are running trial version, <a href=''>click here</a> for paid version");
		return notificationList;
	}
}
