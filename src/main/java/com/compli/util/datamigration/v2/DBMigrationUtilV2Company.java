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

import com.compli.db.bean.migration.v2.CompanyBean;
import com.compli.db.bean.migration.v2.UserBean;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.UserDao;
import com.compli.util.UUID;

public class DBMigrationUtilV2Company {
	private static int COMPANY = 1;
	private static int COMPANY_ABBR = 2;
	private static int COMPANY_LOCATION = 3;
	
	static CompanyDao companyDao;
	
	public DBMigrationUtilV2Company() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");;
		this.companyDao = (CompanyDao) ctx.getBean("companyDao");
	}
	
	public static void init(){
		new DBMigrationUtilV2Company();
	}
	
	public static void createCompanies(Sheet datatypeSheet){
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
			if(companyAbbr.toString().trim().equals("")){
				continue;
			}
			Cell companyLocation = currentRow.getCell(COMPANY_LOCATION, Row.CREATE_NULL_AS_BLANK);
			if(companyLocation.toString().trim().equals("")){
				continue;
			}
			companyBeans.add(new CompanyBean(UUID.getUID(), company.toString(), companyAbbr.toString()));
		}
		
		//Get all data from user table
		List<com.compli.db.bean.CompanyBean> allCompanyFromDb = companyDao.getAllCompany();
		List<CompanyBean> filteredCompanies = companyBeans.parallelStream().filter(companyBean->{
			return !allCompanyFromDb.parallelStream().anyMatch(companyBeansDb->{
				return companyBeansDb.getAbbriviation().equals(companyBean.getCompanyAbbr()) &&
						companyBeansDb.getName().equals(companyBean.getCompanyName());
			});
		}).distinct().collect(Collectors.toList());
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.udpateCompanies(filteredCompanies);
		System.out.println(filteredCompanies);
	}
	
}
