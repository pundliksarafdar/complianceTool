package com.compli.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.compli.bean.company.AddCompany;
import com.compli.bean.registration.GoogleRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.CompanyBean;
import com.compli.bean.UserBean;
import com.compli.db.bean.CompanyLocationBean;
import com.compli.db.bean.UserCompanyBean;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.UserDao;
import com.notifier.SendMailSSL;

public class RegistrationManager {
	private UserDao userDao;
	private CompanyDao companyDao;
	public RegistrationManager() {
		ApplicationContext ctx = DaoManager.getApplicationContext();
		userDao = (UserDao) ctx.getBean("udao");
		this.companyDao = (CompanyDao) ctx.getBean("companyDao");
		
	}
	
	public boolean updateCompanyDetails(String name,String abbr,String id){
		return this.companyDao.updateCompanyDetails(name, abbr, id);
	}
	public boolean registerUser(com.compli.db.bean.UserBean userBean){
		boolean isSuccess = this.userDao.insertUserValues(userBean);
		if(isSuccess){
			SendMailSSL mailSSL = new SendMailSSL();
			//mailSSL.sendRegistrationMail(userBean.getRegId(), userBean.getUserId(), userBean.getEmail());
			mailSSL.reSendRegistrationMail(userBean.getRegId(), userBean.getUserId(), userBean.getEmail());
		}
		return isSuccess;
	}
	
	public boolean insertUserValuesForMaster(com.compli.db.bean.UserBean userBean,String companyId){
		boolean isAdded = false;
		isAdded = this.userDao.insertUserValuesForMaster(userBean);
		return isAdded;
	}
	
	public boolean isUserExists(String userid){
		return this.userDao.isUserExist(userid);
	}
	
	public boolean addCompany(CompanyBean companyBean,com.compli.db.bean.UserBean userBean){
		com.compli.db.bean.CompanyBean companyBeanDb = new com.compli.db.bean.CompanyBean();
		companyBeanDb.setName(companyBean.getCompanyName());
		boolean successFull = this.companyDao.addCompany(companyBeanDb);
		if(successFull){
			UserCompanyBean userCompanyBean = new UserCompanyBean();
			userCompanyBean.setUserId(userBean.getUserId());
			userCompanyBean.setCompanyId(companyBeanDb.getCompanyId());
			this.companyDao.setUserCompany(userCompanyBean);
			
			CompanyLocationBean companyLocationBean = new CompanyLocationBean();
			companyLocationBean.setCompanyId(userCompanyBean.getCompanyId());
			companyLocationBean.setLocationId(companyBean.getLocation());
			this.companyDao.setCompanyLocation(companyLocationBean);

		}
		return true;
	}
	
	public String addCompany(CompanyBean companyBean){
		com.compli.db.bean.CompanyBean companyBeanDb = new com.compli.db.bean.CompanyBean();
		companyBeanDb.setName(companyBean.getCompanyName());
		companyBeanDb.setAbbriviation(companyBean.getAbbr());
		boolean successFull = this.companyDao.addCompanyForMasterUser(companyBeanDb);
		return successFull?companyBeanDb.getCompanyId():null;
	}

