package com.compli.util;

public class UUID {
	public static String getUID(){
		//return UUID.getUID().replace("-", "").substring(0, 16);
		return java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 16);
	}
}
