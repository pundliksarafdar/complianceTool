package com.notifier.timmer;

import java.util.TimerTask;

import com.compli.managers.AlertsManager;

public class Executer extends TimerTask{

	@Override
	public void run() {
		AlertsManager manager = new AlertsManager();
		manager.SendAlerts();
	}
	
}
