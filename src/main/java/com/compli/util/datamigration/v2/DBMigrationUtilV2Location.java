package com.compli.util.datamigration.v2;

import java.util.ArrayList;
import java.util.HashMap;
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



import com.compli.db.bean.migration.v2.CompanyBean;
import com.compli.db.bean.migration.v2.LocationBean;
import com.compli.db.bean.migration.v2.UserBean;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.UserDao;
import com.compli.util.UUID;

public class DBMigrationUtilV2Location {
	private static int LOCATION = 3;
	
	static LocationDao locationDao;
	
	public DBMigrationUtilV2Location() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.locationDao = (LocationDao) ctx.getBean("locationDao");
	}
	
	public static void init(){
		new DBMigrationUtilV2Location();
	}
	
	public static void createLocations(Sheet datatypeSheet){
		Iterator<Row> iterator = datatypeSheet.iterator();
		Set<LocationBean> locationBeansSheet = new HashSet<LocationBean>(); 
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			
			Row currentRow = iterator.next();
			Cell location = currentRow.getCell(LOCATION, Row.CREATE_NULL_AS_BLANK);
			if(location.toString().trim().equals("")){
				continue;
			}
			LocationBean locationBean = new LocationBean(location.toString().toLowerCase().replace(" ", ""), location.toString());
			locationBeansSheet.add(locationBean);
		}
		
		//Get all data from user table locationId,locationName
		List<Map<String, Object>> allLocations = locationDao.getAllLocation();
		Set<LocationBean> locationBeans = new HashSet<LocationBean>(); 
		
		allLocations.parallelStream().forEach(location->{
			String locationId = location.get("locationId").toString().toLowerCase();
			String locationName = location.get("locationName").toString();
			locationBeans.add( new LocationBean(locationId, locationName));			
		});
		
		List<LocationBean> newLocations = locationBeansSheet.parallelStream().filter(locationBeanSheet->{
			return !locationBeans.contains(locationBeanSheet);
		}).distinct().collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateLocation(newLocations);
		System.out.println(newLocations);
	}
	
}
