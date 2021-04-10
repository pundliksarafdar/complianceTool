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
import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.UserCompanyBean;
import com.compli.db.dao.AcivityAssignmentDao;
import com.compli.db.dao.ActivityAssociationDao;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.UserCompanyDao;
import com.compli.db.dao.UserDao;
import com.compli.util.bean.ActivityAssignnmentBean;

public class DBMigrationUtilV2ActivityAssignmentUpload {
	private static int SPN_USER = 11;
	private static int C_MANAGER = 12;
	private static int S_MANAGER = 13;
	private static int C_OWNER = 14;
	
	static UserDao userDao;
	static AcivityAssignmentDao acivityAssignmentDao;
	
	static List<ActivityAssignnmentBean> filteredActivityAssignement;
	static List<UserBean> allUsers;
	static List<ActivityAssignnmentBean> dbActivityAssignement;
	public DBMigrationUtilV2ActivityAssignmentUpload() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		userDao = (UserDao)ctx.getBean("udao");
		acivityAssignmentDao = (AcivityAssignmentDao)ctx.getBean("activityAssignementDao");
		
		dbActivityAssignement = acivityAssignmentDao.getActivityAssignment();
		allUsers = userDao.getAllData();				
	}
	
	public static void init(){
		new DBMigrationUtilV2ActivityAssignmentUpload();
	}
	
	public static void updateUserDetails(Sheet datatypeSheet,int activityCountStart){
		Iterator<Row> iterator = datatypeSheet.iterator();
		List<UserBean> userBeans = new ArrayList<UserBean>();
		boolean isFirst = false;
		List<ActivityAssignnmentBean> allActivityAssignnmentsSheet = new ArrayList<ActivityAssignnmentBean>();
		
		//Form all activity
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			
			Row currentRow = iterator.next();
			String userArTechEmail = currentRow.getCell(SPN_USER, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String userCManagerEmail = currentRow.getCell(C_MANAGER, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String userCOwnerEmail = currentRow.getCell(C_OWNER, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String userSManagerEmail = currentRow.getCell(S_MANAGER, Row.CREATE_NULL_AS_BLANK).toString().trim();
			String activityId =  activityCountStart+"";
			
			String userArTechUserId = getUserId(userArTechEmail);
			String userCMUserId = getUserId(userCManagerEmail);
			String userCOUserId = getUserId(userCOwnerEmail);
			String userSMUserId = getUserId(userSManagerEmail);
			ActivityAssignnmentBean activityAssignnmentBean;
			
			if(activityId!=null){
				if(userArTechUserId!=null){
					activityAssignnmentBean = new ActivityAssignnmentBean(userArTechUserId,activityId);
					allActivityAssignnmentsSheet.add(activityAssignnmentBean);
				}else{
					System.out.println("ArTech null");
				}
				
				if(userCMUserId!=null){
					activityAssignnmentBean = new ActivityAssignnmentBean(userCMUserId,activityId);
					allActivityAssignnmentsSheet.add(activityAssignnmentBean);
				}else{
					System.out.println("CM null");
				}
				
				if(userCOUserId!=null){
					activityAssignnmentBean = new ActivityAssignnmentBean(userCOUserId,activityId);
					allActivityAssignnmentsSheet.add(activityAssignnmentBean);
				}else{
					System.out.println("CO null");
				}
				
				if(userSMUserId!=null){
					activityAssignnmentBean = new ActivityAssignnmentBean(userSMUserId,activityId);
					allActivityAssignnmentsSheet.add(activityAssignnmentBean);
				}else{
					System.out.println("SM Null");
				}
			}
			activityCountStart++;
		}
		
		allActivityAssignnmentsSheet.removeAll(dbActivityAssignement);	
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.saveActivityAssignment(allActivityAssignnmentsSheet);				
	}
	
	
	
	private static String getUserId(String email){
		UserBean bean = allUsers.parallelStream().filter(user->{
			return user.getEmail().trim().equalsIgnoreCase(email);
		}).findFirst().orElse(null);
		
		return bean != null?bean.getUserId():null;
	}
}
