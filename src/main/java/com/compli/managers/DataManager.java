package com.compli.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.ActivityForAddNewActivity;
import com.compli.bean.AddNewActivitiesBean;
import com.compli.db.bean.UserBean;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.UserDao;
import com.compli.util.Util;
import com.compli.util.datamigration.v2.DBMigrationUtilV2ActivityAssignmentUpload;
import com.compli.util.datamigration.v2.DBMigrationUtilV2ActivityAssociationUpload;
import com.compli.util.datamigration.v2.DBMigrationUtilV2ActivityMasterUpload;
import com.compli.util.datamigration.v2.DBMigrationUtilV2ActivityUpload;
import com.compli.util.datamigration.v2.DBMigrationUtilV2LawMaster;
import com.compli.util.datamigration.v2.DBMigrationUtilV2PeriodicityDateMaster;
import com.compli.util.datamigration.v2.DBMigrationUtilV2PeriodicityMaster;
import com.google.api.services.drive.model.File.ImageMediaMetadata.Location;

public class DataManager {
	private static int ROW_COUNT = 15;
	private static int ARTECH_MAIL = 11;
	private static int CM_MAIL = 12;
	private static int SM_MAIL = 13;
	private static int CO_MAIL = 14;
	
	private static int PERIODICITY_ROW_NO = 9;
	private static int PERIODICITY_DATE_ROW_NO = 8;
	private static int LAW_DESC = 4;
	private static int COMPLAINCE_AREA = 1;
	private static int MAX_ACTIVITY_COUNT = 50000;
	UserDao userDao;
	ActivityDao activityDao;
	LocationDao locationDao;
	public DataManager() {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.userDao = (UserDao) ctx.getBean("udao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");
		this.locationDao = (LocationDao) ctx.getBean("locationDao");
	}
	
	public static void main(String[] args) throws IOException {
		FileInputStream excelFile = new FileInputStream(new File("C:\\report\\Table Tracker_Online Tyari_final_FY 2019-20-Modified.xlsx"));
        new DataManager().uploadData(excelFile,"0498d16340aa4f9e");        
	}
	
	public Map<String, Map<String, List<String>>> uploadActivities(AddNewActivitiesBean activitiesBean){
		Map<String, Map<String, List<String>>> errors = precheck(activitiesBean);
		
		if(!errors.isEmpty()){
			return errors;
		}
		int maxActivityCount = activityDao.getMaximumActivityId();
		maxActivityCount++;
		setPeriodicityMaster(activitiesBean.getActivities());
		setPeriodicityDate(activitiesBean.getActivities());
		setLawMaster(activitiesBean.getActivities());
		uploadActivityMaster(activitiesBean.getActivities(), activitiesBean.getCompanies(), maxActivityCount);
		uploadActivityAssociation(activitiesBean.getActivities(), activitiesBean.getCompanies(), maxActivityCount);
		uploadActivity(activitiesBean.getActivities(), activitiesBean.getCompanies(), maxActivityCount);
		return null;
	}
	
	public List<String> uploadData(InputStream excelFile,String companyId) throws IOException{
		Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        List<String> reasonToReject = checkSheet(datatypeSheet);
       
        int maxActivityCount = activityDao.getMaximumActivityId();
        
        if(reasonToReject.isEmpty()){
	        setPeriodicityMaster(datatypeSheet);
	        setPeriodicityDate(datatypeSheet);
	        setLawMaster(datatypeSheet);	        
	        int newActivityCount = ++maxActivityCount;
	        uploadActivityMaster(datatypeSheet,companyId,newActivityCount);
	        uploadActivityAssociation(datatypeSheet,  newActivityCount);
	        uploadActivity(datatypeSheet, newActivityCount, companyId);
	        uploadActivityAssignement(datatypeSheet, newActivityCount);
        }
		return reasonToReject;
	}
	
	public Map<String,Map<String, List<String>>> precheck(AddNewActivitiesBean activitiesBean){
		Map<String,Map<String, List<String>>> errors = new HashMap<String, Map<String, List<String>>>();
		List<String> location = new ArrayList<String>();
		activitiesBean.getActivities().forEach((activity)->{
			location.add(activity.getLocation());
		});
		List<String> companies = activitiesBean.getCompanies();
		
		Map<String, List<String>>companyAndUnavailableLocationMap = new HashMap<String, List<String>>();
		for(String company:companies){
			List<String>unavailableLoc = checkLocationForCompany(location, company);
			if(unavailableLoc.size() != 0){
				companyAndUnavailableLocationMap.put(company, unavailableLoc);
			}
		}
		if(!companyAndUnavailableLocationMap.isEmpty()){
			errors.put("companyAndUnavailableLocationMap", companyAndUnavailableLocationMap);
		}
		return errors;
	}
	
