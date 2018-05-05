package com.compli.managers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.management.RuntimeErrorException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.UserBean;
import com.compli.db.dao.UserDao;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class AuthorisationManager {
	
	static Cache<String, com.compli.db.bean.UserBean> cache = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).build();
	UserDao userDao;
	
	public AuthorisationManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.userDao = (UserDao) ctx.getBean("udao");	
	}
	public static com.compli.db.bean.UserBean getUserCatche(String sessionToken) throws ExecutionException{
		return cache.getIfPresent(sessionToken);
	}
	
	public static String setUserCache(UserBean userBean) throws ExecutionException{
		AuthorisationManager authorisationManager = new AuthorisationManager();
		com.compli.db.bean.UserBean userBean2 = authorisationManager.loginData(userBean.getUsername(), userBean.getPassword());
		String authId = null;
		if(userBean2!=null){
			authId = UUID.randomUUID().toString();
			cache.put(authId, userBean2);
		}
		return authId;
	}
	
	public static boolean isUserActive(UserBean userBean) throws ExecutionException{
		AuthorisationManager authorisationManager = new AuthorisationManager();
		com.compli.db.bean.UserBean userBean2 = authorisationManager.loginData(userBean.getUsername(), userBean.getPassword());
		return userBean2.isIsactive();
	}
	
	public com.compli.db.bean.UserBean loginData(String username,String password){
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.userDao = (UserDao) ctx.getBean("udao");
		return this.userDao.getUserData(username,password);		
	}
	
	public void saveUserData(){
		com.compli.db.bean.UserBean bean = new com.compli.db.bean.UserBean();
		bean.setCreatedBy("Hello");
		bean.setUserId("username");
		bean.setModifiedOn(new Date());
		this.userDao.insertUserValues(bean);
	}
	
	public boolean registerUser(com.compli.db.bean.UserBean registerBean){
		this.userDao.insertUserValues(registerBean);
		return true;
	}
	
	public List<Map<String, Object>> getCompanies(String userId){
		return this.userDao.geetUserCompanies(userId);
	}
	
}
