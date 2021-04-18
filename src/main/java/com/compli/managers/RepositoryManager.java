package com.compli.managers;

import com.compli.bean.ActivityUser;
import com.compli.bean.ChangeDateBean;
import com.compli.db.bean.PeriodicityDateMasterBean;
import com.compli.db.bean.UserBean;
import com.compli.db.bean.migration.v2.UserCompanyBean;
import com.compli.db.dao.*;
import com.compli.util.Constants;
import com.compli.util.bean.ActivityAssignnmentBean;
import com.compli.util.datamigration.v2.DataBaseMigrationUtilV2UpdateDB;
import com.notifier.emailbean.PendingForDiscrepancy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;

public class RepositoryManager {
	DashBoardDao dashBoardDao;
	ActivityDao activityDao;
	PeriodicityDateMasterDao periodicityDateMasterDao;
	UserCompanyDao userCompanyDao;
	RepositoryDao repositoryDao;
	boolean isFullUser;
	String locationId;
	private String userId;

	public RepositoryManager() {
		ApplicationContext ctx = DaoManager.getApplicationContext();
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");
		this.periodicityDateMasterDao = (PeriodicityDateMasterDao) ctx.getBean("periodicityDateDao");
		this.userCompanyDao = (UserCompanyDao) ctx.getBean("userCompanyDao");
		this.repositoryDao = (RepositoryDao) ctx.getBean("repositoryDao");
	}

	public RepositoryManager(String auth) {
		ApplicationContext ctx = DaoManager.getApplicationContext();
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");
		this.periodicityDateMasterDao = (PeriodicityDateMasterDao) ctx.getBean("periodicityDateDao");
		this.userCompanyDao = (UserCompanyDao) ctx.getBean("userCompanyDao");
		this.repositoryDao = (RepositoryDao) ctx.getBean("repositoryDao");
		this.isFullUser = AuthorisationManager.cache.getIfPresent(auth).isFullUser();
		this.userId = AuthorisationManager.cache.getIfPresent(auth).getUserId();

	}

	public RepositoryManager(String auth, String locationId) {
		this.locationId = locationId;
		ApplicationContext ctx = DaoManager.getApplicationContext();
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");
		this.repositoryDao = (RepositoryDao) ctx.getBean("repositoryDao");
		this.isFullUser = AuthorisationManager.cache.getIfPresent(auth).isFullUser();
		this.userId = AuthorisationManager.cache.getIfPresent(auth).getUserId();
	}

	//Year should be finnancial year e.g. 2019-20
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(
			String companyId, boolean isFullUser, String yearStr, String quarter,List<String> activityStatus) {
		List<Map<String, Object>> allActivity = null;
		if (this.locationId == null) {
			allActivity = this.repositoryDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,yearStr, isFullUser, quarter, "all", this.userId, activityStatus);
		} else {
			allActivity = this.repositoryDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,yearStr, isFullUser, quarter, this.locationId, this.userId, activityStatus);
		}

		return allActivity;
	}


	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityForMonthAndYear(String companyId, List<String> activityStatus, String month, String year) {
		List<Map<String, Object>> allActivity = null;
		if (this.locationId == null) {
			allActivity = this.repositoryDao.getAllActivitiesWithDescriptionForCompanyByMonthAndYear(companyId, month, year, this.userId, true, "all",activityStatus);
		} else {
			allActivity = this.repositoryDao.getAllActivitiesWithDescriptionForCompanyByMonthAndYear(companyId, month, year, this.userId, true, this.locationId,activityStatus);
		}

		return allActivity;
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyForYear(String companyId,String year,List<String> activityStatus){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.repositoryDao.getAllActivitiesWithDescriptionForCompanyByYearWithRejected(companyId,year,true,"all",this.userId,activityStatus);
		}else{
			allActivity = this.repositoryDao.getAllActivitiesWithDescriptionForCompanyByYearWithRejected(companyId,year,true,this.locationId,this.userId,activityStatus);
		}
		return allActivity;
	}

}
