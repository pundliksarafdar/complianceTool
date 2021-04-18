package com.compli.managers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.db.dao.*;
import com.compli.util.Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.ActivityUser;
import com.compli.bean.ChangeDateBean;
import com.compli.bean.CompanyBean;
import com.compli.db.bean.UserBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.UserCompanyBean;
import com.compli.db.bean.PeriodicityDateMasterBean;
import com.compli.util.Constants;
import com.compli.util.bean.ActivityAssignnmentBean;
import com.compli.util.datamigration.v2.DataBaseMigrationUtilV2UpdateDB;
import com.notifier.emailbean.PendingForDiscrepancy;

public class ActivityManager {
	DashBoardDao dashBoardDao;
	ActivityDao activityDao;
	PeriodicityDateMasterDao periodicityDateMasterDao;
	ActivityMasterDao activityMasterDao;
	UserCompanyDao userCompanyDao;
	boolean isFullUser;
	String locationId;
	private String userId;
	
	public ActivityManager(){
		ApplicationContext ctx = DaoManager.getApplicationContext();
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");		
		this.periodicityDateMasterDao = (PeriodicityDateMasterDao) ctx.getBean("periodicityDateDao");
		this.userCompanyDao = (UserCompanyDao) ctx.getBean("userCompanyDao");
		this.activityMasterDao = (ActivityMasterDao) ctx.getBean("activityMasterDao");
	}
	
	public ActivityManager(String auth) {
		ApplicationContext ctx = DaoManager.getApplicationContext();
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");		
		this.periodicityDateMasterDao = (PeriodicityDateMasterDao) ctx.getBean("periodicityDateDao");
		this.userCompanyDao = (UserCompanyDao) ctx.getBean("userCompanyDao");
		this.activityMasterDao = (ActivityMasterDao) ctx.getBean("acitvityMasterDao");
		this.isFullUser = AuthorisationManager.cache.getIfPresent(auth).isFullUser();
		this.userId  = AuthorisationManager.cache.getIfPresent(auth).getUserId();
		
	}
	
	public void setActivityUser(ActivityUser activityUser){
		this.activityDao.setActivityUser(activityUser);
	}
	
	public ActivityManager(String auth,String locationId) {
		this.locationId = locationId;
		ApplicationContext ctx = DaoManager.getApplicationContext();
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");		
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");
		this.activityMasterDao = (ActivityMasterDao) ctx.getBean("acitvityMasterDao");
		this.isFullUser = AuthorisationManager.cache.getIfPresent(auth).isFullUser();
		this.userId =  AuthorisationManager.cache.getIfPresent(auth).getUserId();
	}
	
