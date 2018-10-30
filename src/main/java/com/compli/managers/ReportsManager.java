package com.compli.managers;

import java.io.FileNotFoundException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.compli.bean.ChartBean;
import com.compli.bean.GraphBean;
import com.compli.db.bean.CompanyBean;
import com.compli.db.dao.CompanyDao;
import com.compli.db.dao.DashBoardDao;

public class ReportsManager {
	
	private String HIGH = "high";
	private  String MEDIUM = "medium";
	private String LOW = "low";
	
	private String COMPLIED = "complied";
	private String OPEN = "open";
	
	DashBoardDao dashBoardDao;
	CompanyDao companyDao;
	private boolean isFullUser = true;
	private String location = null;
	
	public ReportsManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.companyDao = (CompanyDao)ctx.getBean("companyDao");
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
	
	public static String finnancialYear(String month){
		String finnacialYear = "";
		int monthInt = Integer.parseInt(month);
		int currentYear = LocalDate.now().getYear();
		if(monthInt>3){
			finnacialYear = currentYear + "-" + (currentYear+1);
		}else{
			finnacialYear = (currentYear-1) + "-" + (currentYear);
		}
		return finnacialYear;
	}
	
	public Map<String,Object> generateReportNew(List<Map<String, Object>> compliedInTime,List<Map<String, Object>> compliedDelayed,
			List<Map<String, Object>> compliedOpen,List<GraphBean> graphBeans,List<ChartBean> chartBean,String companyId,String monthNum) throws DRException, FileNotFoundException{
		String month = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNum)-1];
		String finnancialYear = finnancialYear(monthNum);
		CompanyBean company = companyDao.getCompanyById(companyId);
		String companyName = company.getName();
		
		String fileName = companyName+"-"+month+"("+finnancialYear+").pdf";
		byte[] pdfFile = null;
		Map<String, Object>resp = new HashMap<String, Object>(){{put("filename", fileName);}};
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			JRBeanCollectionDataSource beanCollectionDataAll = new JRBeanCollectionDataSource(new ArrayList(){{add(0);}});
			JRBeanCollectionDataSource beanCollectionDataSourceInTime = new JRBeanCollectionDataSource(compliedInTime);
	        JRBeanCollectionDataSource beanCollectionDataSourceInDelayed = new JRBeanCollectionDataSource(compliedDelayed);
	        JRBeanCollectionDataSource beanCollectionDataSourceInOpen = new JRBeanCollectionDataSource(compliedOpen);
	        JRBeanCollectionDataSource graphBeanDataSource = new JRBeanCollectionDataSource(graphBeans);
	        JRBeanCollectionDataSource chartBeanDataSource = new JRBeanCollectionDataSource(chartBean);
	        
	        params.put("CompliedInTimeDataSource", beanCollectionDataSourceInTime);
	        params.put("CompliedDelayedDataSource", beanCollectionDataSourceInDelayed);
	        params.put("CompliedOpenDataSource", beanCollectionDataSourceInOpen);
	        params.put("GraphDataSource", graphBeanDataSource);
	        params.put("ChartBeanDataSource", chartBeanDataSource);
	        params.put("TIME_PERIOD_MONTH",month);
	        params.put("COMPANY_NAME",companyName);
	        params.put("TIME_PERIOD",month+" ("+finnancialYear+")");
	        String path2 = "C:\\DDrive\\Source\\Compliance\\compliance-tool\\ComplianceToolServer\\ComplianceTool\\src\\main\\resources\\reports\\";
	        String path1 = "C:/report/reports/";
	        String path = path2;
	        JasperReport jasperCompliReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/reports/ComplianceReport.jrxml"));
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperCompliReport, params,beanCollectionDataAll);