	//This method will return locations which are not present in 
	public List<String> checkLocationForCompany(List<String>location,String companyId){
		location.replaceAll(loc->Util.formLocationId(loc));
		List locationForCompany = this.locationDao.getCompanyLocation(companyId);
		List<String>locationFromDb = new ArrayList<String>();
		List<String>fullLocationName = new ArrayList<String>();
		Map<String, String>locationMap = new HashMap<String, String>();
		
		locationForCompany.forEach(loc->{
			locationFromDb.add((String)((Map)loc).get("locationId"));
			locationMap.put((String)((Map)loc).get("locationId"), (String)((Map)loc).get("locationName"));
		});
		location.removeAll(locationFromDb);
		
		location.forEach(loc->{
			fullLocationName.add(locationMap.get(loc));
		});
		return fullLocationName;
	}
	
	public static void setPeriodicityMaster(Sheet sheet){
		System.out.println("Upload periodicty master");
		DBMigrationUtilV2PeriodicityMaster.init(PERIODICITY_ROW_NO);
		DBMigrationUtilV2PeriodicityMaster.createPeriodicityMaster(sheet);
	}
	
	public static void setPeriodicityMaster(List<ActivityForAddNewActivity> activities){
		DBMigrationUtilV2PeriodicityMaster.init(PERIODICITY_ROW_NO);
		System.out.println("Upload periodicty master");
		DBMigrationUtilV2PeriodicityMaster.createPeriodicityMaster(activities);
	}
	
	private static void setPeriodicityDate(Sheet sheet){
		System.out.println("Upload periodicity date");
		DBMigrationUtilV2PeriodicityDateMaster.init(PERIODICITY_DATE_ROW_NO);
		DBMigrationUtilV2PeriodicityDateMaster.createPeriodicityDateMaster(sheet);		
	}
	
	private static void setPeriodicityDate(List<ActivityForAddNewActivity> activities){
		System.out.println("Upload periodicity date");
		DBMigrationUtilV2PeriodicityDateMaster.init(PERIODICITY_DATE_ROW_NO);
		DBMigrationUtilV2PeriodicityDateMaster.createPeriodicityDateMaster(activities);		
	}
	
	private static void setLawMaster(Sheet sheet){
		System.out.println("Upload Law master");
		DBMigrationUtilV2LawMaster.init(LAW_DESC,COMPLAINCE_AREA);
		DBMigrationUtilV2LawMaster.createLawMaster(sheet);		
	}
	
	private static void setLawMaster(List<ActivityForAddNewActivity> activities){
		System.out.println("Upload Law master");
		DBMigrationUtilV2LawMaster.init(LAW_DESC,COMPLAINCE_AREA);
		DBMigrationUtilV2LawMaster.createLawMaster(activities);		
	}
	
	private static void uploadActivityMaster(Sheet sheet,String companyId,int activityCount){
		System.out.println("Upload Activity master");
		DBMigrationUtilV2ActivityMasterUpload.init(companyId);
		DBMigrationUtilV2ActivityMasterUpload.createActivityMaster(sheet,activityCount);		
	}
	
	private static void uploadActivityMaster(List<ActivityForAddNewActivity> activities,List<String> companyIds,int activityCount){
		System.out.println("Upload Activity master");
		int index=0;
		for(String companyId:companyIds){
			activityCount = activities.size()*index+activityCount;
			DBMigrationUtilV2ActivityMasterUpload.init(companyId);
			DBMigrationUtilV2ActivityMasterUpload.createActivityMaster(activities,activityCount);
			index++;
		}
	}
	
	private static void uploadActivityAssociation(Sheet sheet,int activityCount){
		System.out.println("Upload Activity association");
		DBMigrationUtilV2ActivityAssociationUpload.init();
		DBMigrationUtilV2ActivityAssociationUpload.createActivityAssociation(sheet,activityCount);		
	}
	
	private static void uploadActivityAssociation(List<ActivityForAddNewActivity> activities,List<String> companyIds,int activityCount){
		System.out.println("Upload Activity association");
		int index=0;
		for(String companyId:companyIds){
			activityCount = activities.size()*index+activityCount;
			DBMigrationUtilV2ActivityAssociationUpload.init();
			DBMigrationUtilV2ActivityAssociationUpload.createActivityAssociation(activities,activityCount);
			index++;
		}
	}
	
	private static void uploadActivity(Sheet sheet,int activityCount,String companyId){
		System.out.println("Upload Activity");
		DBMigrationUtilV2ActivityUpload.init();
		DBMigrationUtilV2ActivityUpload.createActivity(sheet,activityCount,companyId);		
	}
	
