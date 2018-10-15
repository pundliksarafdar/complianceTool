package com.compli.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.LocationBean;
import com.compli.db.bean.UserBean;
import com.compli.util.DatabaseUtils;

public class LocationDao {
	private JdbcTemplate jdbcTemplate;

	private String companyLocations = "select companyId,abbriviation,location.locationId,locationName from location inner join "+	
			"(select cc.companyId,abbriviation,locationId from company cc inner join companylocation "+
					"on cc.companyId = companylocation.companyId where cc.companyId=?) companyWithLocation "+ 
			"on location.locationId = companyWithLocation.locationId";
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
	public boolean addLocation(LocationBean locationBean){
		boolean locationStatus = true;
		try {
			HashMap<String, List> instval = DatabaseUtils.formInsertStatement(locationBean);
			System.out.println(instval);
			int[] dest = new int[instval.get("type").toArray().length];
			int index = 0;
			for(Object objInt:instval.get("type")){
				int objIntI = (Integer)objInt;
				System.out.println(objIntI);
				dest[index++] = objIntI;
			} 
			this.jdbcTemplate.update(instval.get("query").get(0).toString(), 
					instval.get("value").toArray(), 
					dest);
		} catch (IllegalArgumentException | IllegalAccessException | DuplicateKeyException e) {
			e.printStackTrace(); 
			locationStatus = false;
		}
		return locationStatus;
		
	}
	
	public boolean addCompanyForUpload(LocationBean locationBean){
		String sql = "insert into location(locationId,locationName) values(?,?)";
		return this.jdbcTemplate.update(sql,locationBean.getLocationId(),locationBean.getLocationName())>0;
	}
	
	public boolean addCompanyLocationForUpload(String locationId,String companyId){
		String sql = "insert into companylocation(locationId,companyId) values(?,?)";
		try{
			return this.jdbcTemplate.update(sql,locationId,companyId)>0;
		}catch(Exception e){
			System.out.println("Location id:"+locationId+" CompanyId:"+companyId);
		}
		return false;
	}
	
	public static void main(String[] args) {
		LocationBean bean = new LocationBean();
		bean.setLocationId("ABC");
		bean.setLocationName("Mumbai");
		LocationDao dao = new LocationDao();
		//dao.insertUserValues(bean);
	}


	public List<Map<String, Object>> getAllLocation() {	
		List<Map<String, Object>> locationBeans = this.jdbcTemplate.queryForList("select * from location");
		return locationBeans;
	}
	
	public boolean deleteLocation(String locationId){
		return this.jdbcTemplate.update("delete from location where locationId=?",locationId)>0;
	}
	
	public List getCompanyLocation(String companyId){
		return this.jdbcTemplate.queryForList(this.companyLocations,companyId);
	}


}
