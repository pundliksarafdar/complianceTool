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

import com.compli.db.bean.migration.v2.LawMasterBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.LawMasterDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.util.Util;

public class DBMigrationUtilV2LawMaster {
	private static int LAW_DESC = 8;
	private static int COMPLAINCE_AREA = 5;
	private static List<String> LAW_NAME = new ArrayList<String>(){{
		add("");add("Direct Tax");add("Indirect Tax");add("Corporate Law");add("Labour Law");
	}};	
	static LawMasterDao lawMasterDao;
	
	public DBMigrationUtilV2LawMaster() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.lawMasterDao = (LawMasterDao) ctx.getBean("lawMasterDao");
	}
	
	public static void init(){
		new DBMigrationUtilV2LawMaster();
	}
	
	public static void init(int lawDesc,int complArea){
		LAW_DESC = lawDesc;
		COMPLAINCE_AREA = complArea;
		new DBMigrationUtilV2LawMaster();
	}
	
	public static void createLawMaster(Sheet datatypeSheet){
		Iterator<Row> iterator = datatypeSheet.iterator();
		List<com.compli.db.bean.migration.v2.LawMasterBean>lawMasterBeans = new ArrayList<com.compli.db.bean.migration.v2.LawMasterBean>(); 
		Set<com.compli.db.bean.migration.v2.LawMasterBean>lawMasterBeansSet = new HashSet<com.compli.db.bean.migration.v2.LawMasterBean>();
		boolean isFirst = false;
		//Form userBean from uploaded sheet
		while (iterator.hasNext()) {
			if(!isFirst){
				isFirst = true;
				iterator.next();
				continue;
			}
			
			Row currentRow = iterator.next();
			Cell lawDesc = currentRow.getCell(LAW_DESC, Row.CREATE_NULL_AS_BLANK);
			Cell complainceArea = currentRow.getCell(COMPLAINCE_AREA, Row.CREATE_NULL_AS_BLANK);
			if(complainceArea.toString().equals("") || complainceArea.toString().equals("<NULL>") || lawDesc.toString().equals("")){
				continue;
			}
			
			String lawName = "";
			if(complainceArea.getCellType()==Cell.CELL_TYPE_NUMERIC){
				lawName = LAW_NAME.get((int)complainceArea.getNumericCellValue());
			}else{
				lawName = complainceArea.getStringCellValue();
			}
			LawMasterBean lawMasterBean = new LawMasterBean(lawName.trim(), lawDesc.toString().trim());
			lawMasterBeans.add(lawMasterBean);
		}
		//Get unique items
		lawMasterBeansSet.addAll(lawMasterBeans);
		lawMasterBeans.clear();
		lawMasterBeans.addAll(lawMasterBeansSet);
		
		//Get all data from user table
		List<com.compli.db.bean.LawMasterBean> lawMasterDataDB = lawMasterDao.getAllLaw();
		List<LawMasterBean> filteredLawMasterData = lawMasterBeans.parallelStream().filter(lawMasterBean->{
			return !lawMasterDataDB.parallelStream().anyMatch(lawMasterDataDBObj->{
				return lawMasterDataDBObj.getLawName().equals(lawMasterBean.getLawName()) &&
						lawMasterDataDBObj.getLawDesc().equals(lawMasterBean.getLawDesc()); 
			});
		}).distinct().collect(Collectors.toList());
		
		DataBaseMigrationUtilV2UpdateDB updateDB = new DataBaseMigrationUtilV2UpdateDB();
		updateDB.updateLawMaster(filteredLawMasterData);
		System.out.println(filteredLawMasterData);
	}
	
}
