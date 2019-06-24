package com.compli.managers;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.UserBean;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.UserDao;

public class UserManager {
	private UserDao userDao;
	
	public UserManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		userDao = (UserDao) ctx.getBean("udao");
	}
	public List<UserBean> getAllUser(){
		return this.userDao.getAllData();
	}
}
