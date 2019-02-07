package com.compli.util.datamigration.v2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.db.bean.migration.v2.LawMasterBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.ActivityAssociationDao;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.ActivityMasterDao;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.LawMasterDao;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.util.Constants;
import com.compli.util.Util;

public class DBMigrationUtilV2Activity {
	private static int ID = 0;
	private static int COMPANY_NAME = 1;
	private static int COMPANY_ABBR = 2;
	private static int COMPLETION_DATE = 14;
	private static int COMPLAINCE_STATUS = 15;
	private static int ASSIGNED_USER =16;
	private static int REMARK = 17;
	private static int IS_ACTIVE = 22;
	
	static ActivityDao activityDao;
	static CompanyDao companyDao;
	static List<Map<String,Object>> allLocations;
	static List<CompanyBean> companies;
	public DBMigrationUtilV2Activity() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		activityDao = (ActivityDao) ctx.getBean("activityDao");
		companyDao = (CompanyDao)ctx.getBean("companyDao");
		companies = companyDao.getAllCompany();
	}
	
	public static void init(){
		new DBMigrationUtilV2Activity();
	}
	
	public static void createActivity(Sheet datatypeSheet){
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
			String activityId = (int)currentRow.getCell(ID, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()+"";
			String companyName = currentRow.getCell(COMPANY_NAME, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String isActive = currentRow.getCell(IS_ACTIVE, Row.CREATE_NULL_AS_BLANK).toString().trim();
			
			if(companyName.equals("")){
				continue;
			}
			String companyAbbr = currentRow.getCell(COMPANY_ABBR, Row.CREATE_NULL_AS_BLANK).toString().trim();
			int statusNum = (int)currentRow.getCell(COMPLAINCE_STATUS, Row.CREATE_NULL_AS_BLANK).getNumericCellValue();
			String assignedUser = currentRow.getCell(ASSIGNED_USER, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String remark = currentRow.getCell(REMARK, Row.CREATE_NULL_AS_BLANK).toString().trim();
			
			Cell cell = currentRow.getCell(COMPLETION_DATE,Row.CREATE_NULL_AS_BLANK);
			Date completionDate = null;
			if(cell.getCellType()== Cell.CELL_TYPE_NUMERIC && HSSFDateUtil.isCellDateFormatted(cell)){
				completionDate = currentRow.getCell(COMPLETION_DATE,Row.CREATE_NULL_AS_BLANK).getDateCellValue();
			}
			String companyId = getCompanyId(companyAbbr, companyName);
			
			ActivityBean activityBean = new ActivityBean(activityId, companyId, remark, assignedUser, completionDate,false, false, false, false, false,"");
			if(statusNum==1){//In time
				activityBean = new ActivityBean(activityId, companyId, remark, assignedUser, completionDate,true, true, false, false, false,Constants.COMP_INTIME);
			}else if(statusNum==2){//Delayd
				activityBean = new ActivityBean(activityId, companyId, remark, assignedUser, completionDate,true, true, false, false, true,Constants.COM_DELAYED);
			}else if(statusNum==3){//Pending review
				activityBean = new ActivityBean(activityId, companyId, remark, assignedUser, completionDate,true, false, false, false, false,Constants.PENDING_REVIEW);
			}else if(statusNum==4){//Pending compliance
				activityBean = new ActivityBean(activityId, companyId, remark, assignedUser, completionDate,false, false, false, false, false,Constants.PENDING_COMPLIE);
			}else if(statusNum==5){//Not due
				activityBean = new ActivityBean(activityId, companyId, remark, assignedUser, completionDate,false, false, false, true, false,Constants.COM_REJE);
			}
				
			activityBeanSheet.add(activityBean);
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
	
	
	private static String getCompanyId(String abbr,String name){
		for(CompanyBean companyBean:companies){
			if(companyBean.getAbbriviation().trim().equals(abbr) && companyBean.getName().trim().equals(name)){
				return companyBean.getCompanyId();
			}
		}
		return "";
	}
}
