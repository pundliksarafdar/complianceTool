package com.compli.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.dao.DashBoardDao;

public class ReportsManager {
	
	private String HIGH = "high";
	private  String MEDIUM = "medium";
	private String LOW = "low";
	
	private String COMPLIED = "complied";
	private String OPEN = "open";
	
	DashBoardDao dashBoardDao;
	public ReportsManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");		
	}
	
	public HashMap<String,Object> getReportsObject(String companyId,String month){
		 HashMap<String,Object> reportsMap = new HashMap<>();
		 List<Map<String, Object>> activities = getAllActivitiesWithDescriptionForCompanyByMonth(companyId, month);
		 Map<String, Map<String, Integer>> riskCount = getComplainceOverviewByRisk(activities);
		 
		 reportsMap.put("activities", activities);
		 reportsMap.put("riskCount", riskCount);
		 return reportsMap;
	}
	
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId,String month){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonth(companyId,month);
	}
	
	public HashMap<String, Integer> getRiskCount(List<Map<String, Object>> activities){
		HashMap<String, Integer> riskCountMap = new HashMap<String, Integer>();
		riskCountMap.put(HIGH, 0);
		riskCountMap.put(MEDIUM, 0);
		riskCountMap.put(LOW, 0);
		for(int i=0;i<activities.size();i++){
			Map<String, Object> activity = activities.get(i);
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
	
	public Map<String,Map<String,Integer>> getComplainceOverviewByRisk(List<Map<String, Object>> allActivity){
		Map<String,Map<String,Integer>> complianceDetailByLaw = new HashMap<>();
		ActivityManager activityManager = new ActivityManager();
		{
		/*********************************************************/
		Map<String,Integer> complainceOverview = new HashMap<>();
		complainceOverview.put(COMPLIED, 0);
		complainceOverview.put(OPEN, 0);
		complianceDetailByLaw.put("Low", complainceOverview);
		
		complainceOverview = new HashMap<>();
		complainceOverview.put(COMPLIED, 0);
		complainceOverview.put(OPEN, 0);
		complianceDetailByLaw.put("Medium", complainceOverview);
		
		complainceOverview = new HashMap<>();
		complainceOverview.put(COMPLIED, 0);
		complainceOverview.put(OPEN, 0);
		complianceDetailByLaw.put("High", complainceOverview);
		/******************************************************/
		}
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			String riskDes = (String) activity.get("riskDes");
			/*
			if(!complianceDetailByLaw.containsKey(riskDes)){
				Map<String,Integer> complainceOverview = new HashMap<>();
				complainceOverview.put(COMPLIED, 0);
				complainceOverview.put(OPEN, 0);
				complianceDetailByLaw.put(riskDes, complainceOverview);
			}
			*/
			Map<String,Integer>  complainceOverview = complianceDetailByLaw.get(riskDes);
			if(null==activity.get("isComplied") || "false".equals(activity.get("isComplied").toString())){
				complainceOverview.put(OPEN, complainceOverview.get(OPEN)+1);
			}else if(null!=activity.get("isComplied") && "true".equals(activity.get("isComplied").toString())){
				complainceOverview.put(COMPLIED, complainceOverview.get(COMPLIED)+1);
			}
		}
		
		return complianceDetailByLaw;
	}
}
