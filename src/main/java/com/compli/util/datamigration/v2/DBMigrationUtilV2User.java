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

import com.compli.db.bean.migration.v2.UserBean;
import com.compli.db.dao.UserDao;
import com.compli.util.UUID;

public class DBMigrationUtilV2User {
	
	static UserDao userDao;
	
	public DBMigrationUtilV2User() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");;
		this.userDao = (UserDao) ctx.getBean("udao");
	}
	
	private static int SPN_USER = 18;
	private static int C_MANAGER = 19;
	private static int S_MANAGER = 20;
	private static int C_OWNER = 21;

	public static void init(){
		new DBMigrationUtilV2User();
	}
	
	public static void createUsers(Sheet datatypeSheet){
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
			Row currentRow = iterator.next();
			Cell cell = currentRow.getCell(SPN_USER, Row.CREATE_NULL_AS_BLANK);
			if(cell.toString().trim().equals("")){
				continue;
			}
			userBeans.add(createUserBean(cell.toString(), "ArTechUser"));
			cell = currentRow.getCell(C_MANAGER, Row.CREATE_NULL_AS_BLANK);
			if(cell.toString().trim().equals("")){
				continue;
			}
			userBeans.add(createUserBean(cell.toString(), "cManager"));
			cell = currentRow.getCell(S_MANAGER, Row.CREATE_NULL_AS_BLANK);
			if(cell.toString().trim().equals("")){
				continue;
			}
			userBeans.add(createUserBean(cell.toString(), "sManager"));
			 cell = currentRow.getCell(C_OWNER, Row.CREATE_NULL_AS_BLANK);
			 if(cell.toString().trim().equals("")){
					continue;
				}
			 userBeans.add(createUserBean(cell.toString(), "cOwner"));
		}
		
		//Get all data from user table
		List<com.compli.db.bean.UserBean> allUserFromDb = userDao.getAllData();
		List<UserBean> filteredUsers = userBeans.parallelStream().filter(userBean->{
			return !allUserFromDb.parallelStream().anyMatch(userDb->{
				return userDb.getEmail().trim().toLowerCase().equals(userBean.getEmail().trim().toLowerCase());
			});
		}).distinct().collect(Collectors.toList());
		
		Set<UserBean> userSet = new HashSet<UserBean>();
		userSet.addAll(filteredUsers);
		System.out.println(userSet);
		filteredUsers.clear();
		filteredUsers.addAll(userSet);
		//System.out.println(filteredUsers);
		
		updateUserNameIfPresent(filteredUsers, allUserFromDb);
		System.out.println(filteredUsers);
		
		DataBaseMigrationUtilV2UpdateDB updateDb = new DataBaseMigrationUtilV2UpdateDB();
		updateDb.updateUserDB(filteredUsers);
	}
	
	private static UserBean createUserBean(String email,String userType){
		//Email should be always lower case
		email = email.toLowerCase();
		UserBean userBean = new UserBean();
		userBean.setEmail(email);
		userBean.setUsername(email.split("@")[0]);
		userBean.setUserType(userType);
		return userBean;
	}
	
	private static void updateUserNameIfPresent(List<UserBean> filteredUsers,List<com.compli.db.bean.UserBean> allUserFromDb){
		filteredUsers.forEach(filteredUser->{
			filteredUser.setUsername(getUniqueUserName(filteredUser.getUsername(), allUserFromDb));
		});
	}
	
	private static String getUniqueUserName(String username,List<com.compli.db.bean.UserBean> allUserFromDb){
		String correctedUserName = username.toLowerCase();
		
		if(correctedUserName.length()>16){
			correctedUserName = correctedUserName.substring(0, 16);
		}
		for(int i=0;i<allUserFromDb.size();i++){
			if(allUserFromDb.get(i).getUserId().equals(correctedUserName)){
				correctedUserName = UUID.getUID().substring(0, 1)+correctedUserName;
				return getUniqueUserName(correctedUserName, allUserFromDb);
			}
		}
		
		if(correctedUserName.length()>16){
			correctedUserName = correctedUserName.substring(0, 16);
		}
		return correctedUserName;
	}
}
