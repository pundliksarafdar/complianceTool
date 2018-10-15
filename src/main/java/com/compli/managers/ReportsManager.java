package com.compli.managers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.datatype.DataTypes;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.tabulator.Column;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.BeanWithList;
import com.compli.db.dao.DashBoardDao;

public class ReportsManager {
	
	private String HIGH = "high";
	private  String MEDIUM = "medium";
	private String LOW = "low";
	
	private String COMPLIED = "complied";
	private String OPEN = "open";
	
	DashBoardDao dashBoardDao;
	private boolean isFullUser = true;
	private String location = null;
	
	public ReportsManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");		
	}
	
	public ReportsManager(String location) {
		this.location = location;
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");		
	}
	
	public HashMap<String,Object> getReportsObject(String companyId,String month){
		 HashMap<String,Object> reportsMap = new HashMap<>();
		 List<Map<String, Object>> activities = null;
		 if(this.location==null){		 
		 	activities = getAllActivitiesWithDescriptionForCompanyByMonth(companyId, month);
		 }else{
			 activities = getAllActivitiesWithDescriptionForCompanyByMonth(companyId, month,this.location);
		 }
		 Map<String, Map<String, Integer>> riskCount = getComplainceOverviewByRisk(activities);
		 
		 reportsMap.put("activities", activities);
		 reportsMap.put("riskCount", riskCount);
		 return reportsMap;
	}
	
	public HashMap<String,Object> getReportsObjectByQuarter(String companyId,String quarter){
		 HashMap<String,Object> reportsMap = new HashMap<>();
		 List<Map<String, Object>> activities = null;
		 if(this.location==null){	
		 	activities = getAllActivitiesWithDescriptionForCompanyByQuarter(companyId, quarter);
		 }else{
			 activities = getAllActivitiesWithDescriptionForCompanyByQuarter(companyId, quarter,this.location);
		 }
		 Map<String, Map<String, Integer>> riskCount = getComplainceOverviewByRisk(activities);
		 
		 reportsMap.put("activities", activities);
		 reportsMap.put("riskCount", riskCount);
		 return reportsMap;
	}
	
	
	public HashMap<String,Object> getReportsObjectByYear(String companyId,String year){
		 HashMap<String,Object> reportsMap = new HashMap<>();
		 List<Map<String, Object>> activities = null;
		 if(this.location==null){
			 activities = getAllActivitiesWithDescriptionForCompanyByYear(companyId, year);
		 }else{
			 activities = getAllActivitiesWithDescriptionForCompanyByYear(companyId, year,this.location);
		 }
		 Map<String, Map<String, Integer>> riskCount = getComplainceOverviewByRisk(activities);
		 
		 reportsMap.put("activities", activities);
		 reportsMap.put("riskCount", riskCount);
		 return reportsMap;
	}
	
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId,String month){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonth(companyId,month,this.isFullUser);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId,String month,String location){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonth(companyId,month,this.isFullUser,location);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(String companyId,String quarter){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,this.isFullUser,quarter);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(String companyId,String quarter,String location){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,this.isFullUser,quarter,location);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByYear(String companyId,String year){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYear(companyId,year,this.isFullUser);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByYear(String companyId,String year,String location){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYear(companyId,year,this.isFullUser,location);
	}
	
	public HashMap<String, Integer> getRiskCount(List<Map<String, Object>> activities){
		HashMap<String, Integer> riskCountMap = new HashMap<String, Integer>();
		riskCountMap.put(HIGH, 0);
		riskCountMap.put(MEDIUM, 0);
		riskCountMap.put(LOW, 0);
		for(int i=0;i<activities.size();i++){
			Map<String, Object> activity = activities.get(i);
			System.out.println(activity);
			String riskId = (String) activity.get("riskId");
			if(HIGH.equals(riskId)){
				riskCountMap.put(HIGH,riskCountMap.get(HIGH)+1);
			}else if(MEDIUM.equals(riskId)){
				riskCountMap.put(MEDIUM,riskCountMap.get(MEDIUM)+1);
			}else if(LOW.equals(riskId)){
				riskCountMap.put(LOW,riskCountMap.get(LOW)+1);
			}
			
		}
		return riskCountMap;
	}
	
	public Map<String,Map<String,Integer>> getComplainceOverviewByRisk(List<Map<String, Object>> allActivity){
		Map<String,Map<String,Integer>> complianceDetailByLaw = new HashMap<>();
		{
		/*********************************************************/
		Map<String,Integer> complainceOverview = new HashMap<>();
		complainceOverview.put(COMPLIED, 0);
		complainceOverview.put(OPEN, 0);
		complianceDetailByLaw.put("Low", complainceOverview);
		
		complainceOverview = new HashMap<>();
		complainceOverview.put(COMPLIED, 0);
		complainceOverview.put(OPEN, 0);
		complianceDetailByLaw.put("Medium", complainceOverview);
		
		complainceOverview = new HashMap<>();
		complainceOverview.put(COMPLIED, 0);
		complainceOverview.put(OPEN, 0);
		complianceDetailByLaw.put("High", complainceOverview);
		/******************************************************/
		}
		for(int i=0;i<allActivity.size();i++){
			Map<String,Object> activity = allActivity.get(i);
			String riskDes = (String) activity.get("riskDes");
			/*
			if(!complianceDetailByLaw.containsKey(riskDes)){
				Map<String,Integer> complainceOverview = new HashMap<>();
				complainceOverview.put(COMPLIED, 0);
				complainceOverview.put(OPEN, 0);
				complianceDetailByLaw.put(riskDes, complainceOverview);
			}
			*/
			Map<String,Integer>  complainceOverview = complianceDetailByLaw.get(riskDes);
			if(null==activity.get("isComplied") || "0".equals(activity.get("isComplied").toString())){
				complainceOverview.put(OPEN, complainceOverview.get(OPEN)+1);
			}else if(null!=activity.get("isComplied") && "1".equals(activity.get("isComplied").toString())){
				complainceOverview.put(COMPLIED, complainceOverview.get(COMPLIED)+1);
			}
		}
		
		return complianceDetailByLaw;
	}
	
	public boolean generateReport(List<Map<String, Object>> data) throws DRException, FileNotFoundException{
		try {
	        Map<String, Object> params = new HashMap<String, Object>();
	        JasperReport jasperReport = JasperCompileManager.compileReport("C:/report/report1.xml");
	        
	        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(data); 
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, beanCollectionDataSource);

	        JasperExportManager.exportReportToPdfFile(jasperPrint, "c:/report/report.pdf");
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println(e.getMessage());
	    }
	return true;
	}
	
	private static void format(List<Map<String, Object>> data){
		for(Map<String, Object> dt :data){
			if((Integer)dt.get("isComplied")==1 && (Integer)dt.get("isComplianceApproved")==1){
					dt.put("complianceState", "Complied activities");
			}else if((Integer)dt.get("isComplainceDelayed")==1){
				dt.put("complianceState", "Compliance delayed");
			}else if((Integer)dt.get("isComplied")==0){
				dt.put("complianceState", "Open activities");
			}
		}
	}
	
	public static void main(String[] args) throws DRException, FileNotFoundException {
		ReportsManager manager = new ReportsManager();
		HashMap<String, Object> d = manager.getReportsObject("11f90c2223744aba", "10");
		System.out.println(d);
		List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("activities");
		format(data);
		manager.generateReport(data);
		
	}
}

