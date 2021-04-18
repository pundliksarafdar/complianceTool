package com.compli.managers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.management.RuntimeErrorException;

import com.compli.bean.registration.GoogleRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.UserBean;
import com.compli.db.dao.UserDao;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class AuthorisationManager {
	
	static Cache<String, com.compli.db.bean.UserBean> cache = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.HOURS).build();
	UserDao userDao;
	
	public AuthorisationManager() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
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
			NotificationManager notificationManager = new NotificationManager();
			int unreadCount = notificationManager.getCountOfUnreadMessageForUser(userBean2.getUserId());
			authId = UUID.randomUUID().toString();
			userBean2.setUnreadMessage(unreadCount);
			cache.put(authId, userBean2);
		}
		return authId;
	}

	public static String setUserCacheForGmailLogin(String googleId) throws ExecutionException{
		AuthorisationManager authorisationManager = new AuthorisationManager();
		com.compli.db.bean.UserBean userBean2 = authorisationManager.loginDataForGoogleId(googleId);
		String authId = null;
		if(userBean2!=null){
			authId = UUID.randomUUID().toString();
			cache.put(authId, userBean2);
		}
		return authId;
	}

	public static void setUserBean(String auth,com.compli.db.bean.UserBean userBean){
		cache.put(auth,userBean);
	}
	
	public static boolean isUserActive(String authId) throws ExecutionException{
		com.compli.db.bean.UserBean userBean2 = cache.getIfPresent(authId);
		return userBean2.isIsactive()!=null && userBean2.isIsactive();
	}
	
	public static String getUserType(String authToken){
		try {
			return getUserCatche(authToken).getUserTypeId();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public com.compli.db.bean.UserBean loginData(String username,String password){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		this.userDao = (UserDao) ctx.getBean("udao");
		return this.userDao.getUserData(username,password);		
	}

	public com.compli.db.bean.UserBean loginDataForGoogleId(String googleId){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		this.userDao = (UserDao) ctx.getBean("udao");
		return this.userDao.getUserDataByGoogleId(googleId);
	}
	
	/*
	public boolean registerUser(com.compli.db.bean.UserBean registerBean){
		this.userDao.insertUserValues(registerBean);
		return true;
	}
	*/
	public List<Map<String, Object>> getCompanies(String userId){
		return this.userDao.geetUserCompanies(userId);
	}
	
}
