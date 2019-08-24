package com.compli.bean.stats;

import java.io.Serializable;

public class EmailCounts implements Serializable{
	public int firstReminder;
	public int secondReminder;
	public  int followup;
	
	public int getFirstReminder() {
		return firstReminder;
	}
	public int getSecondReminder() {
		return secondReminder;
	}
	public int getFollowup() {
		return followup;
	}
	public void setFirstReminder(int firstReminder) {
		this.firstReminder = firstReminder;
	}
	public void setSecondReminder(int secondReminder) {
		this.secondReminder = secondReminder;
	}
	public void setFollowup(int followup) {
		this.followup = followup;
	}
	
	
}
