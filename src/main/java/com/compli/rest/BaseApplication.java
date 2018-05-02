package com.compli.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.compli.filter.AuthFIlter;

public class BaseApplication extends Application{
	private Set<Object> singletons = new HashSet<Object>();
    public BaseApplication() {
        singletons.add(new UsersRestApi());
        singletons.add(new QuestionairRestApi());
        singletons.add(new LocationRestApi());
        singletons.add(new DashboardRestApi());
        singletons.add(new ActivityRestApi());
    }
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
     
    @Override
    public Set<Class<?>> getClasses() {
    	HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(AuthFIlter.class);
        return classes;
    }
}
