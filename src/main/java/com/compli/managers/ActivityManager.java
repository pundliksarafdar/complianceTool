package com.compli.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.DashBoardDao;
import com.notifier.emailbean.PendingForDiscrepancy;

public class ActivityManager {
	DashBoardDao dashBoardDao;
	ActivityDao activityDao;
	boolean isFullUser;
	String locationId;
	public ActivityManager(String auth) {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");		
		this.isFullUser = AuthorisationManager.cache.getIfPresent(auth).isFullUser();
	}
	
	public ActivityManager(String auth,String locationId) {
		this.locationId = locationId;
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");		
		this.isFullUser = AuthorisationManager.cache.getIfPresent(auth).isFullUser();
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId,this.isFullUser,true,false);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId,String locationId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId,this.isFullUser,locationId,false,true);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithRisk(String companyId,String riskId){
		List<Map<String, Object>> activities ;
		if(!"all".equals(riskId)){
			activities = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyWithRisk(companyId,riskId,this.isFullUser);
		}else{
			activities = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyWithRisk(companyId,"%",this.isFullUser);
		}
		return activities;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityAndLaw(String companyId,String status,String law){
		String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		List<String> monthsList = Arrays.asList(months);
		if(monthsList.contains(law)){
			String monthStr = (monthsList.indexOf(law)+1)+"";
			List<Map<String, Object>> allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthAndStatus(companyId,monthStr,status,true);
			return allActivity;
		}else{
			List<Map<String, Object>> allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByLawAndStatus(companyId,law,status,true);
			return allActivity;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverity(String companyId,String severity){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){	
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId,true,false,true);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompany(companyId,true,this.locationId,true,false);
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
					&& (isComplianceRejected!=null && "0".equals(isComplianceRejected.toString())) && "Pending compliance".equalsIgnoreCase(severity)){
				filteredActivity.add(activity);
			}else if((isComplainceDelayed!=null && "1".equals(isComplainceDelayed.toString())) && "Complied-Delayed".equalsIgnoreCase(severity)){
				filteredActivity.add(activity);
			}else if((isComplied!=null && "1".equals(isComplied.toString())) &&
					(isComplianceApproved!=null && "0".equals(isComplianceApproved.toString())) 
					&& (isComplianceRejected!=null && "0".equals(isComplianceRejected.toString())) && "Pending for review".equalsIgnoreCase(severity)){
				filteredActivity.add(activity);
			}else if((isComplied!=null && "1".equals(isComplied.toString()) && isComplainceDelayed!=null && "0".equals(isComplainceDelayed.toString())  
					&& isComplianceApproved!=null && "1".equals(isComplianceApproved.toString()) && isComplianceRejected!=null)
					&& "Complied- In time".equalsIgnoreCase(severity) ){
				filteredActivity.add(activity);
			}else if(isComplied!=null && "1".equals(isComplied.toString()) && activity.get("isProofRequired")!=null && "1".equals(activity.get("isProofRequired").toString())	&& "Pending for Discrepancy".equalsIgnoreCase(severity)){
				filteredActivity.add(activity);
			}
		}
		return filteredActivity;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityForMonth(String companyId,String severity,String month){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonth(companyId,month,true);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonth(companyId,month,true,this.locationId);
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
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyForMonth(String companyId,String month){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(companyId,month,true);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(companyId,month,true,this.locationId);
		}
		return allActivity;
	}
	//This function is for repository with tracker
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyForQuarter(String companyId,String month){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarterWithRejected(companyId,month,true);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarterWithRejected(companyId,month,true,this.locationId);
		}
		return allActivity;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityForYear(String companyId,String severity,String year){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYear(companyId,year,true);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYear(companyId,year,true,this.locationId);
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
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithSeverityForQuarter(String companyId,String severity,String quarter){
		List<Map<String, Object>> allActivity = null;
		if(this.locationId==null){
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,true,quarter);
		}else{
			allActivity = this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,true,quarter,this.locationId);
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
		boolean pendingDescrepancy,String remark,Date completionDate){
		if(isComplied){
			boolean isSuccess = this.dashBoardDao.changeActivityStatus(companyId,activityId, isComplied,remark);
			try{
				if(isSuccess){
					AlertsManager.deleteEventForActivity(activityId);
				}
			}catch(Exception e){e.printStackTrace();}
			return isSuccess;
		}else if(compliedInTime){
			boolean isSuccess = this.dashBoardDao.changeActivityStatusApproved(companyId, activityId,completionDate);
			AlertsManager.deleteEventForActivity(activityId);
			return isSuccess;
		}else if(compliedDelayed){
			 boolean isSuccess = this.dashBoardDao.changeActivityStatusComplainceDelayed(companyId, activityId,completionDate);
			 return isSuccess;
		}else if(pendingDescrepancy){
			PendingForDiscrepancy activity = activityDao.getActivityDataForMail(activityId);
			EmailManager.sendActivityPendingForDescripancy(activity);
			return this.dashBoardDao.changeActivityStatusPendingDecrepancy(companyId, activityId,remark);
		}else {
			return this.dashBoardDao.changeActivityStatusPendingComplied(companyId, activityId);
		}
	}
	
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,int monthCount,boolean isFullUser){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyLast3Months(companyId,monthCount,isFullUser);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,int monthCount,boolean isFullUser,String locationId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyLast3Months(companyId,monthCount,isFullUser,locationId);
	}
	
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
}
