package com.compli.managers;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.dao.DashBoardDao;

public class ActivityManager {
	DashBoardDao dashBoardDao;
	public ActivityManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");		
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId);
	}
	
}
