package com.compli.util.datamigration.v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.Files;
import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.db.bean.migration.v2.FilesBean;
import com.compli.db.bean.migration.v2.LawMasterBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.ActivityAssociationDao;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.ActivityMasterDao;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.FilesDao;
import com.compli.db.dao.LawMasterDao;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.services.GoogleServices;
import com.compli.util.Util;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class DBMigrationUtilV2FIlesDriveRefresh {
	static FilesDao filesDao;
	public DBMigrationUtilV2FIlesDriveRefresh() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");;
		filesDao = (FilesDao) ctx.getBean("filesDao");
	}
	public static void init(){
		new DBMigrationUtilV2FIlesDriveRefresh();
	}
	
	public static void refreshFilesId(){
		List<File> filesAll = new ArrayList<File>();
		try {
			Drive dService = GoogleServices.getDriveService();
			FileList fileList;
			String tokenNxtPage = null;
				do{
					fileList = dService.files().list().setPageToken(tokenNxtPage).execute();
					tokenNxtPage = fileList.getNextPageToken();
					List<File> files = fileList.getFiles();
					filesAll.addAll(files);
				}while(fileList.getNextPageToken()!=null);
			} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(filesAll.size());
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateFilesId(filesAll);
	}
}
