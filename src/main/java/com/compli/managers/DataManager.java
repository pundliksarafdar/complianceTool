package com.compli.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

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

public class DataManager {
	private static int ROW_COUNT = 15;
	private static int ARTECH_MAIL = 11;
	private static int CM_MAIL = 12;
	private static int SM_MAIL = 13;
	private static int CO_MAIL = 14;
	private static int LOCATION = 0;
	
	private static int PERIODICITY_ROW_NO = 9;
	private static int PERIODICITY_DATE_ROW_NO = 8;
	private static int LAW_DESC = 4;
	private static int COMPLAINCE_AREA = 1;
	private static int MAX_ACTIVITY_COUNT = 50000;

	private static int ALL_COM_LOCATION = 0;
	private static int ALL_COM_COM_AREA = 1;
	private static int ALL_COM_ACT_NAME = 2;
	private static int ALL_COM_DESC = 3;
	private static int ALL_COM_LAW_DESC = 4;
	private static int ALL_COM_RISK = 5;
	private static int ALL_COM_CONS = 6;
	private static int ALL_COM_FORM = 7;
	private static int ALL_COM_PERIODICITY = 8;
	private static int ALL_COM_PERIODICITY_DESC = 9;


	UserDao userDao;
	ActivityDao activityDao;
	LocationDao locationDao;
	public DataManager() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		this.userDao = (UserDao) ctx.getBean("udao");
		this.activityDao = (ActivityDao) ctx.getBean("activityDao");
		this.locationDao = (LocationDao) ctx.getBean("locationDao");
	}
	
	public static void main(String[] args) throws IOException {
		FileInputStream excelFile = new FileInputStream(new File("C:\\report\\Table Tracker_Online Tyari_final_FY 2019-20-Modified.xlsx"));
        new DataManager().uploadData(excelFile,"0498d16340aa4f9e");        
	}

	public AddNewActivitiesBean loadActivitiesForCompanies(InputStream excelFile,List<String> companies){
		Workbook workbook = null;
		AddNewActivitiesBean activitiesBean = new AddNewActivitiesBean();
		try {
			workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			List<com.compli.db.bean.migration.v2.PeriodicityMasterBean> periodicityMasterBeans = new ArrayList<com.compli.db.bean.migration.v2.PeriodicityMasterBean>();
			Set<com.compli.db.bean.migration.v2.PeriodicityMasterBean> periodicityMasterBeansSet = new HashSet<com.compli.db.bean.migration.v2.PeriodicityMasterBean>();
			boolean isFirst = false;
			//Form userBean from uploaded sheet
			List<ActivityForAddNewActivity> activities = new ArrayList<>();
			while (iterator.hasNext()) {
				if(!isFirst){
					isFirst = true;
					iterator.next();
					continue;
				}

				Row currentRow = iterator.next();
				String loc = currentRow.getCell(ALL_COM_LOCATION, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				String cArea = currentRow.getCell(ALL_COM_COM_AREA, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				String aName = currentRow.getCell(ALL_COM_ACT_NAME, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				String desc = currentRow.getCell(ALL_COM_DESC, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				String lawDesc = currentRow.getCell(ALL_COM_LAW_DESC, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				String risk = currentRow.getCell(ALL_COM_RISK, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				String cons = currentRow.getCell(ALL_COM_CONS, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				Date timeD = currentRow.getCell(ALL_COM_PERIODICITY, Row.CREATE_NULL_AS_BLANK).getDateCellValue();

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
				String dateFormat = simpleDateFormat.format(timeD);

				String form = currentRow.getCell(ALL_COM_FORM, Row.CREATE_NULL_AS_BLANK).getStringCellValue();
				String perDesc = currentRow.getCell(ALL_COM_PERIODICITY_DESC, Row.CREATE_NULL_AS_BLANK).getStringCellValue();

				ActivityForAddNewActivity activityForAddNewActivity = new ActivityForAddNewActivity(loc,cArea,aName,desc,lawDesc,risk,cons,form,dateFormat,perDesc);
				activities.add(activityForAddNewActivity);
			}
			activitiesBean.setActivities(activities);
			activitiesBean.setCompanies(companies);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return activitiesBean;
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

	public Map<String, Map<String, List<String>>> uploadDataForMultipleCompanies(InputStream excelFile,List<String> companyId) throws IOException{
		AddNewActivitiesBean activities = loadActivitiesForCompanies(excelFile, companyId);
		return uploadActivities(activities);
	}
	
	public Map<String, Map<String, List<String>>> uploadData(InputStream excelFile,String companyId) throws IOException{
		Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Map<String, Map<String, List<String>>> reasonToReject = checkSheet(datatypeSheet,companyId);
       
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


	//This pre-check method is only for upload files for multiple companies
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
		Set<String>fullLocationName = new HashSet<>();
		Map<String, String>locationMap = new HashMap<String, String>();
		
		locationForCompany.forEach(loc->{
			locationFromDb.add((String)((Map)loc).get("locationId"));
			locationMap.put((String)((Map)loc).get("locationId"), (String)((Map)loc).get("locationName"));
		});
		location.removeAll(locationFromDb);

		Map<String, String> allLocations = this.locationDao.getAllLocationsAndId();
		location.forEach(loc->{
			fullLocationName.add(allLocations.get(loc));
		});
		return new ArrayList<>(fullLocationName);
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

	public static void setPeriodicityDate(List<ActivityForAddNewActivity> activities){
		System.out.println("Upload periodicity date");
		DBMigrationUtilV2PeriodicityDateMaster.init(PERIODICITY_DATE_ROW_NO);
		DBMigrationUtilV2PeriodicityDateMaster.createPeriodicityDateMaster(activities);		
	}
	
	private static void setLawMaster(Sheet sheet){
		System.out.println("Upload Law master");
		DBMigrationUtilV2LawMaster.init(LAW_DESC,COMPLAINCE_AREA);
		DBMigrationUtilV2LawMaster.createLawMaster(sheet);		
	}

	public static void setLawMaster(List<ActivityForAddNewActivity> activities){
		System.out.println("Upload Law master");
		DBMigrationUtilV2LawMaster.init(LAW_DESC,COMPLAINCE_AREA);
		DBMigrationUtilV2LawMaster.createLawMaster(activities);		
	}
	
	private static void uploadActivityMaster(Sheet sheet,String companyId,int activityCount){
		System.out.println("Upload Activity master");
		DBMigrationUtilV2ActivityMasterUpload.init(companyId);
		DBMigrationUtilV2ActivityMasterUpload.createActivityMaster(sheet,activityCount);		
	}

	public static void uploadActivityMaster(List<ActivityForAddNewActivity> activities,List<String> companyIds,int activityCount){
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

	public static void uploadActivityAssociation(List<ActivityForAddNewActivity> activities,List<String> companyIds,int activityCount){
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

	public static void uploadActivity(List<ActivityForAddNewActivity> activities,List<String> companyIds,int activityCount){
		System.out.println("Upload Activity");
		int index=0;
		for(String companyId:companyIds){
			activityCount = activities.size()*index+activityCount;
			DBMigrationUtilV2ActivityUpload.init();
			DBMigrationUtilV2ActivityUpload.createActivity(activities,activityCount,companyId);
			index++;
		}
	}

	public static void assigneActivities(int activityidStart,int activityIdEnd,String userId){
		DBMigrationUtilV2ActivityAssignmentUpload.init();
		DBMigrationUtilV2ActivityAssignmentUpload.assigneActivities(activityidStart,activityIdEnd,userId);
	}
	
	private static void uploadActivityAssignement(Sheet sheet,int activityCount){
		System.out.println("Upload Activity");
		DBMigrationUtilV2ActivityAssignmentUpload.init();
		DBMigrationUtilV2ActivityAssignmentUpload.updateUserDetails(sheet,activityCount);		
	}
	
	public Map<String, Map<String, List<String>>> checkSheet(Sheet uploadedSheet, String companyId){
		Map<String,Map<String, List<String>>> errors = new HashMap<String, Map<String, List<String>>>();
		List<String> invalidReson = new ArrayList<String>();
		boolean isCountValid = validateSheet(uploadedSheet);
		if(!isCountValid){
			invalidReson.add("Count is wrong");
		}
		
		List<String> invalidUsers = checkUserEmails(uploadedSheet);
		List<String> invalidLocation = checkLocations(uploadedSheet,companyId);
		if(invalidUsers.size()>0){
			invalidReson.add(invalidUsers.toString()+"User emails are wrong");
		}

		if(invalidLocation.size()>0){
			errors.put("companyAndUnavailableLocationMap",new HashMap(){{put(companyId,invalidLocation);}});
		}
		if(invalidReson.size()>0){
			errors.put("invalidEmails",new HashMap(){{put(companyId,invalidReson);}});
		}
		return errors;
	}

	//This method will check user and there type in sheet
	public List<String> checkLocations(Sheet sheet,String companyId){
		List<String> companyLocation = new ArrayList<>();
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

			Cell cell = currentRow.getCell(LOCATION, Row.CREATE_NULL_AS_BLANK);
			String location = cell.getStringCellValue();
			if(!companyLocation.contains(location)){
				companyLocation.add(location);
			}
		}
		companyLocation.replaceAll(loc->Util.formLocationId(loc));
		return checkLocationForCompany(companyLocation, companyId);
	}

	//This method will check user and there type in sheet
	public List<String> checkUserEmails(Sheet sheet){
		Set<String> wrongEmail = new HashSet<>();
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
		return new ArrayList<>(wrongEmail);
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
