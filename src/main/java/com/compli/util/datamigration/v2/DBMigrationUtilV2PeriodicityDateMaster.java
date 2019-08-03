package com.compli.util.datamigration.v2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.ActivityForAddNewActivity;
import com.compli.db.bean.migration.v2.PeriodicityDateMasterBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.PeriodicityDateMasterDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.util.Util;

public class DBMigrationUtilV2PeriodicityDateMaster {
	private static int PERIODICITY_DATE = 12;
	
	static PeriodicityDateMasterDao periodicityDateMasterDao;
	
	public DBMigrationUtilV2PeriodicityDateMaster() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.periodicityDateMasterDao = (PeriodicityDateMasterDao) ctx.getBean("periodicityDateDao");
	}
	
	public static void init(){
		new DBMigrationUtilV2PeriodicityDateMaster();
	}
	
	public static void init(int rowNo){
		PERIODICITY_DATE = rowNo;
		new DBMigrationUtilV2PeriodicityDateMaster();
	}
	
	public static void createPeriodicityDateMaster(Sheet datatypeSheet){
		Iterator<Row> iterator = datatypeSheet.iterator();
		List<com.compli.db.bean.migration.v2.PeriodicityDateMasterBean> periodicityDateMasterBeans = new ArrayList<com.compli.db.bean.migration.v2.PeriodicityDateMasterBean>(); 
		Set<com.compli.db.bean.migration.v2.PeriodicityDateMasterBean> periodicityDateMasterBeansSet = new HashSet<com.compli.db.bean.migration.v2.PeriodicityDateMasterBean>();
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			
			Row currentRow = iterator.next();
			Cell periodictyDate = currentRow.getCell(PERIODICITY_DATE, Row.CREATE_NULL_AS_BLANK);
			if(periodictyDate.toString().trim().equals("") || periodictyDate.toString().trim().equals("<NULL>")){
				continue;
			}
			
			String periodicityDateId = Util.getPeriodicityDateId(periodictyDate.toString());
			PeriodicityDateMasterBean periodicityMasterBean = new PeriodicityDateMasterBean(periodicityDateId, periodicityDateId);
			periodicityDateMasterBeans.add(periodicityMasterBean);
		}
		//Get unique items
		periodicityDateMasterBeansSet.addAll(periodicityDateMasterBeans);
		periodicityDateMasterBeans.clear();
		periodicityDateMasterBeans.addAll(periodicityDateMasterBeansSet);
		
		//Get all data from user table
		List<com.compli.db.bean.PeriodicityDateMasterBean> periodicityDateMasterDataDB = periodicityDateMasterDao.getAllPeriodictyDate();
		List<PeriodicityDateMasterBean> filteredPeriMasterData = periodicityDateMasterBeans.parallelStream().filter(periodicityDateMasterBean->{
			return !periodicityDateMasterDataDB.parallelStream().anyMatch(periodicityDateMasterDataDBObj->{
				return periodicityDateMasterDataDBObj.getPeriodicityDateId().equals(periodicityDateMasterBean.getPeriodicityDateId());
			});
		}).distinct().collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updatePeriodicityDateMaster(filteredPeriMasterData);
		System.out.println(filteredPeriMasterData);
	}
	
	public static void createPeriodicityDateMaster(List<ActivityForAddNewActivity> activities){
		List<com.compli.db.bean.migration.v2.PeriodicityDateMasterBean> periodicityDateMasterBeans = new ArrayList<com.compli.db.bean.migration.v2.PeriodicityDateMasterBean>(); 
		Set<com.compli.db.bean.migration.v2.PeriodicityDateMasterBean> periodicityDateMasterBeansSet = new HashSet<com.compli.db.bean.migration.v2.PeriodicityDateMasterBean>();
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		for (ActivityForAddNewActivity activity : activities) {
			String periodicityDateId = Util.getPeriodicityDateId(activity.getPeriodicity());
			PeriodicityDateMasterBean periodicityMasterBean = new PeriodicityDateMasterBean(periodicityDateId, periodicityDateId);
			periodicityDateMasterBeans.add(periodicityMasterBean);
		}
		//Get unique items
		periodicityDateMasterBeansSet.addAll(periodicityDateMasterBeans);
		periodicityDateMasterBeans.clear();
		periodicityDateMasterBeans.addAll(periodicityDateMasterBeansSet);
		
		//Get all data from user table
		List<com.compli.db.bean.PeriodicityDateMasterBean> periodicityDateMasterDataDB = periodicityDateMasterDao.getAllPeriodictyDate();
		List<PeriodicityDateMasterBean> filteredPeriMasterData = periodicityDateMasterBeans.parallelStream().filter(periodicityDateMasterBean->{
			return !periodicityDateMasterDataDB.parallelStream().anyMatch(periodicityDateMasterDataDBObj->{
				return periodicityDateMasterDataDBObj.getPeriodicityDateId().equals(periodicityDateMasterBean.getPeriodicityDateId());
			});
		}).distinct().collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updatePeriodicityDateMaster(filteredPeriMasterData);
		System.out.println(filteredPeriMasterData);
	}
	
}