	private static void uploadActivity(List<ActivityForAddNewActivity> activities,List<String> companyIds,int activityCount){
		System.out.println("Upload Activity");
		int index=0;
		for(String companyId:companyIds){
			activityCount = activities.size()*index+activityCount;
			DBMigrationUtilV2ActivityUpload.init();
			DBMigrationUtilV2ActivityUpload.createActivity(activities,activityCount,companyId);
			index++;
		}
	}
	
	private static void uploadActivityAssignement(Sheet sheet,int activityCount){
		System.out.println("Upload Activity");
		DBMigrationUtilV2ActivityAssignmentUpload.init();
		DBMigrationUtilV2ActivityAssignmentUpload.updateUserDetails(sheet,activityCount);		
	}
	
	public List<String> checkSheet(Sheet uploadedSheet){
		List<String> invalidReson = new ArrayList<String>();
		boolean isCountValid = validateSheet(uploadedSheet);
		if(!isCountValid){
			invalidReson.add("Count is wrong");
		}
		
		List<String> invalidUsers = checkUserEmails(uploadedSheet);
		if(invalidUsers.size()>0){
			invalidReson.add(invalidUsers.toString()+"User emails are wrong");
		}
		return invalidReson;
	}
	
	public List<String> checkUserEmails(Sheet sheet){
		List<String> wrongEmail = new ArrayList<String>();
		Map<String,List<String>> userTypeAndMailMap = new HashMap<String, List<String>>();
		List<UserBean> allUsers = this.userDao.getAllData();
		Map<String,String> userType = new HashMap<String, String>();
		allUsers.parallelStream().forEach(user->{
			userType.put(user.getEmail(), user.getUserTypeId());
		});
		
		Iterator<Row> iterator = sheet.iterator();
        int rowCount = 0;
        Set<Integer>count = new HashSet<Integer>();
        while (iterator.hasNext()) {
        	if(rowCount==0){
        		rowCount++;
        		Row currentRow = iterator.next();
        		continue;
        	}
        	rowCount++;
        	Row currentRow = iterator.next();
        	
        	Cell cell = currentRow.getCell(ARTECH_MAIL, Row.CREATE_NULL_AS_BLANK);
        	String artechEmail = cell.getStringCellValue();
        	if(!(userType.containsKey(artechEmail) && userType.get(artechEmail).equals("ArTechUser"))){
        		wrongEmail.add(artechEmail);
        	}
        	cell = currentRow.getCell(CM_MAIL, Row.CREATE_NULL_AS_BLANK);
        	String cmEmail = cell.getStringCellValue();
        	if(!(userType.containsKey(cmEmail) && userType.get(cmEmail).equals("cManager"))){
        		wrongEmail.add(cmEmail);
        	}
        	cell = currentRow.getCell(SM_MAIL, Row.CREATE_NULL_AS_BLANK);
        	String smEmail = cell.getStringCellValue();
        	if(!(userType.containsKey(smEmail) && userType.get(smEmail).equals("sManager"))){
        		wrongEmail.add(smEmail);
        	}
        	cell = currentRow.getCell(CO_MAIL, Row.CREATE_NULL_AS_BLANK);
        	String coEmail = cell.getStringCellValue();
        	if(!(userType.containsKey(coEmail) && userType.get(coEmail).equals("cOwner"))){
        		wrongEmail.add(coEmail);
        	}        	
        }      
		return wrongEmail;
	}
	
	public static boolean validateSheet(Sheet datatypeSheet){
		Set<Integer>count = new HashSet<Integer>();
		Iterator<Row> iterator = datatypeSheet.iterator();
		while (iterator.hasNext()) {
        	Row currentRow = iterator.next();
            count.add((int) currentRow.getLastCellNum());
        }
		count.remove(ROW_COUNT);
		return count.size()==0;
	}
	
	public static Sheet readSheetFile() throws IOException{
		FileInputStream excelFile = new FileInputStream(new File(""));
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        int rowCount = 0;
        Set<Integer>count = new HashSet<Integer>();
        while (iterator.hasNext()) {
        	rowCount++;
        	Row currentRow = iterator.next();
        	int cn;
            for(cn=0; cn<currentRow.getLastCellNum(); cn++) {
                // If the cell is missing from the file, generate a blank one
                // (Works by specifying a MissingCellPolicy)
                Cell cell = currentRow.getCell(cn, Row.CREATE_NULL_AS_BLANK);
            }
            count.add(cn);
        }      
        return datatypeSheet;
       }

}