	public boolean uploadActivities(){
		
		return false;
	}
	public List<UserBean> getUsersForActivity(String activityId){
		return this.activityDao.getUsersForActivity(activityId);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId,this.isFullUser,true,false,this.userId,"all");
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId,String locationId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId,this.isFullUser,true,false,this.userId,locationId);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithRisk(String companyId,String riskId){
		List<Map<String, Object>> activities ;
		if(!"all".equals(riskId)){
			activities = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyWithRisk(companyId,riskId,this.userId,this.isFullUser, this.locationId);
		}else{
			activities = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyWithRisk(companyId,"%",this.userId,this.isFullUser, this.locationId);
		}
		return activities;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityAndLaw(String companyId,String status,String law){
		String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		List<String> monthsList = Arrays.asList(months);
		String location = this.locationId!=null?this.locationId:"all";
		if(monthsList.contains(law)){
			String monthStr = (monthsList.indexOf(law)+1)+"";
			List<Map<String, Object>> allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthAndStatus(companyId,monthStr,status,this.userId,true,location);
			return allActivity;
		}else{
			List<Map<String, Object>> allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByLawAndStatus(companyId,law,status,this.userId,true,location);
			return allActivity;
		}
	}
	
	//this function is only used by master user
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(String companyId,String month,String year){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(companyId, month, year);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverity(String companyId,List<String> severity){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){	
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId,true,false,true,this.userId,"all" );
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId,true,false,true,this.userId,this.locationId );
		}
		List<Map<String, Object>> filteredActivity = new ArrayList<Map<String,Object>>();
		int listSize = allActivity.size();
		for(int i=0;i<listSize;i++){
			Map<String,Object> activity = allActivity.get(i);
			Object isComplied = activity.get("isComplied");
			Object  isComplianceApproved = activity.get("isComplianceApproved");
			Object isComplianceRejected = activity.get("isComplianceRejected");
			Object  isComplainceDelayed = activity.get("isComplainceDelayed");
			
			if((isComplied!=null && "0".equals(isComplied.toString())) &&
					(isComplianceApproved!=null && "0".equals(isComplianceApproved.toString())) 
					&& (isComplianceRejected!=null && "0".equals(isComplianceRejected.toString())) && severity.indexOf("Pending compliance")!=-1){
				filteredActivity.add(activity);
			}else if((isComplainceDelayed!=null && "1".equals(isComplainceDelayed.toString())) && severity.indexOf("Complied-Delayed")!=-1){
				filteredActivity.add(activity);
			}else if((isComplied!=null && "1".equals(isComplied.toString())) &&
					(isComplianceApproved!=null && "0".equals(isComplianceApproved.toString())) &&
					(isComplainceDelayed!=null && "0".equals(isComplainceDelayed.toString())) 
					&& (isComplianceRejected!=null && "0".equals(isComplianceRejected.toString())) && severity.indexOf("Pending for review")!=-1){
				filteredActivity.add(activity);
			}else if((isComplied!=null && "1".equals(isComplied.toString()) && isComplainceDelayed!=null && "0".equals(isComplainceDelayed.toString())  
					&& isComplianceApproved!=null && "1".equals(isComplianceApproved.toString()) && isComplianceRejected!=null)
					&& severity.indexOf("Complied- In time")!=-1 ){
				filteredActivity.add(activity);
			}else if(isComplied!=null && "1".equals(isComplied.toString()) && activity.get("isProofRequired")!=null && "1".equals(activity.get("isProofRequired").toString())	&& severity.indexOf("Pending for Discrepancy")!=-1 ){
				filteredActivity.add(activity);
			}
		}
		return filteredActivity;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityForMonth(String companyId,String severity,String month){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthAndYear(companyId,month,null,this.userId,true,"all");
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthAndYear(companyId,month,null,this.userId,true,this.locationId);
		}
		List<Map<String, Object>> filteredActivity = new ArrayList<Map<String,Object>>();
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			if((activity.get("isComplied")!=null && "0".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString())) && "Pending compliance".equalsIgnoreCase(severity)){
				//complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplainceDelayed")!=null && "1".equals(activity.get("isComplainceDelayed").toString())) && "Complied-Delayed".equalsIgnoreCase(severity)){
				//complainceOverview.put(COMPLIANCE_DELAYED, complainceOverview.get(COMPLIANCE_DELAYED)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString())) && "Pending for review".equalsIgnoreCase(severity)){
				//complainceOverview.put(COMPLAINCE_REVEIW, complainceOverview.get(COMPLAINCE_REVEIW)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString()) && activity.get("isComplainceDelayed")!=null && "0".equals(activity.get("isComplainceDelayed").toString())  
					&& activity.get("isComplianceApproved")!=null && "1".equals(activity.get("isComplianceApproved").toString()) && activity.get("isComplianceRejected")!=null)
					&& "Complied- In time".equalsIgnoreCase(severity) ){
				//complainceOverview.put(COMPLAINCE_INTIME, complainceOverview.get(COMPLAINCE_INTIME)+1);
				filteredActivity.add(activity);
			}
		}
		return filteredActivity;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityForMonthAndYear(String companyId,String severity,String month,String year){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthAndYear(companyId,month,year,this.userId,true,"all");
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthAndYear(companyId,month,year,this.userId,true,this.locationId);
		}
		List<Map<String, Object>> filteredActivity = new ArrayList<Map<String,Object>>();
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			if((activity.get("isComplied")!=null && "0".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString())) && "Pending compliance".equalsIgnoreCase(severity)){
				//complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplainceDelayed")!=null && "1".equals(activity.get("isComplainceDelayed").toString())) && "Complied-Delayed".equalsIgnoreCase(severity)){
				//complainceOverview.put(COMPLIANCE_DELAYED, complainceOverview.get(COMPLIANCE_DELAYED)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString())) && "Pending for review".equalsIgnoreCase(severity)){
				//complainceOverview.put(COMPLAINCE_REVEIW, complainceOverview.get(COMPLAINCE_REVEIW)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString()) && activity.get("isComplainceDelayed")!=null && "0".equals(activity.get("isComplainceDelayed").toString())  
					&& activity.get("isComplianceApproved")!=null && "1".equals(activity.get("isComplianceApproved").toString()) && activity.get("isComplianceRejected")!=null)
					&& "Complied- In time".equalsIgnoreCase(severity) ){
				//complainceOverview.put(COMPLAINCE_INTIME, complainceOverview.get(COMPLAINCE_INTIME)+1);
				filteredActivity.add(activity);
			}
		}
		return filteredActivity;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyForYear(String companyId,String year){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYearWithRejected(companyId,year,true,"all",this.userId);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYearWithRejected(companyId,year,true,this.locationId,this.userId);
		}
		return allActivity;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyForMonth(String companyId,String month){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(companyId,month,null,true,"all",this.userId);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(companyId,month,null,true,this.locationId,this.userId);
		}
		return allActivity;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyForMonthAndYear(String companyId,String month,String year){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(companyId,month,year,true,"all",this.userId);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(companyId,month,year,true,this.locationId,this.userId);
		}
		return allActivity;
	}
	//This function is for repository with tracker
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyForQuarter(String companyId,String yearStr,String month){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarterWithRejected(companyId,yearStr,month,true,"all",this.userId);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarterWithRejected(companyId,yearStr, month,true,this.locationId,this.userId);
		}
		return allActivity;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityForYear(String companyId,String severity,String year){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYear(companyId,year,true,"all",this.userId);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYear(companyId,year,true,this.locationId,this.userId);
		}
		List<Map<String, Object>> filteredActivity = new ArrayList<Map<String,Object>>();
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			if((activity.get("isComplied")!=null && "0".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString())) && "Pending compliance".equalsIgnoreCase(severity)){
				//complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplainceDelayed")!=null && "1".equals(activity.get("isComplainceDelayed").toString())) && "Complied-Delayed".equalsIgnoreCase(severity)){
				//complainceOverview.put(COMPLIANCE_DELAYED, complainceOverview.get(COMPLIANCE_DELAYED)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString())) && "Pending for review".equalsIgnoreCase(severity)){
				//complainceOverview.put(COMPLAINCE_REVEIW, complainceOverview.get(COMPLAINCE_REVEIW)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString()) && activity.get("isComplainceDelayed")!=null && "0".equals(activity.get("isComplainceDelayed").toString())  
					&& activity.get("isComplianceApproved")!=null && "1".equals(activity.get("isComplianceApproved").toString()) && activity.get("isComplianceRejected")!=null)
					&& "Complied- In time".equalsIgnoreCase(severity) ){
				//complainceOverview.put(COMPLAINCE_INTIME, complainceOverview.get(COMPLAINCE_INTIME)+1);
				filteredActivity.add(activity);
			}
		}
		return filteredActivity;
	}

	//Year should be finnancial year e.g. 2019-20
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityForQuarter(String companyId, String yearStr, String severity,String quarter){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,yearStr,true,quarter,"all",this.userId);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,yearStr,true,quarter,this.locationId,this.userId);
		}
		List<Map<String, Object>> filteredActivity = new ArrayList<Map<String,Object>>();
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			if((activity.get("isComplied")!=null && "0".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString())) && "Pending compliance".equalsIgnoreCase(severity)){
				//complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplainceDelayed")!=null && "1".equals(activity.get("isComplainceDelayed").toString())) && "Complied-Delayed".equalsIgnoreCase(severity)){
				//complainceOverview.put(COMPLIANCE_DELAYED, complainceOverview.get(COMPLIANCE_DELAYED)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString())) && "Pending for review".equalsIgnoreCase(severity)){
				//complainceOverview.put(COMPLAINCE_REVEIW, complainceOverview.get(COMPLAINCE_REVEIW)+1);
				filteredActivity.add(activity);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString()) && activity.get("isComplainceDelayed")!=null && "0".equals(activity.get("isComplainceDelayed").toString())  
					&& activity.get("isComplianceApproved")!=null && "1".equals(activity.get("isComplianceApproved").toString()) && activity.get("isComplianceRejected")!=null)
					&& "Complied- In time".equalsIgnoreCase(severity) ){
				//complainceOverview.put(COMPLAINCE_INTIME, complainceOverview.get(COMPLAINCE_INTIME)+1);
				filteredActivity.add(activity);
			}
		}
		return filteredActivity;
	}
	
