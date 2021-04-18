package com.compli.util.datamigration.v2;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.db.bean.migration.v2.CompanyBean;
import com.compli.db.bean.migration.v2.FilesBean;
import com.compli.db.bean.migration.v2.LawMasterBean;
import com.compli.db.bean.migration.v2.LocationBean;
import com.compli.db.bean.migration.v2.PeriodicityDateMasterBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.bean.migration.v2.UserBean;
import com.compli.db.bean.migration.v2.UserCompanyBean;
import com.compli.db.dao.AcivityAssignmentDao;
import com.compli.db.dao.ActivityAssociationDao;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.ActivityMasterDao;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.FilesDao;
import com.compli.db.dao.LawMasterDao;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.db.dao.UserCompanyDao;
import com.compli.db.dao.UserDao;
import com.compli.util.bean.ActivityAssignnmentBean;
import com.google.api.services.drive.model.File;

public class DataBaseMigrationUtilV2UpdateDB {
	UserDao userDao;
	CompanyDao companyDao;
	LocationDao locationDao;
	PeriodicityMasterDao periodicityMasterDao;
	PeriodicityDateMasterDao periodicityDateMasterDao; 
	LawMasterDao lawMasterDao;
	ActivityMasterDao activityMasterDao;
	ActivityAssociationDao activityAssociationDao;
	ActivityDao activityDao;
	UserCompanyDao userCompanyDao;
	FilesDao filesDao;
	AcivityAssignmentDao acivityAssignmentDao;
	
