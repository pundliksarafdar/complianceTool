package com.notifier.timmer;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;

public class RunSchedular {
	static long _24HOURTIME = 24*60*60*1000L;
	public static void startSchedular(){
		Timer timer = new Timer();
		timer.schedule(new Executer(), getStartTime(),_24HOURTIME);
		timer.schedule(new LawUpdateExecuter(),getStartTime(), _24HOURTIME);
	}
	
	public static Date getStartTime(){
		Date startTime;
		Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		instance.set(Calendar.HOUR_OF_DAY, 22);
		instance.set(Calendar.MINUTE, 00);
		startTime = instance.getTime();
		return startTime;
	}
}
