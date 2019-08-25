package com.compli.managers;

import java.io.IOException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.stats.DriveData;
import com.compli.bean.stats.RiskIdCount;
import com.compli.bean.stats.StatsCount;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.DashBoardDao;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.UserCompanyDao;
import com.compli.services.GoogleServices;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.About;

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
		statsCount.setDriveData(getDriveData());
		return statsCount;
	}
	
	public DriveData getDriveData(){
		DriveData driveData = new DriveData();
		try {
			Drive driveService = GoogleServices.getDriveService();
			About about = driveService.about().get().setFields("storageQuota").execute();
			long totalUsage = about.getStorageQuota().getUsageInDrive();
			long totalLimit = 	about.getStorageQuota().getLimit();
			System.out.println("totalUsage:"+totalUsage+"::::totalLimit"+totalLimit);
			driveData.setTotalSpace(totalLimit);
			driveData.setUsedSpace(totalUsage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return driveData;
	}
		
}
