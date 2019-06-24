package com.compli.managers;

import java.util.concurrent.ExecutionException;

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
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		userDao = (UserDao) ctx.getBean("udao");
		this.companyDao = (CompanyDao) ctx.getBean("companyDao");
		
	}
	public boolean registerUser(com.compli.db.bean.UserBean registerBean){
		return this.userDao.insertUserValues(registerBean);
	}
	
	public boolean insertUserValuesForMaster(com.compli.db.bean.UserBean userBean,String companyId){
		boolean isAdded = false;
		isAdded = this.userDao.insertUserValuesForMaster(userBean);
		
		if(isAdded){
			UserCompanyBean userCompanyBean = new UserCompanyBean();
			userCompanyBean.setUserId(userBean.getUserId());
			userCompanyBean.setCompanyId(companyId);
			this.companyDao.setUserCompany(userCompanyBean);
		}
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
			
			SendMailSSL mailSSL = new SendMailSSL();
			mailSSL.sendRegistrationMail(userBean.getRegId(), userBean.getUserId(), userBean.getEmail());
		}
		return true;
	}
	
	public boolean addCompany(CompanyBean companyBean){
		com.compli.db.bean.CompanyBean companyBeanDb = new com.compli.db.bean.CompanyBean();
		companyBeanDb.setName(companyBean.getCompanyName());
		companyBeanDb.setAbbriviation(companyBean.getAbbr());
		boolean successFull = this.companyDao.addCompanyForMasterUser(companyBeanDb);
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
		userBean.setGoogleId(userBeanToUpdate.getGoogleId());
		if(userBeanToUpdate.getPass()!=null && userBeanToUpdate.getPass().trim().isEmpty()){
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
}
