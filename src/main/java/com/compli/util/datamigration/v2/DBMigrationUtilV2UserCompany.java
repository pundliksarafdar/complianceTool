package com.compli.util.datamigration.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.UserBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.UserCompanyBean;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.UserCompanyDao;
import com.compli.db.dao.UserDao;

public class DBMigrationUtilV2UserCompany {
	private static int ID = 0;
	private static int COMPANY_NAME = 1;
	private static int COMPANY_ABBR = 2;
	private static int SPN_USER = 18;
	private static int C_MANAGER = 19;
	private static int S_MANAGER = 20;
	private static int C_OWNER = 21;

	
	static CompanyDao companyDao;
	static UserCompanyDao userCompanyDao;
	static UserDao userDao;
	
	static List<Map<String,Object>> allLocations;
	static List<CompanyBean> companies;
	static List<UserBean> filteredUser;
	public DBMigrationUtilV2UserCompany() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		companyDao = (CompanyDao)ctx.getBean("companyDao");
		userCompanyDao = (UserCompanyDao)ctx.getBean("userCompanyDao");
		userDao = (UserDao)ctx.getBean("udao");
		
		companies = companyDao.getAllCompany();
		
		List<UserBean> allUsers = userDao.getAllData();
		
		List<UserCompanyBean> allUserCompanyDb = userCompanyDao.getAllUsersCompany();
		
		filteredUser = allUsers.parallelStream().filter(user->{
			return !allUserCompanyDb.parallelStream().anyMatch(userCompany->{
				return userCompany.getUserId().equals(user.getUserId());
			});
		}).collect(Collectors.toList());
	}
	
	public static void init(){
		new DBMigrationUtilV2UserCompany();
	}
	
	public static void createUserCompany(Sheet datatypeSheet){
		List<UserCompanyBean> userCompanyBeans = new ArrayList<UserCompanyBean>();
		Iterator<Row> iterator = datatypeSheet.iterator();
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			UserCompanyBean userCompanyBean = null;
			Row currentRow = iterator.next();
			String companyName = currentRow.getCell(COMPANY_NAME, Row.CREATE_NULL_AS_BLANK).toString().trim();
			if(companyName.equals("")){
				continue;
			}
			String companyAbbr = currentRow.getCell(COMPANY_ABBR, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String companyId = getCompanyId(companyAbbr, companyName);
			
			Cell cell = currentRow.getCell(SPN_USER, Row.CREATE_NULL_AS_BLANK);
			if(cell.toString().trim().equals("")){
				continue;
			}else{
				String userId = isUserContiainsFilteredUser(cell.toString());
				if(null!=userId){
					userCompanyBean = new UserCompanyBean(userId,companyId);
					userCompanyBeans.add(userCompanyBean);
				}
			}
			
			cell = currentRow.getCell(C_MANAGER, Row.CREATE_NULL_AS_BLANK);
			if(cell.toString().trim().equals("")){
				continue;
			}else{
				String userId = isUserContiainsFilteredUser(cell.toString());
				if(null!=userId){
					userCompanyBean = new UserCompanyBean(userId,companyId);
					userCompanyBeans.add(userCompanyBean);
				}
			}
			
			cell = currentRow.getCell(S_MANAGER, Row.CREATE_NULL_AS_BLANK);
			if(cell.toString().trim().equals("")){
				continue;
			}else{
				String userId = isUserContiainsFilteredUser(cell.toString());
				if(null!=userId){
					userCompanyBean = new UserCompanyBean(userId,companyId);
					userCompanyBeans.add(userCompanyBean);
				}
			}
			
			 cell = currentRow.getCell(C_OWNER, Row.CREATE_NULL_AS_BLANK);
			 if(cell.toString().trim().equals("")){
					continue;
				}else{
					String userId = isUserContiainsFilteredUser(cell.toString());
					if(null!=userId){
						userCompanyBean = new UserCompanyBean(userId,companyId);
						userCompanyBeans.add(userCompanyBean);
					}
				}
			 
		}
		userCompanyBeans = userCompanyBeans.parallelStream().filter(userCompanyBeanI->{
			 return userCompanyBeanI!=null;
			 }).distinct().collect(Collectors.toList());
	
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateUserCompany(userCompanyBeans);
	}
	
	
	private static String getCompanyId(String abbr,String name){
		for(CompanyBean companyBean:companies){
			if(companyBean.getAbbriviation().trim().equals(abbr) && companyBean.getName().trim().equals(name)){
				return companyBean.getCompanyId();
			}
		}
		return "";
	}
	
	private static String isUserContiainsFilteredUser(String userEmail){
		String userId = null;
		for(UserBean user:filteredUser){
			if(user.getEmail().trim().equals(userEmail)){
				userId = user.getUserId();
				break;
			}
		}
		return userId;
	}
}
