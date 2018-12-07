package com.compli.db.bean;

public class Pair {
	String keyfordata,val;
	
	public Pair() {
	
	}
	public Pair(String keyfordata,String val) {
		this.keyfordata = keyfordata;
		this.val = val;
	}

	public String getKeyfordata() {
		return keyfordata;
	}

	public String getVal() {
		return val;
	}

	public void setKeyfordata(String keyfordata) {
		this.keyfordata = keyfordata;
	}

	public void setVal(String val) {
		this.val = val;
	}
	
	
}
