package com.compli.util.datamigration.v2;

import java.util.ArrayList;
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

import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.db.bean.migration.v2.LawMasterBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.ActivityAssociationDao;
import com.compli.db.dao.ActivityMasterDao;
import com.compli.db.dao.LawMasterDao;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.util.Util;

public class DBMigrationUtilV2ActivityAssociation {
	private static int ID = 0;
	private static int COMPANY_LOCATION = 3;
	private static int LAW_DESC = 8;
	static ActivityAssociationDao activityAssociationDao;
	static LocationDao locationDao;
	static List<Map<String,Object>> allLocations;
	public DBMigrationUtilV2ActivityAssociation() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");;
		activityAssociationDao = (ActivityAssociationDao) ctx.getBean("activityAssociationDao");
		this.locationDao = (LocationDao) ctx.getBean("locationDao");
		this.allLocations = locationDao.getAllLocation();
	}
	
	public static void init(){
		new DBMigrationUtilV2ActivityAssociation();
	}
	
	public static void createActivityAssociation(Sheet datatypeSheet){
		List<ActivityAssociationBean> activityAssociationBeans = new ArrayList<ActivityAssociationBean>();
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
			String companyLocation = currentRow.getCell(COMPANY_LOCATION, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String lawDesc = currentRow.getCell(LAW_DESC, Row.CREATE_NULL_AS_BLANK).toString().trim();
			if(companyLocation.equals("") || lawDesc.equals("")){
				continue;
			}
			String companyLocationId = Util.formLocationId(companyLocation);//getCompanyLocationId(companyLocation);
			if(companyLocationId.equals("")){
				System.out.println(companyLocation);
			}
			ActivityAssociationBean activityAssociationBean = new ActivityAssociationBean(activityId, activityId, "AAA", companyLocationId);
			activityAssociationBeans.add(activityAssociationBean);
		}
		
		List<ActivityAssociationBean> associationFromDb = activityAssociationDao.getAllActivityAssociationData();
		List<ActivityAssociationBean> filterdAssociations = activityAssociationBeans.parallelStream().filter(activityAssociation->{
			return !associationFromDb.parallelStream().anyMatch(associationDb->{
				return associationDb.getActivityId().equals(activityAssociation.getActivityId()) &&
						associationDb.getAssociationId().equals(activityAssociation.getAssociationId());
			});
		}).collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateActivityAssociation(filterdAssociations);
	}
	
	static String getCompanyLocationId(String companyLocation){
		for(Map<String,Object>location:allLocations){
			if(location.get("locationName").toString().trim().toLowerCase().equals(companyLocation.trim().toLowerCase())){
				return location.get("locationId").toString().trim();
			}
		}
		return "";
	}
}
