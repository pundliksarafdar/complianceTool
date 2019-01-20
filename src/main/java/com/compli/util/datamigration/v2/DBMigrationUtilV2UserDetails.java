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

public class DBMigrationUtilV2UserDetails {
	private static int FISRT_NAME = 1;
	private static int LAST_NAME = 2;
	private static int EMAIL = 3;
	
	static UserDao userDao;
	
	//filterdUser are user which are not having first or last name
	static List<UserBean> filteredUser;
	public DBMigrationUtilV2UserDetails() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		userDao = (UserDao)ctx.getBean("udao");
		
		List<UserBean> allUsers = userDao.getAllData();
		
		filteredUser = allUsers.parallelStream().filter(user->{
			return (user.getFirstName()==null || user.getFirstName().trim().isEmpty()) || (user.getLastName()==null || user.getLastName().trim().isEmpty());			
		}).collect(Collectors.toList());
	}
	
	public static void init(){
		new DBMigrationUtilV2UserDetails();
	}
	
	public static void updateUserDetails(Sheet datatypeSheet){
		Iterator<Row> iterator = datatypeSheet.iterator();
		List<UserBean> userBeans = new ArrayList<UserBean>();
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			
			UserBean userBean = new UserBean();
			Row currentRow = iterator.next();
			String firstName = currentRow.getCell(FISRT_NAME, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String lastName = currentRow.getCell(LAST_NAME, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String email = currentRow.getCell(EMAIL, Row.CREATE_NULL_AS_BLANK).toString().trim();
			
			userBean.setFirstName(firstName);
			userBean.setLastName(lastName);
			userBean.setEmail(email.toLowerCase());
			
			String userPresent = isUserContiainsFilteredUser(email);
			if(null!=userPresent){
				userBeans.add(userBean);
			}
			
			DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
			updateDB.updateUserDetails(userBeans);
		}
				
	}
	
	private static String isUserContiainsFilteredUser(String userEmail){
		String userId = null;
		for(UserBean user:filteredUser){
			if(user.getEmail().trim().equalsIgnoreCase(userEmail)){
				userId = user.getUserId();
				break;
			}
		}
		return userId;
	}
}
