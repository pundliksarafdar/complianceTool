package com.compli.util.datamigration.v2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.migration.v2.CompanyBean;
import com.compli.db.bean.migration.v2.UserBean;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.UserDao;
import com.compli.util.UUID;
import com.compli.util.Util;

public class DBMigrationUtilV2CompanyLocations {
	private static int COMPANY = 1;
	private static int COMPANY_ABBR = 2;
	private static int COMPANY_LOCATION = 3;
	
	static CompanyDao companyDao;
	
	public DBMigrationUtilV2CompanyLocations() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.companyDao = (CompanyDao) ctx.getBean("companyDao");
	}
	
	public static void init(){
		new DBMigrationUtilV2CompanyLocations();
	}
	
	public static void createCompaniesLocation(Sheet datatypeSheet){
		Iterator<Row> iterator = datatypeSheet.iterator();
		List<CompanyBean> companyBeans = new ArrayList<CompanyBean>(); 
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			
			Row currentRow = iterator.next();
			Cell company = currentRow.getCell(COMPANY, Row.CREATE_NULL_AS_BLANK);
			if(company.toString().trim().equals("")){
				continue;
			}
			Cell companyAbbr = currentRow.getCell(COMPANY_ABBR, Row.CREATE_NULL_AS_BLANK);
			Cell companyLocation = currentRow.getCell(COMPANY_LOCATION, Row.CREATE_NULL_AS_BLANK);
			CompanyBean companyBean = new CompanyBean(UUID.getUID(), company.toString(), companyAbbr.toString());
			
			//Get companyiTem from list to update it
			int beanLocation = companyBeans.indexOf(companyBean);
			String locationId = Util.formLocationId(companyLocation.toString());
			if(beanLocation>-1){
				//if company present in list then just update location if not exist
				if(!companyBeans.get(beanLocation).getCompanyLocations().contains(locationId)){
					companyBeans.get(beanLocation).getCompanyLocations().add(locationId);
				}
			}else{
				//If company is not present then udpate location and add bean to list
				companyBean.getCompanyLocations().add(locationId);
				companyBeans.add(companyBean);
			}
		}
		
		//Get all data from user table
		List<Map<String, Object>> allCompanyFromDb = companyDao.getAllComapanyAndLocations();
		
		List<CompanyBean> formedCompaniesBean  = formCompanyList(allCompanyFromDb);
		
		List<CompanyBean> filteredCompanies = companyBeans.parallelStream().filter(companyBean->{
			return !formedCompaniesBean.parallelStream().anyMatch(companyBeansDb->{
				return companyBeansDb.getCompanyAbbr().toLowerCase().trim().equals(companyBean.getCompanyAbbr().toLowerCase().trim()) &&
						companyBeansDb.getCompanyName().toLowerCase().trim().equals(companyBean.getCompanyName().toLowerCase().trim()) &&
						isNewLocationAdded(companyBeansDb.getCompanyLocations(), companyBean.getCompanyLocations());
			});
		}).distinct().collect(Collectors.toList());
		updateIds(filteredCompanies, formedCompaniesBean);
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateCompanyLocationLocation(filteredCompanies);
		System.out.println(filteredCompanies);
	}
	
	private static List<CompanyBean> formCompanyList(List<Map<String, Object>> allCompanyFromDb){
		List<CompanyBean> companyBeans = new ArrayList<CompanyBean>();
		allCompanyFromDb.forEach(companyFDb->{
			CompanyBean companyBean = new CompanyBean(companyFDb.get("companyId").toString(), companyFDb.get("name").toString(), companyFDb.get("abbriviation").toString());
			int beanLoc = companyBeans.indexOf(companyBean);
			if(beanLoc>-1){
				companyBeans.get(beanLoc).getCompanyLocations().add(companyFDb.get("locationId").toString());
			}else{
				if(null!=companyFDb.get("locationId")){
					companyBean.getCompanyLocations().add(companyFDb.get("locationId").toString());					
				}
				companyBeans.add(companyBean);
			}
		});
		
		return companyBeans;
	}
	
	private static boolean isNewLocationAdded(List<String>locationsFromDb,List<String>locationFromFromSheet){
		locationFromFromSheet.removeAll(locationsFromDb);
		return locationFromFromSheet.size()==0;
	}
	
	//Ids should be updated from db
	private static void updateIds(List<CompanyBean> filteredCompanies,List<CompanyBean> formedCompaniesBean){
		for(CompanyBean companyBean:filteredCompanies){
			int index = formedCompaniesBean.indexOf(companyBean);
			if(index>-1){
				companyBean.setCompanyId(formedCompaniesBean.get(index).getCompanyId());
			}
		}
	}
	
}
