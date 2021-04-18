package com.compli.managers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoManager {
    private static ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
    public static ApplicationContext getApplicationContext(){
        return ctx;
    }
}
