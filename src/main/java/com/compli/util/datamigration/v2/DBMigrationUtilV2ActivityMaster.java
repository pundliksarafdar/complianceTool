package com.compli.util.datamigration.v2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.db.bean.migration.v2.LawMasterBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.ActivityMasterDao;
import com.compli.db.dao.LawMasterDao;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.util.Util;

public class DBMigrationUtilV2ActivityMaster {
	private static int ID = 0;
	private static int COMPANY_NAME = 1;
	private static int COMPANY_ABBR = 2;
	private static int COMPANY_LOCATION = 3;
	private static int COMPLAINCE_AREA = 5;
	private static int ACTIVITY_NAME = 6;
	private static int ACTIVITY_DESC = 7;
	private static int LAW_DESC = 8;
	private static int RISK = 9;
	private static int CONSEQUENCES = 10;
	private static int DUE_DATE = 12;
	private static int PERODICITY = 13;
	
	private static List<String> COMPLAINCE_AREA_LABEL = new ArrayList<String>(){{
		add("");add("Direct Tax");add("Indirect Tax");add("Corporate Law");add("Labour Law");
	}};
	
	private static List<String> RISK_DESC = new ArrayList<String>(){{
		add("");add("low");add("high");add("medium");
	}};	
	
	static LawMasterDao lawMasterDao;
	static PeriodicityMasterDao periodicityMasterDao;
	static PeriodicityDateMasterDao periodicityDateMasterDao;
	static ActivityMasterDao activityMasterDao;
	
	static List<com.compli.db.bean.LawMasterBean> allLaw;
	static List<com.compli.db.bean.PeriodicityMasterBean> periodicityMasterBeans;
	static List<com.compli.db.bean.PeriodicityDateMasterBean> periodicityDateMasterBeans;
	
	public DBMigrationUtilV2ActivityMaster() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");;
		this.lawMasterDao = (LawMasterDao) ctx.getBean("lawMasterDao");
		this.periodicityMasterDao = (PeriodicityMasterDao) ctx.getBean("periodicityDao");
		this.periodicityDateMasterDao = (PeriodicityDateMasterDao) ctx.getBean("periodicityDateDao");
		this.activityMasterDao = (ActivityMasterDao) ctx.getBean("acitvityMasterDao");
		
		this.allLaw = this.lawMasterDao.getAllLaw();
		periodicityMasterBeans = this.periodicityMasterDao.getAllPeriodicty();
		periodicityDateMasterBeans = this.periodicityDateMasterDao.getAllPeriodictyDate();
	}
	
	public static void init(){
		new DBMigrationUtilV2ActivityMaster();
	}
	
	public static void createActivityMaster(Sheet datatypeSheet){
		List<ActivityMasterBean> activityBeans = new ArrayList<ActivityMasterBean>();
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
			String activityId = (int)currentRow.getCell(ID, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()+"";
			String companyName = currentRow.getCell(COMPANY_NAME, Row.CREATE_NULL_AS_BLANK).toString().trim();
			if(companyName.equals("")){
				continue;
			}
			String companyAbbr = currentRow.getCell(COMPANY_ABBR, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String companyLocation = currentRow.getCell(COMPANY_LOCATION, Row.CREATE_NULL_AS_BLANK).toString().trim();
			
			int complianceAreaNum = (int)currentRow.getCell(COMPLAINCE_AREA, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
			String compalinceArea = COMPLAINCE_AREA_LABEL.get(complianceAreaNum);
			
			String activityName = currentRow.getCell(ACTIVITY_NAME, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String activityDesc = currentRow.getCell(ACTIVITY_DESC, Row.CREATE_NULL_AS_BLANK).toString().trim();
			
			String lawDesc  = currentRow.getCell(LAW_DESC, Row.CREATE_NULL_AS_BLANK).toString().trim();
			if(lawDesc.equals("")){
				continue;
			}
			
			int riskInt = (int)currentRow.getCell(RISK, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
			String risk = RISK_DESC.get(riskInt);
			
			String consequences = currentRow.getCell(CONSEQUENCES, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String dueDate = currentRow.getCell(DUE_DATE, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String periodicity = currentRow.getCell(PERODICITY, Row.CREATE_NULL_AS_BLANK).toString().trim();
			
			//Get lawId here
			com.compli.db.bean.LawMasterBean lawMasterBean = new com.compli.db.bean.LawMasterBean(compalinceArea, lawDesc);
			int lawIndex = allLaw.indexOf(lawMasterBean);
			if(lawIndex==-1){
				System.out.println(lawMasterBean);
			}
			lawMasterBean = allLaw.get(lawIndex);
			String lawId = lawMasterBean.getLawId();
			
			//Get periodicityId
			com.compli.db.bean.PeriodicityMasterBean periodicityMasterBean = new com.compli.db.bean.PeriodicityMasterBean();
			periodicityMasterBean.setDescription(periodicity);
			int periodicityIndex = periodicityMasterBeans.indexOf(periodicityMasterBean);
			periodicityMasterBean = periodicityMasterBeans.get(periodicityIndex);
			String periodicityId = periodicityMasterBean.getPeriodicityId();
			//System.out.println(periodicityId);
			
			//Get periodicityDateId
			String periodicityDateId = Util.getPeriodicityDateId(dueDate);
			
			ActivityMasterBean activityBean = new ActivityMasterBean(activityId, companyName, companyAbbr, companyLocation, compalinceArea, activityName, activityDesc, lawDesc, risk, consequences, dueDate, periodicity, lawId, periodicityId, periodicityDateId);
			
			
			activityBeans.add(activityBean);
		}
		//All activity from db;
		List<ActivityMasterBean> activitiesDb = activityMasterDao.getAllActivityMasterData();
		
		List<ActivityMasterBean> filteredActivities = activityBeans.parallelStream().filter(activityBean->{
			return !activitiesDb.parallelStream().anyMatch(activityDb->{
				return activityDb.getActivityId().equals(activityBean.getActivityId());
			});
		}).collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateActivityMaster(filteredActivities);
		System.out.println(filteredActivities);
	}
}
