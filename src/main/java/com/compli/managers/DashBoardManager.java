package com.compli.managers;

import java.util.*;

import com.compli.bean.dashboard.DashboardMonthlyOverview;
import com.compli.bean.dashboard.DashboardMonthlyOverviewObject;
import com.compli.bean.dashboard.DashboardparamsBean;
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
	private String PENDING_DESCREPANCY = "pendingDescrepancy";
	private String COMPLIED = "complied";
	
	private String companyId; 
	 DashBoardDao dashBoardDao;
	 List<Map<String, Object>> activities;
	 String auth;
	 String userId;
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
		this.userId =  AuthorisationManager.cache.getIfPresent(auth).getUserId();
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
		this.userId =  AuthorisationManager.cache.getIfPresent(auth).getUserId();
	}
	
	public HashMap<String,Object> getDashBoardData(DashboardparamsBean dparam){
		HashMap<String, Integer> riskCountMap = getRiskCount(dparam);
		HashMap<String,Object> dashBoardData = new HashMap<String,Object>();
		dashBoardData.put("riskCount", riskCountMap);
		
		Map<String, Integer> compliceOverview = getComplianceOverview(dparam);
		dashBoardData.put("compliceOverview", compliceOverview);
		
		/*New method is at line no 70
		Map<Integer,Map<String, Integer>> compliceOverviewLast3Month = getComplianceOverviewLast3Month(dparam);
		dashBoardData.put("compliceOverviewLast3Month", compliceOverviewLast3Month);
		*/
		List<Map<String, Object>> data = dashBoardDao.getActivityLast3MonthQueryPerMonth(dparam, this.locationId, this.userId, this.companyId);
		DashboardMonthlyOverviewObject monthlyDashBoardData = getDashboardMonthlyOverview(data);
		dashBoardData.put("monthlyDashBoardData", monthlyDashBoardData);
		
		Map<String, Map<String, Integer>> complainceOverviewByLaw = getComplainceOverviewByLaw(dparam);
		dashBoardData.put("complainceOverviewByLaw", complainceOverviewByLaw);

		return dashBoardData;
	}
	
	public HashMap<String, Integer> getRiskCount(DashboardparamsBean dashboardparamsBean){
		HashMap<String, Integer> riskCountMap = new HashMap<String, Integer>();
		riskCountMap.put(HIGH, 0);
		riskCountMap.put(MEDIUM, 0);
		riskCountMap.put(LOW, 0);
		List<Map<String, Object>> counts;
		if(null == this.locationId){
			counts = this.dashBoardDao.getPendingActivityCoutForNext10Days(this.companyId,this.userId,this.isFullUser,dashboardparamsBean);
		}else{
			counts = this.dashBoardDao.getPendingActivityCoutForNext10Days(this.companyId,this.isFullUser,this.locationId);
		}
		for(Map<String, Object> count : counts){
			String riskId = ((String)count.get("riskId")).toLowerCase();
			int countN = Integer.parseInt((Long)count.get("count")+"");
			riskCountMap.put(riskId, countN);
		}
		return riskCountMap;
	}
	
	public Map<String,Integer> getComplianceOverview(DashboardparamsBean dparam){
		ActivityManager activityManager = new ActivityManager(this.auth);
		Map<String,Integer> complainceOverview = new HashMap<>();
		boolean isFullUser = true;
		if(isFullUser){
            Map<String, Object> counts = this.dashBoardDao.getComplianceOverview(this.companyId, this.locationId, this.userId,dparam);
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

	public DashboardMonthlyOverviewObject getDashboardMonthlyOverview(List<Map<String, Object>> overviewList){
		DashboardMonthlyOverviewObject object = new DashboardMonthlyOverviewObject();
		List<Map<String,Integer>> monthlyOverviewMap = new ArrayList<>();
		List<String>labels = new ArrayList<>();
		for(Map<String, Object> overviewData : overviewList){
			String dDate = overviewData.get("dDate").toString();
			String status = overviewData.get("activityStatus").toString();
			Long count = (Long)overviewData.get("count");

			int indx = 0;
			if ((indx = labels.indexOf(dDate)) == -1){
				labels.add(dDate);
				Map<String,Integer> statusMap = new HashMap<>();
				monthlyOverviewMap.add(statusMap);
			}
			//DDate variable is like Jan 16 , Feb 16 it can get repeat for same status so need to get count if already present and sum it with new count
			int indxL = labels.indexOf(dDate);
			Map<String,Integer> statusMap = monthlyOverviewMap.get(indxL);
            //DDate variable is like Jan 16 , Feb 16 it can get repeat for same status so need to get count if already present and sum it with new count
			int statusCount = statusMap.getOrDefault(status,0);
			statusMap.put(status, statusCount + count.intValue());
		}

		List<Integer>pendingL = new ArrayList<>();
		List<Integer>compliedL = new ArrayList<>();
		List<Integer>pendingReviewL = new ArrayList<>();

		monthlyOverviewMap.forEach(overviewM->{
			int pendingCompliance = overviewM.getOrDefault("pendingCompliance",0);
			int compliedInTime = overviewM.getOrDefault("compliedInTime",0);
			int compliedDelayed = overviewM.getOrDefault("compliedDelayed",0);
			int pendingReview = overviewM.getOrDefault("pendingReview",0);

			pendingReviewL.add(pendingReview);
			pendingL.add(pendingCompliance);
			compliedL.add(compliedInTime + compliedDelayed);
		});
		DashboardMonthlyOverview pendingReviewD = new DashboardMonthlyOverview("Pending for review",pendingReviewL);
		DashboardMonthlyOverview pendingLD = new DashboardMonthlyOverview("Pending compliance",pendingL);
		DashboardMonthlyOverview compliedLD = new DashboardMonthlyOverview("Complied",compliedL);
		ArrayList<DashboardMonthlyOverview> list = new ArrayList<DashboardMonthlyOverview>() {{
			add(pendingLD);
			add(pendingReviewD);
			add(compliedLD);
		}};
		object.setDashboardMonthlyOverviewList(list);
		object.setMonths(labels);
		return object;
	}
	
	/*Map<Integer,Map<String, Integer>> getComplianceOverviewLast3Month(DashboardparamsBean dparam){
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
			int monthCal = month-index;
			//if monthCal is 0 then its dec or if its -1 then its nov
			if(monthCal<=0){
				monthCal = monthCal == 0?12:11;
			}
			complianceOverviewForLast3Month.put(monthCal, complainceOverview);
		}
		
		ActivityManager activityManager = new ActivityManager(this.auth);
		List<Map<String, Object>> allActivity;
		String locationIdH = null==this.locationId?"all":this.locationId;
		allActivity = activityManager.getAllActivitiesWithDescriptionForCompanyLast3Months(this.companyId,month,this.userId,this.isFullUser,locationIdH);
		
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
	}*/
	
	public Map<String,Map<String,Integer>> getComplainceOverviewByLaw(DashboardparamsBean dparam){
		Map<String,Map<String,Integer>> complianceDetailByLaw = new HashMap<>();
		ActivityManager activityManager = new ActivityManager(this.auth);
		List<Map<String, Object>> allActivity;
		List<Map<String, Object>> overViewByLaw = dashBoardDao.getComplianceOverviewByLaw(this.userId, this.locationId, this.companyId, dparam);

		for(int i=0;i<overViewByLaw.size();i++){
			Map<String,Object> activity = overViewByLaw.get(i);
			String lawId = (String) activity.get("lawName");
			String activityStatus = (String) activity.get("activityStatus");
			Long count = (Long) activity.get("count");
			if(!complianceDetailByLaw.containsKey(lawId)){
				Map<String,Integer> complainceOverview = new HashMap<>();
				complianceDetailByLaw.put(lawId, complainceOverview);
			}
			complianceDetailByLaw.get(lawId).put(activityStatus,count.intValue());
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
