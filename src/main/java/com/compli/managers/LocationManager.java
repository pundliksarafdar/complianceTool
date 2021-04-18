package com.compli.managers;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.CompanyLocations;
import com.compli.db.bean.LocationBean;
import com.compli.db.dao.LocationDao;

public class LocationManager {
	LocationDao locationDao;
	
	public LocationManager() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		locationDao = (LocationDao) ctx.getBean("locationDao");
	}
	
	public List<Map<String, Object>> getAllLocations(){
		return locationDao.getAllLocation();		
	}
	
	public boolean addLocation(List<LocationBean> locationBeans){
		boolean locationStatus = false;
		for(LocationBean locationBean:locationBeans){
			locationStatus = locationDao.addLocation(locationBean);
		}
		return locationStatus;
	}
	
	public boolean deleteLocation(String locationId){
		return locationDao.deleteLocation(locationId);
	}
	
	public List getCompanyLocation(String companyId){
		return locationDao.getCompanyLocation(companyId);
	}
	
	public boolean addCompanyLocation(CompanyLocations companyLocations ){
		boolean isLocationAdded = true;
		List<String> locations = companyLocations.getLocationIds();
		for(String locationId:locations){
			try{
			isLocationAdded &= locationDao.addCompanyLocationForUpload(locationId, companyLocations.getCompanyId());
			}catch(Exception e){
				System.out.println("Location is already present");
			}
		}
		return isLocationAdded;
	}
}