	public DataBaseMigrationUtilV2UpdateDB() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");;
		this.userDao = (UserDao) ctx.getBean("udao");
		this.companyDao = (CompanyDao) ctx.getBean("companyDao");
		this.locationDao = (LocationDao) ctx.getBean("locationDao");
		this.periodicityMasterDao = (PeriodicityMasterDao) ctx.getBean("periodicityDao");
		this.periodicityDateMasterDao = (PeriodicityDateMasterDao) ctx.getBean("periodicityDateDao");
		this.lawMasterDao = (LawMasterDao) ctx.getBean("lawMasterDao");
		this.activityMasterDao = (ActivityMasterDao) ctx.getBean("acitvityMasterDao");
		activityAssociationDao = (ActivityAssociationDao) ctx.getBean("activityAssociationDao");
		activityDao = (ActivityDao) ctx.getBean("activityDao");
		userCompanyDao = (UserCompanyDao)ctx.getBean("userCompanyDao");
		filesDao = (FilesDao) ctx.getBean("filesDao");
		this.acivityAssignmentDao = (AcivityAssignmentDao)ctx.getBean("activityAssignementDao");
	}
	
	public int updateUserDB(List<UserBean> filteredUsers){
		int errorRow= 0;
		filteredUsers.forEach(user->{
			com.compli.db.bean.UserBean userBean = new com.compli.db.bean.UserBean();	
			userBean.setUserId(user.getUsername());
			userBean.setUserTypeId(user.getUserType());
			userBean.setEmail(user.getEmail());
			userBean.setPass(user.getPassword());
			userDao.insertUserValuesForUpload(userBean);
		});
		return 0;
	}	
	
	public int udpateCompanies(List<CompanyBean> filteredCompanyBeans){
		int len = filteredCompanyBeans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			com.compli.db.bean.CompanyBean companyBean = new com.compli.db.bean.CompanyBean();
			companyBean.setCompanyId(filteredCompanyBeans.get(i).getCompanyId());
			companyBean.setName(filteredCompanyBeans.get(i).getCompanyName());
			companyBean.setAbbriviation(filteredCompanyBeans.get(i).getCompanyAbbr());
			boolean isSuccess = companyDao.addCompanyForUpload(companyBean);
			if(!isSuccess){
				errorRow++;
			}
		}
		return errorRow;
	}
	
	public int updateLocation(List<LocationBean> locationBeans){
		int len = locationBeans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			com.compli.db.bean.LocationBean locationBean = new com.compli.db.bean.LocationBean();
			locationBean.setLocationId(locationBeans.get(i).getLocationId());
			locationBean.setLocationName(locationBeans.get(i).getLocationName());
			boolean isSucess = locationDao.addCompanyForUpload(locationBean);
			if(isSucess){
				errorRow++;
			}			
		}
		return errorRow;
	}
	
	public int updateCompanyLocationLocation(List<CompanyBean> companyBeans){
		int len = companyBeans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			CompanyBean companyBean = companyBeans.get(i);
			int locationLen = companyBean.getCompanyLocations().size();
			for(int j=0;j<locationLen;j++){
				boolean isSucess = locationDao.addCompanyLocationForUpload(companyBean.getCompanyLocations().get(j),companyBean.getCompanyId());
				if(isSucess){
					errorRow++;
				}			
			}
		}
		return errorRow;
	}
	
	public int updatePeriodicity(List<PeriodicityMasterBean> periodicityMasterBeans){
		int len = periodicityMasterBeans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			com.compli.db.bean.PeriodicityMasterBean periodicityMasterBean = new com.compli.db.bean.PeriodicityMasterBean();
			periodicityMasterBean.setPeriodicityId(periodicityMasterBeans.get(i).getPeriodicityId());
			periodicityMasterBean.setDescription(periodicityMasterBeans.get(i).getDescription());
			boolean isSucess = periodicityMasterDao.addPeriodicityMasterForUpload(periodicityMasterBean);
			if(isSucess){
				errorRow++;
			}			
		}
		return errorRow;
	}
	
	public int updatePeriodicityDateMaster(List<PeriodicityDateMasterBean> periodicityDateMasterBeans){
		int len = periodicityDateMasterBeans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			com.compli.db.bean.PeriodicityDateMasterBean periodicityDateMasterBean = new com.compli.db.bean.PeriodicityDateMasterBean();
			periodicityDateMasterBean.setPeriodicityDateId(periodicityDateMasterBeans.get(i).getPeriodicityDateId());
			periodicityDateMasterBean.setDueDate(periodicityDateMasterBeans.get(i).getDueDate());
			boolean isSucess = periodicityDateMasterDao.addPeriodicityMasterForUpload(periodicityDateMasterBean);
			if(isSucess){
				errorRow++;
			}			
		}
		return errorRow;
	}
	
	public int updateLawMaster(List<LawMasterBean> lawMasterBeans){
		int len = lawMasterBeans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			com.compli.db.bean.LawMasterBean lawMasterBean = new com.compli.db.bean.LawMasterBean();
			lawMasterBean.setLawId(lawMasterBeans.get(i).getLawId());
			lawMasterBean.setLawDesc(lawMasterBeans.get(i).getLawDesc());
			lawMasterBean.setLawName(lawMasterBeans.get(i).getLawName());
			boolean isSucess = lawMasterDao.addLawForUpload(lawMasterBean);
			if(isSucess){
				errorRow++;
			}			
		}
		return errorRow;
	}
	
	public int updateActivityMaster(List<ActivityMasterBean> activityBeans){
		int len = activityBeans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			boolean isSucess = this.activityMasterDao.addActivityForUpload(activityBeans.get(i));
			if(isSucess){
				errorRow++;
			}	
		}
		return errorRow;
	}
	
	public int updateActivityAssociation(List<ActivityAssociationBean> activityAssociationBeans){
		int len = activityAssociationBeans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			try{
			boolean isSucess = this.activityAssociationDao.addActivityAssociationForUpload(activityAssociationBeans.get(i));
			if(isSucess){
				errorRow++;
			}	
			}catch(Exception e){
				e.printStackTrace();
				System.out.println(activityAssociationBeans.get(i));
			}
		}
		return errorRow;
	}
	
	public int updateActivity(List<ActivityBean> activities){
		int len = activities.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			boolean isSucess = this.activityDao.addActivityForUpload(activities.get(i));
			if(isSucess){
				errorRow++;
			}	
		}
		return errorRow;
	}
	
	public int updateUserCompany(List<UserCompanyBean> companyBean){
		int len = companyBean.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			boolean isSucess = this.userCompanyDao.addUserCompanyForUpload(companyBean.get(i));
			if(isSucess){
				errorRow++;
			}	
		}
		return errorRow;
	}
	
	public int updateFiles(List<FilesBean> filesBean){
		int len = filesBean.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			boolean isSucess = this.filesDao.saveFile(filesBean.get(i).getActivityId(), filesBean.get(i).getCompanyId(), filesBean.get(i).getDocumentName(), filesBean.get(i).getDocumentFile(),filesBean.get(i).getCreatedOn());
			if(len%500==0){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(isSucess){
				errorRow++;
			}	
		}
		return errorRow;
	}
	
	public int updateFilesId(List<File> filesBean){
		int len = filesBean.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			boolean isSucess = this.filesDao.updateFileId(filesBean.get(i).getId(), filesBean.get(i).getName());
			if(!isSucess){
				System.out.println(filesBean.get(i).getName());
			}
			if(len%500==0){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(isSucess){
				errorRow++;
			}	
		}
		return errorRow;
	}
	
	public int updateUserDetails(List<com.compli.db.bean.UserBean> beans){
		int len = beans.size();
		int errorRow = 0;
		for(int i=0;i<len;i++){
			boolean isSucess = this.userDao.updateUserDetails(beans.get(i).getFirstName(), beans.get(i).getLastName(),beans.get(i).getEmail());
			if(!isSucess){
				System.out.println(beans.get(i).getFirstName());
			}
			if(len%500==0){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(isSucess){
				errorRow++;
			}	
		}
		return errorRow;
	}
	
	public void saveActivityAssignment(List<ActivityAssignnmentBean> actAssociationBean){
		this.acivityAssignmentDao.saveActivityAssignment(actAssociationBean);		
	}
	
	public void removeActivityAssignment(List<ActivityAssignnmentBean> actAssociationBean){
		this.acivityAssignmentDao.removeActivityAssignment(actAssociationBean);		
	}
}
