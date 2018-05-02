package com.compli.managers;

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
	private String PENDING_REVEIW = "pendingReview";
	private String PENDING_DELAYED = "delayedCompliance";
	private String PENDING_INTIME = "intimeCompliance";
	
	private String companyId; 
	 DashBoardDao dashBoardDao;
	 List<Map<String, Object>> activities;
	public DashBoardManager(String companyId) {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.activities = dashBoardDao.getAllActivitiesForCompany(companyId);
		this.companyId = companyId;
	}
	
	public HashMap<String,Object> getDashBoardData(){
		HashMap<String, Integer> riskCountMap = getRiskCount();
		HashMap<String,Object> dashBoardData = new HashMap<String,Object>();
		dashBoardData.put("riskCount", riskCountMap);
		
		Map<String, Integer> compliceOverview = getComplianceOverview();
		dashBoardData.put("compliceOverview", compliceOverview);
		
		Map<String, Map<String, Integer>> complainceOverviewByLaw = getComplainceOverviewByLaw();
		dashBoardData.put("complainceOverviewByLaw", complainceOverviewByLaw);
		
		return dashBoardData;
	}
	
	public HashMap<String, Integer> getRiskCount(){
		HashMap<String, Integer> riskCountMap = new HashMap<String, Integer>();
		riskCountMap.put(HIGH, 0);
		riskCountMap.put(MEDIUM, 0);
		riskCountMap.put(LOW, 0);
		for(int i=0;i<this.activities.size();i++){
			Map<String, Object> activity = this.activities.get(i);
			System.out.println(activity);
			String riskId = (String) activity.get("riskId");
			if(HIGH.equals(riskId)){
				riskCountMap.put(HIGH,riskCountMap.get(HIGH)+1);
			}else if(MEDIUM.equals(riskId)){
				riskCountMap.put(MEDIUM,riskCountMap.get(MEDIUM)+1);
			}else if(LOW.equals(riskId)){
				riskCountMap.put(LOW,riskCountMap.get(LOW)+1);
			}
			
		}
		return riskCountMap;
	}
	
	public Map<String,Integer> getComplianceOverview(){
		Map<String,Integer> complainceOverview = new HashMap<>();
		complainceOverview.put(PENDING_COMPLIANCE, 0);
		complainceOverview.put(PENDING_DELAYED, 0);
		complainceOverview.put(PENDING_INTIME, 0);
		complainceOverview.put(PENDING_REVEIW, 0);
		ActivityManager activityManager = new ActivityManager();
		List<Map<String, Object>> allActivity = activityManager.getAllActivitiesWithDescriptionForCompany(this.companyId);
		
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			if(null==activity.get("isComplied") || "false".equals(activity.get("isComplied").toString())){
				complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
			}else if(activity.get("isComplianceApproved")==null && activity.get("isComplianceRejected")==null){
				complainceOverview.put(PENDING_REVEIW, complainceOverview.get(PENDING_REVEIW)+1);
			}
		}
		return complainceOverview;
	}
	
	public Map<String,Map<String,Integer>> getComplainceOverviewByLaw(){
		Map<String,Map<String,Integer>> complianceDetailByLaw = new HashMap<>();
		ActivityManager activityManager = new ActivityManager();
		List<Map<String, Object>> allActivity = activityManager.getAllActivitiesWithDescriptionForCompany(this.companyId);
		
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			String lawName = (String) activity.get("lawName");
			
			if(!complianceDetailByLaw.containsKey(lawName)){
				Map<String,Integer> complainceOverview = new HashMap<>();
				complainceOverview.put(PENDING_COMPLIANCE, 0);
				complainceOverview.put(PENDING_DELAYED, 0);
				complainceOverview.put(PENDING_INTIME, 0);
				complainceOverview.put(PENDING_REVEIW, 0);
				
				complianceDetailByLaw.put(lawName, complainceOverview);
			}
			
			Map<String,Integer>  complainceOverview = complianceDetailByLaw.get(lawName);
			if(null==activity.get("isComplied") || "false".equals(activity.get("isComplied").toString())){
				complainceOverview.put(PENDING_COMPLIANCE, complainceOverview.get(PENDING_COMPLIANCE)+1);
			}else if(activity.get("isComplianceApproved")==null && activity.get("isComplianceRejected")==null){
				complainceOverview.put(PENDING_REVEIW, complainceOverview.get(PENDING_REVEIW)+1);
			}
		}
		
		return complianceDetailByLaw;
	}
}
