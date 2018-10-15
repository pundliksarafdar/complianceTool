package com.compli.managers;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.dao.DashBoardDao;
import com.compli.db.dao.UserDao;

public class DashBoardManager {
	
	private String HIGH = "high";
	private  String MEDIUM = "medium";
	private String LOW = "low";
	
	private String PENDING_COMPLIANCE = "pendingCompliance";
	private String COMPLAINCE_REVEIW = "pendingReview";
	private String COMPLIANCE_DELAYED = "delayedCompliance";
	private String COMPLAINCE_INTIME = "intimeCompliance";
	private String COMPLIED = "complied";
	
	private String companyId; 
	 DashBoardDao dashBoardDao;
	 List<Map<String, Object>> activities;
	 String auth;
	 private boolean isFullUser;
	 private String locationId;
	 
	public DashBoardManager(String companyId,String auth) {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activities = dashBoardDao.getAllActivitiesForCompany(companyId);
		this.companyId = companyId;
		this.auth=auth;
		this.isFullUser = AuthorisationManager.cache.getIfPresent(auth).isFullUser();
	}
	
	public DashBoardManager(String companyId,String auth,String locationId) {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activities = dashBoardDao.getAllActivitiesForCompany(companyId);
		this.companyId = companyId;
		this.auth=auth;
		this.isFullUser = AuthorisationManager.cache.getIfPresent(auth).isFullUser();
		this.locationId = locationId;
	}
	
	public HashMap<String,Object> getDashBoardData(){
		HashMap<String, Integer> riskCountMap = getRiskCount();
		HashMap<String,Object> dashBoardData = new HashMap<String,Object>();
		dashBoardData.put("riskCount", riskCountMap);
		
		Map<String, Integer> compliceOverview = getComplianceOverview();
		dashBoardData.put("compliceOverview", compliceOverview);
		
		Map<Integer,Map<String, Integer>> compliceOverviewLast3Month = getComplianceOverviewLast3Month();
		dashBoardData.put("compliceOverviewLast3Month", compliceOverviewLast3Month);
		
		Map<String, Map<String, Integer>> complainceOverviewByLaw = getComplainceOverviewByLaw();
		dashBoardData.put("complainceOverviewByLaw", complainceOverviewByLaw);
		
		return dashBoardData;
	}
	
	public HashMap<String, Integer> getRiskCount(){
		HashMap<String, Integer> riskCountMap = new HashMap<String, Integer>();
		riskCountMap.put(HIGH, 0);
		riskCountMap.put(MEDIUM, 0);
		riskCountMap.put(LOW, 0);
		List<Map<String, Object>> counts;
		if(null == this.locationId){
			counts = this.dashBoardDao.getPendingActivityCoutForNext10Days(this.companyId,this.isFullUser);
		}else{
			counts = this.dashBoardDao.getPendingActivityCoutForNext10Days(this.companyId,this.isFullUser,this.locationId);
		}
		for(Map<String, Object> count : counts){
			String riskId = (String)count.get("riskId");
			int countN = Integer.parseInt((Long)count.get("count")+"");
			riskCountMap.put(riskId, countN);
		}
		return riskCountMap;
	}
	
