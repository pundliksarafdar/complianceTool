package com.compli.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.backup.User;
import com.compli.db.dao.BackupDao;


public class BackupManager {
	BackupDao backupDao; 
	
	public BackupManager() {
		ApplicationContext ctx = DaoManager.getApplicationContext();
		this.backupDao = (BackupDao)ctx.getBean("backupDao");
	}
	
	public static void main(String[] args) {
		BackupManager backupManager = new BackupManager();
		backupManager.startBackup();
	}

	private void startBackup(){
		 List<Object> userData = backupDao.getUserData();
		 try {
			createSheet(userData);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private HashMap<String, List<String>> createSheet(List<Object> object) throws IllegalArgumentException, IllegalAccessException{
		HashMap<String, List<String>>dataMap = new HashMap<String, List<String>>();
		for(Object obj:object){
			Field[] fields = obj.getClass().getFields();
			for (Field field:fields) {
				String fieldValue = (String) field.get(obj);
				String fieldName = field.getName();
				System.out.println(fieldName+"="+fieldValue);
				List<String>dataList = dataMap.get(fieldName);
				if(dataList == null){
					dataMap.put(fieldName, new ArrayList<String>());
					dataList = dataMap.get(fieldName);
				}
				dataList.add(fieldValue);
			}	
		}
		return dataMap;
	}
}


