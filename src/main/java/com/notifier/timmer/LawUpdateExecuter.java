package com.notifier.timmer;

import java.util.Date;
import java.util.TimerTask;

import com.compli.managers.ActivityManager;

public class LawUpdateExecuter extends TimerTask{

	@Override
	public void run() {
		ActivityManager activityManager = new ActivityManager();
		System.out.println("Updating activities.........."+new Date());
		activityManager.updateActivities();
	}
	
}
