package com.notifier.timmer;

import com.compli.managers.ActivityManager;
import com.compli.managers.NotificationManager;

import java.util.Date;
import java.util.TimerTask;

public class EmailLogCleanup extends TimerTask{

	@Override
	public void run() {
		NotificationManager notificationManager = new NotificationManager();
		System.out.println("Cleaning up older data"+new Date());
		notificationManager.cleanUpEmailsLog(5);
	}
}
