package com.compli.bean;

import com.google.gson.Gson;

public class SettingScheduleOwnerBean{
	int cmDelay;
	int coDelay;
	boolean sendToCm = true;
	boolean sendToCo = true;
	
	public SettingScheduleOwnerBean(){
		
	}
	
	public SettingScheduleOwnerBean(int cmDelay,int coDelay,boolean sendToCm,boolean sendToCo) {
		this.cmDelay = cmDelay;
		this.coDelay = coDelay;
		this.sendToCm = sendToCm;
		this.sendToCo = sendToCo;
	}
	
	
	public int getCmDelay() {
		return cmDelay;
	}


	public int getCoDelay() {
		return coDelay;
	}


	public void setCmDelay(int cmDelay) {
		this.cmDelay = cmDelay;
	}


	public void setCoDelay(int coDelay) {
		this.coDelay = coDelay;
	}


	public boolean isSendToCm() {
		return sendToCm;
	}
	public boolean isSendToCo() {
		return sendToCo;
	}
	public void setSendToCm(boolean sendToCm) {
		this.sendToCm = sendToCm;
	}
	public void setSendToCo(boolean sendToCo) {
		this.sendToCo = sendToCo;
	}
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