//	        JasperExportManager.exportReportToPdfFile(jasperPrint, "c:/report/report.pdf");
	        pdfFile = JasperExportManager.exportReportToPdf(jasperPrint);
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println(e.getMessage());
	    }
		resp.put("pdfFile", pdfFile);
	return resp;
	}
	
	private static HashMap<String, List> format(List<Map<String, Object>> data){
		HashMap<String, List> dataMap = new HashMap<String, List>();
		List<Map<String, Object>> compliedInTime = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> compliedDelayed = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> compliedOpen = new ArrayList<Map<String,Object>>();
		
		int lowComplied = 0;
		int mediumComplied = 0;
		int highComplied = 0;
		
		int lowOpen = 0;
		int mediumOpen = 0;
		int highOpen = 0;
		
		for(Map<String, Object> dt :data){
			if((Integer)dt.get("isComplied")==1 && (Integer)dt.get("isComplianceApproved")==1 && (Integer)dt.get("isComplainceDelayed")==0){
					dt.put("complianceState", "Complied activities");
					compliedInTime.add(dt);	
					if(dt.get("riskDes").toString().toLowerCase().equals("low")){
						lowComplied++;
					}else if(dt.get("riskDes").toString().toLowerCase().equals("medium")){
						mediumComplied++;
					}else if(dt.get("riskDes").toString().toLowerCase().equals("high")){
						highComplied++;
					}
			}else if((Integer)dt.get("isComplainceDelayed")==1){
				dt.put("complianceState", "Compliance delayed");
				compliedDelayed.add(dt);
				if(dt.get("riskDes").toString().toLowerCase().equals("low")){
					lowComplied++;
				}else if(dt.get("riskDes").toString().toLowerCase().equals("medium")){
					mediumComplied++;
				}else if(dt.get("riskDes").toString().toLowerCase().equals("high")){
					highComplied++;
				}
			}else if((Integer)dt.get("isComplied")==0){
				dt.put("complianceState", "Open activities");
				compliedOpen.add(dt);
				if(dt.get("riskDes").toString().toLowerCase().equals("low")){
					lowOpen++;
				}else if(dt.get("riskDes").toString().toLowerCase().equals("medium")){
					mediumOpen++;
				}else if(dt.get("riskDes").toString().toLowerCase().equals("high")){
					highOpen++;
				}
			}else{
				System.out.println(dt);
			}
		}
		
		GraphBean low = new GraphBean("Low", lowOpen, lowComplied);
		GraphBean medium = new GraphBean("Medium", mediumOpen, mediumComplied);
		GraphBean high = new GraphBean("High", highOpen, highComplied);
		List<GraphBean> graphBeans = new ArrayList<GraphBean>(){{add(low);add(medium);add(high);}};
		
		ChartBean chartBeanLowOpen = new ChartBean("Low", "Open", lowOpen);
		ChartBean chartBeanMediumOpen = new ChartBean("Medium", "Open", mediumOpen);
		ChartBean chartBeanHighOpen = new ChartBean("High", "Open", highOpen);
		ChartBean chartBeanLowCompl = new ChartBean("Low", "Complied", lowComplied);
		ChartBean chartBeanMediumCompl = new ChartBean("Medium", "Complied", mediumComplied);
		ChartBean chartBeanHighCompl = new ChartBean("High", "Complied", highComplied);
		List<ChartBean> chartBeans = new ArrayList<ChartBean>(){{add(chartBeanLowOpen);add(chartBeanMediumOpen);add(chartBeanHighOpen);
		add(chartBeanLowCompl);add(chartBeanMediumCompl);add(chartBeanHighCompl);}};
		
		dataMap.put("compliedInTime", compliedInTime);
		dataMap.put("compliedDelayed", compliedDelayed);
		dataMap.put("compliedOpen", compliedOpen);
		dataMap.put("graphBeans", graphBeans);
		dataMap.put("chartBeans", chartBeans);
		
		return dataMap;
	}
	
	public Map<String, Object> generateReport(String companyId,String month) throws FileNotFoundException, DRException{
		ReportsManager manager = new ReportsManager();
		HashMap<String, Object> d = manager.getReportsObject(companyId, month);
		
		List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("activities");
		HashMap<String, List> formattedDataMap = format(data);
		Map<String, Object> fileObject = manager.generateReportNew(formattedDataMap.get("compliedInTime"),formattedDataMap.get("compliedDelayed"),
				formattedDataMap.get("compliedOpen"),formattedDataMap.get("graphBeans"),formattedDataMap.get("chartBeans"),companyId,month
				);	
		return fileObject;
	}
	
	public static void main(String[] args) throws DRException, FileNotFoundException {
		ReportsManager manager = new ReportsManager();
		HashMap<String, Object> d = manager.getReportsObject("ff2dbbe29f7d4073", "4");
		
		List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("activities");
		HashMap<String, List> formattedDataMap = format(data);
		manager.generateReportNew(formattedDataMap.get("compliedInTime"),formattedDataMap.get("compliedDelayed"),
				formattedDataMap.get("compliedOpen"),formattedDataMap.get("graphBeans"),formattedDataMap.get("chartBeans"),
				"ff2dbbe29f7d4073","4");
		
	}
}

