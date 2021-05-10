package com.compli.managers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DaoManager {
    private static ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
    private static com.compli.db.dao.b2c.ActivityDao  actDaoB2c = (com.compli.db.dao.b2c.ActivityDao) getApplicationContext().getBean("activityDaoB2c");

    public static ApplicationContext getApplicationContext(){
        return ctx;
    }

    public static com.compli.db.dao.b2c.ActivityDao getApplicationDao(){
        return actDaoB2c;
    }
}
