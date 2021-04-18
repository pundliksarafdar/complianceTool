package com.compli.util.datamigration.v2;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.Files;
import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.db.bean.migration.v2.FilesBean;
import com.compli.db.bean.migration.v2.LawMasterBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.ActivityAssociationDao;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.ActivityMasterDao;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.FilesDao;
import com.compli.db.dao.LawMasterDao;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.util.Util;

public class DBMigrationUtilV2FIles {
	private static int ACTIVITY_ID = 1;
	private static int DOCUMENT_NAME = 3;
	private static int SUBMIT_DATE = 4;
	private static int DOCUMENT_FILE = 5;
	
	static ActivityDao activityDao;
	static FilesDao filesDao;
	static List<CompanyBean> companies;
	public DBMigrationUtilV2FIles() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");;
		activityDao = (ActivityDao) ctx.getBean("activityDao");
		filesDao = (FilesDao) ctx.getBean("filesDao");
	}
	public static void init(){
		new DBMigrationUtilV2FIles();
	}
	
	public static void createFIles(Sheet datatypeSheet){
		List<FilesBean> filesBeans = new ArrayList<FilesBean>();
		List<ActivityBean> allActivities = activityDao.getAllActivityData();
		List<Files> allFIles = filesDao.getAlFiles();
		
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
			String activityId = (int)currentRow.getCell(ACTIVITY_ID, Row.CREATE_NULL_AS_BLANK).getNumericCellValue()+"";
			String docName = currentRow.getCell(DOCUMENT_NAME, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String submitDate = currentRow.getCell(SUBMIT_DATE, Row.CREATE_NULL_AS_BLANK).toString().trim();
			Date createdOn = currentRow.getCell(SUBMIT_DATE, Row.CREATE_NULL_AS_BLANK).getDateCellValue();
			 String fileName = currentRow.getCell(DOCUMENT_FILE, Row.CREATE_NULL_AS_BLANK).toString().trim();
			if(docName.equals("")){
				docName = fileName;
			}
			
			 if(fileName.equals("")){
					continue;
				}			
			
			 String companyId = getCompanyId(allActivities, activityId);
			 if(companyId.equals("")){
				 System.out.println("Company for id "+activityId +" not found");
				 continue;
			 }
			 FilesBean filesBean = new FilesBean(activityId, companyId, docName, submitDate, fileName);
			 filesBean.setCreatedOn(createdOn);
			 filesBeans.add(filesBean);
		}
		
		List<FilesBean> filteredList = filesBeans.parallelStream().filter(fileBean->{
			return !allFIles.parallelStream().anyMatch(allFile->{
				return allFile.getActivityId().equals(fileBean.getActivityId()) && allFile.getCompanyId().equals(fileBean.getCompanyId()) 
						&& allFile.getFilename().equals(fileBean.getDocumentName());
			});
		}).collect(Collectors.toList());
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateFiles(filteredList);
	}
	
	
	private static String getCompanyId(List<ActivityBean> allActivities,String activityId){
		for(ActivityBean activity:allActivities){
			if(activity.getActivityId().equals(activityId)){
				return activity.getCompanyId();
			}
		}
		return "";
	}
}