	public boolean addCompany(AddCompany addCompany){
		CompanyBean companyBean = new CompanyBean();
		companyBean.setAbbr(addCompany.getAbbr());
		companyBean.setCompanyName(addCompany.getCompanyname());
		String comanyId = addCompany(companyBean);
		addCompany.setId(comanyId);
		//Add Head office location
		CompanyLocationBean locationBean = new CompanyLocationBean();
		locationBean.setCompanyId(addCompany.getId());
		locationBean.setLocationId(addCompany.getHeadQuarterLocation());
		this.companyDao.setCompanyLocation(locationBean,true);
		this.companyDao.setCompanyLocations(addCompany.getId(),addCompany.getBranchLocation());

		UserCompanyBean userCompanyBean = new UserCompanyBean();
		userCompanyBean.setCompanyId(addCompany.getId());
		userCompanyBean.setUserId(addCompany.getUserId());
		this.companyDao.setUserCompany(userCompanyBean);

		//Set activities for location
		List<String> locations = new ArrayList<>();
		locations.add(addCompany.getHeadQuarterLocation());
		boolean activities = MasterManager.startLoadingActivityForLocationForMonth(locations,addCompany.getId(),addCompany.getUserId());
		List<String> branchLocations = new ArrayList<>();
		branchLocations.addAll(addCompany.getBranchLocation());
		activities = MasterManager.startLoadingActivityForBranchLocationForMonth(branchLocations,addCompany.getId(),addCompany.getUserId());
		return true;
	}

	
	public boolean validateEmail(String registrationId) {
		return this.userDao.validateEmail(registrationId);		
	}
	public void sendActivationLinkFor(String auth) throws ExecutionException {
		com.compli.db.bean.UserBean userBean = AuthorisationManager.getUserCatche(auth);
		SendMailSSL mailSSL = new SendMailSSL();
		mailSSL.reSendRegistrationMail(userBean.getRegId(), userBean.getUserId(), userBean.getEmail());
	}
	
	public boolean updateUser(String auth,com.compli.db.bean.UserBean userBeanToUpdate) throws ExecutionException{
		com.compli.db.bean.UserBean userBean = AuthorisationManager.getUserCatche(auth);
		String googleIdOld = userBean.getGoogleId();
		userBean.setFirstName(userBeanToUpdate.getFirstName());
		userBean.setLastName(userBeanToUpdate.getLastName());
		userBean.setImage(userBeanToUpdate.getImage());
		userBean.setPhone(userBeanToUpdate.getPhone());
		userBean.setGoogleId(userBeanToUpdate.getGoogleId());
		if(userBeanToUpdate.getPass()!=null && !userBeanToUpdate.getPass().trim().isEmpty()){
			userBean.setPass(userBeanToUpdate.getPass());
		}
		
		boolean isSuccess = this.userDao.updateUserData(userBean);
		if(isSuccess){
			//Update event only if goole email is changed
			if(null==googleIdOld || !googleIdOld.equals(userBeanToUpdate.getGoogleId())){
				AlertsManager alertsManager = new AlertsManager();
				alertsManager.sendInitialCalendarEvents(userBeanToUpdate.getGoogleId());
			}
		}
		return isSuccess;
	}
	
	public boolean updateUserForMaster(String auth,com.compli.db.bean.UserBean userBeanToUpdate) throws ExecutionException{
		com.compli.db.bean.UserBean userBean = AuthorisationManager.getUserCatche(auth);
		String googleIdOld = userBean.getGoogleId();
		userBean.setFirstName(userBeanToUpdate.getFirstName());
		userBean.setLastName(userBeanToUpdate.getLastName());
		userBean.setPass(userBeanToUpdate.getPass());
		userBean.setEmail(userBeanToUpdate.getEmail());
		userBean.setPhone(userBeanToUpdate.getPhone());
		userBean.setUserTypeId(userBeanToUpdate.getUserTypeId());
		userBean.setPass(userBeanToUpdate.getPass());
		userBean.setUserId(userBeanToUpdate.getUserId());
		boolean isSuccess = this.userDao.updateUserDataForMaster(userBean);
		if(isSuccess){
			//Update event only if goole email is changed
			if(null==googleIdOld || !googleIdOld.equals(userBeanToUpdate.getGoogleId())){
				AlertsManager alertsManager = new AlertsManager();
				alertsManager.sendInitialCalendarEvents(userBeanToUpdate.getGoogleId());
			}
		}
		return isSuccess;
	}

	public boolean registerUserForGmailId(GoogleRegistrationBean bean){
		return this.userDao.createUserForGoogleId(bean);
	}
}
