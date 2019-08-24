package com.compli.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Authorised{
	public enum ROLE{ALL,ADMIN,CUSTOMER,MASTER}
	public ROLE role();
}