package com.compli.managers;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.stats.RiskIdCount;
import com.compli.bean.stats.StatsCount;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.DashBoardDao;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.UserCompanyDao;

public class StatsManager {
	DashBoardDao dashBoardDao;
	ActivityDao activityDao;
	PeriodicityDateMasterDao periodicityDateMasterDao;
	UserCompanyDao userCompanyDao;
	
	public StatsManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");		
		this.periodicityDateMasterDao = (PeriodicityDateMasterDao) ctx.getBean("periodicityDateDao");
		this.userCompanyDao = (UserCompanyDao) ctx.getBean("userCompanyDao");
	}
	public StatsCount getCounts(){
		List<RiskIdCount> riskCounts = activityDao.getRiskUpdateCountForToday();
		StatsCount statsCount = new StatsCount();
		statsCount.setRiskIdCounts(riskCounts);
		return statsCount;
	}
}
