package com.compli.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.compli.exceptions.ApplicationExceptionMapper;
import com.compli.filter.AuthFIlter;
import com.notifier.timmer.RunSchedular;

public class BaseApplication extends Application{
	private Set<Object> singletons = new HashSet<Object>();
    public BaseApplication() {
        singletons.add(new UsersRestApi());
        singletons.add(new QuestionairRestApi());
        singletons.add(new LocationRestApi());
        singletons.add(new DashboardRestApi());
        singletons.add(new ActivityRestApi());
        singletons.add(new ReportRestApi());
        singletons.add(new FilesRestApi());
        singletons.add(new SettingsRestApi());
        singletons.add(new AlertsRestApi());
        singletons.add(new ApplicationExceptionMapper());
        
        RunSchedular.startSchedular();
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
