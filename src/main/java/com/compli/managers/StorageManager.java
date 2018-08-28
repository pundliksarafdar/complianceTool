package com.compli.managers;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.Files;
import com.compli.db.dao.FilesDao;

public class StorageManager {
	private static FilesDao filesDao;
	public StorageManager() {
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		filesDao = (FilesDao) ctx.getBean("filesDao");
	}
	
	public List<Map<String, Object>> getFilesForActivity(String activityId, String companyId) {
		return filesDao.getFilesForActivity(activityId, companyId);
	}
	
	public List<Files> getFileById(String fileId) {
		return filesDao.getFileById(fileId);
	}
	
	public boolean saveFile(String activityId,String companyId,String fileId,String filename) {
		return filesDao.saveFile(activityId, companyId, fileId, filename);		
	}
}
