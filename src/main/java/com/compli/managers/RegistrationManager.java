package com.compli.managers;

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
	public void registerUser(com.compli.db.bean.UserBean registerBean){
		this.userDao.insertUserValues(registerBean);
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
	
	public boolean validateEmail(String registrationId) {
		return this.userDao.validateEmail(registrationId);		
	}
}
