package com.compli.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;



import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



import com.compli.db.bean.UserAccess;
import com.compli.db.bean.UserBean;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.UserAccessDao;
import com.compli.db.dao.UserDao;
import com.compli.exceptions.SessionExpiredException;import com.google.gson.Gson;


public class UserManager {
	private UserDao userDao;
	private UserAccessDao accessDao;
	private static List<String>CommonAccess
			= new ArrayList<String>(Arrays.asList("dashboardLnk","dashboard.serviaritycount","dashboard.monthlyOverview","dashboard.complianceOverview","dashboard.snapshot",
					"activitiesLnk","activities.download","activities.changestatus","activities.downloadFiles"
					));
	public UserManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		userDao = (UserDao) ctx.getBean("udao");
		accessDao = (UserAccessDao) ctx.getBean("userAccessDao");
	}
	public List<UserBean> getAllUser(){
		return this.userDao.getAllData();
	}
	
	
	public List<String>getUserAccess(String authToken){
		List<String>userAccess = new ArrayList<String>();
		String userRole = "";
		String userId = "";
		try{
			UserBean userBean = AuthorisationManager.getUserCatche(authToken);
			userRole = userBean.getUserTypeId();
			userId = userBean.getUserId();
			}catch(SessionExpiredException | ExecutionException e){
				
			}
		
		switch (userRole) {
		case "master":
			List<String> accessList = new ArrayList<String>(Arrays.asList("settingsLnk","masteractivityLnk","statsLnk"));
			userAccess = accessList;
			break;
		case "cManager":
			userAccess = CommonAccess;
			userAccess.add("reportLnk");
			userAccess.add("repositoriesLnk");
			break;
		case "ArTechUser":
			userAccess = CommonAccess;
			userAccess.add("reportLnk");
			userAccess.add("repositoriesLnk");
			break;
		case "cOwner":
			userAccess = CommonAccess;
			break;
		case "sManager":
			userAccess = CommonAccess;
			userAccess.add("reportLnk");
			userAccess.add("repositoriesLnk");
			break;
		case "demoUser":
			userAccess = getUsersAccess(userId);
			break;			
		default:
			break;
		}
		return userAccess;
	}
	
	public List<String> getUsersAccess(String userId){
		UserAccess userAccess = this.accessDao.getUsersAccess(userId);
		List<String>access = new ArrayList<String>();
		if(userAccess == null){
			access.addAll(CommonAccess);
			access.add("reportsLnk");
		}else{
			com.compli.bean.access.UserAccess userAcces = new Gson().fromJson(userAccess.getAccesscontrol(), com.compli.bean.access.UserAccess.class);
			if(userAcces.isActivitiesLnk()){
				access.add("activitiesLnk");
			}
			if(userAcces.isDashboardLnk()){
				access.add("dashboardLnk");
			}
			if(userAcces.isReportLnk()){
				access.add("reportLnk");
			}
			if(userAcces.isRepositoriesLnk()){
				access.add("repositoriesLnk");
			}
			if(userAcces.getActivities().isChangestatus()){
				access.add("activities.changestatus");
			}
			if(userAcces.getActivities().isDownloadFiles()){
				access.add("activities.downloadFiles");
			}
			if(userAcces.getActivities().isUploadFile()){
				access.add("activities.uploadFiles");
			}
			if(userAcces.getDashboard().isComplianceOverview()){
				access.add("dashboard.complianceOverview");
			}
			if(userAcces.getDashboard().isMonthlyOverview()){
				access.add("dashboard.monthlyOverview");
			}
			if(userAcces.getDashboard().isServiaritycount()){
				access.add("dashboard.serviaritycount");
			}
			if(userAcces.getDashboard().isSnapshot()){
				access.add("dashboard.snapshot");
			}
		}
		return access;
	}
	
	public List<com.compli.bean.access.UserAccess> getAllUserAccess(){
		List<UserAccess> allUserAccesses = this.accessDao.getAllUserAccess();
		List<com.compli.bean.access.UserAccess> allUserAccessesResp = new ArrayList<com.compli.bean.access.UserAccess>();
		allUserAccesses.forEach(allUserAccess->{
			com.compli.bean.access.UserAccess userAccess = new com.compli.bean.access.UserAccess();
			if (allUserAccess.getAccesscontrol() != null){
				userAccess = new Gson().fromJson(allUserAccess.getAccesscontrol(), com.compli.bean.access.UserAccess.class);
			}else{
				userAccess = new com.compli.bean.access.UserAccess();
			}
			userAccess.setFirstname(allUserAccess.getFirstname());
			userAccess.setLastname(allUserAccess.getLastname());
			userAccess.setUserId(allUserAccess.getUserId());
			allUserAccessesResp.add(userAccess);
		});
		return allUserAccessesResp;
	}
	
	public boolean updateUserAccess(com.compli.bean.access.UserAccess userAccess){
		UserAccess access = new UserAccess();
		access.setUserId(userAccess.getUserId());
		access.setAccesscontrol(new Gson().toJson(userAccess));
		return this.accessDao.updateUserAccess(access);
	}
}