public boolean changeActivityStatus(String companyId,String activityId,boolean isComplied,boolean pendingComplied,boolean compliedInTime,boolean compliedDelayed,
		boolean pendingDescrepancy,boolean isNotDue,String remark,Date completionDate){
		//First check if user is demo user if user is demo user check if acitvity is complied in time or delayed by date entered
		if (!this.isFullUser){
			List<ActivityMasterBean> activities = this.activityMasterDao.getActivityMasterDataById(activityId);
			ActivityMasterBean bean = activities.get(0);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date dueDate = null;
			try{
				dueDate = dateFormat.parse(bean.getPeriodicityDateId());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			boolean isDelayed = Util.isDateAfter(dueDate,completionDate);
			if(!isDelayed){
				boolean isSuccess = this.dashBoardDao.changeActivityStatusApproved(companyId, activityId, completionDate);
				AlertsManager.deleteEventForActivity(activityId);
				return isSuccess;
			}else{
				boolean isSuccess = this.dashBoardDao.changeActivityStatusComplainceDelayed(companyId, activityId, completionDate);
				AlertsManager.deleteEventForActivity(activityId);
				return isSuccess;
			}
		}else {
			if (isComplied) {
				boolean isSuccess = this.dashBoardDao.changeActivityStatus(companyId, activityId, isComplied, remark, Constants.PENDING_REVIEW);
				try {
					if (isSuccess) {
						AlertsManager.deleteEventForActivity(activityId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return isSuccess;
			} else if (compliedInTime) {
				boolean isSuccess = this.dashBoardDao.changeActivityStatusApproved(companyId, activityId, completionDate);
				AlertsManager.deleteEventForActivity(activityId);
				return isSuccess;
			} else if (compliedDelayed) {
				boolean isSuccess = this.dashBoardDao.changeActivityStatusComplainceDelayed(companyId, activityId, completionDate);
				return isSuccess;
			} else if (pendingDescrepancy) {
				PendingForDiscrepancy activity = activityDao.getActivityDataForMail(activityId);
				EmailManager.sendActivityPendingForDescripancy(activity);
				return this.dashBoardDao.changeActivityStatusPendingDecrepancy(companyId, activityId, remark);
			} else if (isNotDue) {
				return this.dashBoardDao.changeActivityStatusRejected(companyId, activityId);
			} else {
				return this.dashBoardDao.changeActivityStatusPendingComplied(companyId, activityId);
			}
		}
	}
	
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,int monthCount,String userId,boolean isFullUser,String locationId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyLast3Months(companyId,monthCount,userId,isFullUser,locationId);
	}
	
	/*public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,int monthCount,boolean isFullUser,String locationId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyLast3Months(companyId,monthCount,isFullUser,locationId);
	}*/
	
	public boolean requestToReopen(String activityId,String companyId){
		PendingForDiscrepancy activity = activityDao.getActivityDataForArTechMail(activityId);
		EmailManager.sendActivityRequestForReopen(activity);
		this.activityDao.reopenActivity(activityId,companyId);
		return true;
	}

	public boolean changeToOpen(String activityId, String companyId) {
		this.activityDao.changeToOpen(activityId,companyId);
		return true;
	}
	
	public void changeDate(ChangeDateBean changeDateBean){
		PeriodicityDateMasterBean pDateBean = new PeriodicityDateMasterBean(changeDateBean.getDate(), changeDateBean.getDate()); 
		//Insert periodicityDate before adding change date
		try{this.periodicityDateMasterDao.addPeriodicityMasterForUpload(pDateBean);}catch(Exception e){System.out.println("Value is already present.");}
		this.activityDao.updatePeriodicityDateOfActivity(changeDateBean.getActivityId(), changeDateBean.getDate());
	}
	
	public void addUserForActivity(List<ActivityAssignnmentBean> activityAssignnmentBeans){
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.saveActivityAssignment(activityAssignnmentBeans);	
	}
	
	public void removeUserForActivity(ActivityAssignnmentBean activityAssignnmentBean){
		List<ActivityAssignnmentBean> activityAssignnmentBeans = new ArrayList<ActivityAssignnmentBean>();
		activityAssignnmentBeans.add(activityAssignnmentBean);
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.removeActivityAssignment(activityAssignnmentBeans);	
	}
	
	public void removeUserFromCompany(String userId,String companyId){
		this.activityDao.removeUserForCompany(userId,companyId);
	}
	
	public void addUserToCompany(String userId,String companyId){
		UserCompanyBean companyBean = new UserCompanyBean();
		companyBean.setCompanyId(companyId);
		companyBean.setUserId(userId);
		boolean isSucess = this.userCompanyDao.addUserCompanyForUpload(companyBean);
	}
	
	public void updateActivities(){
		this.activityDao.updateActivityRisk( "medium","high");
		this.activityDao.updateActivityRisk("low", "medium");		
	}
	
	public void getActivityForMonthAndYear(List<String>seviarity,int year,int month){
		
	}
	
	private Map<String,String>LSQIDS = new HashMap<String, String>(){{
		put("labourLaw", "Labour Law");
		put("indirecttax", "Indirect Tax");
		put("directTax", "Direct Tax");
		put("corporateLaw", "Corporate Law");
	}} ;
	public void updateUserAccess(List<String> userIds,String fromDate,List<String> lawIds){
		List<String>laws = new ArrayList<String>();
		
		for(String lawId:lawIds){
			laws.add(LSQIDS.get(lawId));
		}
		for(String userId:userIds){
			this.activityDao.updateUserAccess(userId, fromDate, laws);	
		}		
	}
}
