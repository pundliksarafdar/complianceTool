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

import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.util.Util;

public class DBMigrationUtilV2PeriodicityMaster {
	private static int PERIODICITY_DESC = 13;
	
	static PeriodicityMasterDao periodicityMasterDao;
	
	public DBMigrationUtilV2PeriodicityMaster() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.periodicityMasterDao = (PeriodicityMasterDao) ctx.getBean("periodicityDao");
	}
	
	public static void init(){
		new DBMigrationUtilV2PeriodicityMaster();
	}
	
	public static void createPeriodicityMaster(Sheet datatypeSheet){
		Iterator<Row> iterator = datatypeSheet.iterator();
		List<com.compli.db.bean.migration.v2.PeriodicityMasterBean> periodicityMasterBeans = new ArrayList<com.compli.db.bean.migration.v2.PeriodicityMasterBean>(); 
		Set<com.compli.db.bean.migration.v2.PeriodicityMasterBean> periodicityMasterBeansSet = new HashSet<com.compli.db.bean.migration.v2.PeriodicityMasterBean>();
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			
			Row currentRow = iterator.next();
			Cell periodictyDesc = currentRow.getCell(PERIODICITY_DESC, Row.CREATE_NULL_AS_BLANK);
			String periodicityId = Util.getPeriodicityId(periodictyDesc.toString());
			PeriodicityMasterBean periodicityMasterBean = new PeriodicityMasterBean(periodicityId, periodictyDesc.toString());
			periodicityMasterBeans.add(periodicityMasterBean);
		}
		//Get unique items
		periodicityMasterBeansSet.addAll(periodicityMasterBeans);
		periodicityMasterBeans.clear();
		periodicityMasterBeans.addAll(periodicityMasterBeansSet);
		
		//Get all data from user table
		List<com.compli.db.bean.PeriodicityMasterBean> periodicityMasterDataDB = periodicityMasterDao.getAllPeriodicty();
		List<PeriodicityMasterBean> filteredPeriMasterData = periodicityMasterBeans.parallelStream().filter(periodicityMasterBean->{
			return !periodicityMasterDataDB.parallelStream().anyMatch(periodicityMasterDataDBObj->{
				return periodicityMasterDataDBObj.getPeriodicityId().equals(periodicityMasterBean.getPeriodicityId());
			});
		}).distinct().collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updatePeriodicity(filteredPeriMasterData);
		System.out.println(filteredPeriMasterData);
	}
	
}