	public Map<String,Integer> getComplianceOverview(){
		ActivityManager activityManager = new ActivityManager(this.auth);
		Map<String,Integer> complainceOverview = new HashMap<>();
		boolean isFullUser = true;
		if(isFullUser){
			Map<String, Object> counts = this.dashBoardDao.getComplianceOverview(this.companyId,this.locationId);
			complainceOverview.put(PENDING_COMPLIANCE, Integer.parseInt((counts.get(PENDING_COMPLIANCE)!=null?counts.get(PENDING_COMPLIANCE):0)+""));
			complainceOverview.put(COMPLIANCE_DELAYED, Integer.parseInt((counts.get(COMPLIANCE_DELAYED)!=null?counts.get(COMPLIANCE_DELAYED):0)+""));
			complainceOverview.put(COMPLAINCE_INTIME, Integer.parseInt((counts.get(COMPLAINCE_INTIME)!=null?counts.get(COMPLAINCE_INTIME):0)+""));
			complainceOverview.put(COMPLAINCE_REVEIW, Integer.parseInt((counts.get(COMPLAINCE_REVEIW)!=null?counts.get(COMPLAINCE_REVEIW):0)+""));
			return complainceOverview;
		}else{
		complainceOverview.put(PENDING_COMPLIANCE, 0);
		complainceOverview.put(COMPLIANCE_DELAYED, 0);
		complainceOverview.put(COMPLAINCE_INTIME, 0);
		complainceOverview.put(COMPLAINCE_REVEIW, 0);
		complainceOverview.put(COMPLIED, 0);
		
		List<Map<String, Object>> allActivity;
		if(null == this.locationId){
			 allActivity = activityManager.getAllActivitiesWithDescriptionForCompany(this.companyId);
		}else{
			 allActivity = activityManager.getAllActivitiesWithDescriptionForCompany(this.companyId,this.locationId);
		}
		//Making here
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			
			if((activity.get("isComplied")!=null && "0".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString()))){
				complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
			}else if(activity.get("isComplainceDelayed")!=null && "1".equals(activity.get("isComplainceDelayed").toString())){
				complainceOverview.put(COMPLIANCE_DELAYED, complainceOverview.get(COMPLIANCE_DELAYED)+1);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString()))){
				complainceOverview.put(COMPLAINCE_REVEIW, complainceOverview.get(COMPLAINCE_REVEIW)+1);
			}else if(activity.get("isComplied")!=null && activity.get("isComplainceDelayed")!=null && activity.get("isComplianceApproved")!=null && activity.get("isComplianceRejected")!=null ){
				complainceOverview.put(COMPLAINCE_INTIME, complainceOverview.get(COMPLAINCE_INTIME)+1);
			}
		}
		return complainceOverview;
		}
	}
	
	Map<Integer,Map<String, Integer>> getComplianceOverviewLast3Month(){
		
		//Month starts from 1
		int month = Calendar.getInstance().get(Calendar.MONTH)+1; 
		Map<Integer,Map<String, Integer>> complianceOverviewForLast3Month = new HashMap<>();
		
		for(int index=2;index>=0;index--){
			Map<String,Integer> complainceOverview = new HashMap<>();
			complainceOverview.put(PENDING_COMPLIANCE, 0);
			complainceOverview.put(COMPLIANCE_DELAYED, 0);
			complainceOverview.put(COMPLAINCE_INTIME, 0);
			complainceOverview.put(COMPLAINCE_REVEIW, 0);
			complainceOverview.put(COMPLIED, 0);
			complianceOverviewForLast3Month.put(month-index, complainceOverview);
		}
		
		ActivityManager activityManager = new ActivityManager(this.auth);
		List<Map<String, Object>> allActivity;
		if(null==this.locationId){
			 allActivity = activityManager.getAllActivitiesWithDescriptionForCompanyLast3Months(this.companyId,month,this.isFullUser);
		}else{
			allActivity = activityManager.getAllActivitiesWithDescriptionForCompanyLast3Months(this.companyId,month,this.isFullUser,this.locationId);
		}
		
		for(int i=0;i<allActivity.size();i++){
			Map<String,Integer> complainceOverview = complianceOverviewForLast3Month.get(Integer.parseInt(allActivity.get(i).get("dueMonth")+""));
			if(null==complainceOverview){
				continue;
			}
			Map<String,Object> activity = allActivity.get(i);
			if((activity.get("isComplied")!=null && "0".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString()))){
				complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
			}else if(activity.get("isComplainceDelayed")!=null && "1".equals(activity.get("isComplainceDelayed").toString())){
				complainceOverview.put(COMPLIANCE_DELAYED, complainceOverview.get(COMPLIANCE_DELAYED)+1);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString()))){
				complainceOverview.put(COMPLAINCE_REVEIW, complainceOverview.get(COMPLAINCE_REVEIW)+1);
			}else if(activity.get("isComplied")!=null && activity.get("isComplainceDelayed")!=null && activity.get("isComplianceApproved")!=null && activity.get("isComplianceRejected")!=null ){
				complainceOverview.put(COMPLAINCE_INTIME, complainceOverview.get(COMPLAINCE_INTIME)+1);
			}
		}
		return complianceOverviewForLast3Month;
	}
	
	public Map<String,Map<String,Integer>> getComplainceOverviewByLaw(){
		Map<String,Map<String,Integer>> complianceDetailByLaw = new HashMap<>();
		ActivityManager activityManager = new ActivityManager(this.auth);
		List<Map<String, Object>> allActivity;
		if(null == this.locationId){
			 allActivity = activityManager.getAllActivitiesWithDescriptionForCompany(this.companyId);
		}else{
			 allActivity = activityManager.getAllActivitiesWithDescriptionForCompany(this.companyId,this.locationId);
		}
		
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			String lawId = (String) activity.get("lawName");
			//lawId = getActivityType(lawId);
			
			if(!complianceDetailByLaw.containsKey(lawId)){
				Map<String,Integer> complainceOverview = new HashMap<>();
				complainceOverview.put(PENDING_COMPLIANCE, 0);
				complainceOverview.put(COMPLIANCE_DELAYED, 0);
				complainceOverview.put(COMPLAINCE_INTIME, 0);
				complainceOverview.put(COMPLAINCE_REVEIW, 0);
				
				complianceDetailByLaw.put(lawId, complainceOverview);
			}
			
			Map<String,Integer>  complainceOverview = complianceDetailByLaw.get(lawId);
			if((activity.get("isComplied")!=null && "0".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString()))){
				complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
			}else if(activity.get("isComplainceDelayed")!=null && "1".equals(activity.get("isComplainceDelayed").toString())){
				complainceOverview.put(COMPLIANCE_DELAYED, complainceOverview.get(COMPLIANCE_DELAYED)+1);
			}else if((activity.get("isComplied")!=null && "1".equals(activity.get("isComplied").toString())) &&
					(activity.get("isComplianceApproved")!=null && "0".equals(activity.get("isComplianceApproved").toString())) 
					&& (activity.get("isComplianceRejected")!=null && "0".equals(activity.get("isComplianceRejected").toString()))){
				complainceOverview.put(COMPLAINCE_REVEIW, complainceOverview.get(COMPLAINCE_REVEIW)+1);
			}else if(activity.get("isComplied")!=null && activity.get("isComplainceDelayed")!=null && activity.get("isComplianceApproved")!=null && activity.get("isComplianceRejected")!=null ){
				complainceOverview.put(COMPLAINCE_INTIME, complainceOverview.get(COMPLAINCE_INTIME)+1);
			}
		}
		
		return complianceDetailByLaw;
	}
	
	public static String getActivityType(String lawId){
		lawId = lawId.toLowerCase();
		if(lawId.startsWith("directtax")){
			return "Direct tax";
		}else if(lawId.startsWith("indirecttax")){
			return "Indirect tax";
		}else if(lawId.startsWith("labourlaw")){
			return "Labour law";
		}else if(lawId.startsWith("corporatelaw")){
			return "Corporate law";
		}else{
			return lawId;
		}
	}
}
