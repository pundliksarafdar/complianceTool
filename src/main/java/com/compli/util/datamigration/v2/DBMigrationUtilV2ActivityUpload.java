package com.compli.util.datamigration.v2;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.ActivityForAddNewActivity;
import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.CompanyDao;
import com.compli.util.Constants;

public class DBMigrationUtilV2ActivityUpload {
	private static int ASSIGNED_USER =10;
	
	static ActivityDao activityDao;
	static CompanyDao companyDao;
	static List<Map<String,Object>> allLocations;
	static List<CompanyBean> companies;
	public DBMigrationUtilV2ActivityUpload() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		activityDao = (ActivityDao) ctx.getBean("activityDao");
		companyDao = (CompanyDao)ctx.getBean("companyDao");
		companies = companyDao.getAllCompany();
	}
	
	public static void init(){
		new DBMigrationUtilV2ActivityUpload();
	}
	
	public static void createActivity(Sheet datatypeSheet,int activityCountStart,String companyId){
		List<ActivityBean> activityBeanSheet = new ArrayList<ActivityBean>();
		Iterator<Row> iterator = datatypeSheet.iterator();
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			
			Row currentRow = iterator.next();
			String activityId = activityCountStart+"";
			
			String assignedUser = currentRow.getCell(ASSIGNED_USER, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String remark = "";
			Date completionDate = null;
			
			ActivityBean  activityBean = new ActivityBean(activityId, companyId, remark, assignedUser, completionDate,false, false, false, false, false,Constants.PENDING_COMPLIE);
				
			activityBeanSheet.add(activityBean);
			activityCountStart++;
		}
		
		List<ActivityBean> activitiesFromDb = activityDao.getAllActivityData();
		List<ActivityBean> filterdActivities = activityBeanSheet.parallelStream().filter(activityBean->{
			return !activitiesFromDb.parallelStream().anyMatch(activityDb->{ 
				return activityDb.getActivityId().equals(activityBean.getActivityId()) &&
						activityDb.getCompanyId().equals(activityBean.getCompanyId());
			});
		}).collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateActivity(filterdActivities);
	}
	
	public static void createActivity(List<ActivityForAddNewActivity> activities,int activityCountStart,String companyId){
		List<ActivityBean> activityBeanSheet = new ArrayList<ActivityBean>();
		
		for (ActivityForAddNewActivity activity:activities) {
			String activityId = activityCountStart+"";
			
			String assignedUser = "";
			String remark = "";
			Date completionDate = null;
			
			ActivityBean  activityBean = new ActivityBean(activityId, companyId, remark, assignedUser, completionDate,false, false, false, false, false,Constants.PENDING_COMPLIE);
				
			activityBeanSheet.add(activityBean);
			activityCountStart++;
		}
		
		List<ActivityBean> activitiesFromDb = activityDao.getAllActivityData();
		List<ActivityBean> filterdActivities = activityBeanSheet.parallelStream().filter(activityBean->{
			return !activitiesFromDb.parallelStream().anyMatch(activityDb->{ 
				return activityDb.getActivityId().equals(activityBean.getActivityId()) &&
						activityDb.getCompanyId().equals(activityBean.getCompanyId());
			});
		}).collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateActivity(filterdActivities);
	}
}
