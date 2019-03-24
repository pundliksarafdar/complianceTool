package com.compli.managers;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private String userId;
	public static String newline = System.getProperty("line.separator");
	public ReportsManager() {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.companyDao = (CompanyDao)ctx.getBean("companyDao");
	}
	
	public ReportsManager(String auth) {
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");
		this.companyDao = (CompanyDao)ctx.getBean("companyDao");
		this.userId  = AuthorisationManager.cache.getIfPresent(auth).getUserId();
	}
	
	public ReportsManager(String location,String auth) {
		this.location = location;
		String path = getClass().getResource("/applicationContext.xml").getPath();
		ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");
		this.dashBoardDao = (DashBoardDao) ctx.getBean("dashBoardDao");		
		this.userId  = AuthorisationManager.cache.getIfPresent(auth).getUserId();
		this.companyDao = (CompanyDao)ctx.getBean("companyDao");
	}
	
	public HashMap<String,Object> getReportsObject(String companyId,String month){
		 HashMap<String,Object> reportsMap = new HashMap<>();
		 List<Map<String, Object>> activities = null;
		 String locationH = (this.location==null)?"all":this.location;		 
		 activities = getAllActivitiesWithDescriptionForCompanyByMonth(companyId, month,locationH,this.userId);
		 Map<String, Map<String, Integer>> riskCount = getComplainceOverviewByRisk(activities);
		 
		 reportsMap.put("activities", activities);
		 reportsMap.put("riskCount", riskCount);
		 return reportsMap;
	}
	
	public HashMap<String,Object> getReportsObjectByQuarter(String companyId,String quarter){
		 HashMap<String,Object> reportsMap = new HashMap<>();
		 List<Map<String, Object>> activities = null;
		 if(this.location==null){	
		 	activities = getAllActivitiesWithDescriptionForCompanyByQuarter(companyId, quarter,"all",this.userId);
		 }else{
			 activities = getAllActivitiesWithDescriptionForCompanyByQuarter(companyId, quarter,this.location,this.userId);
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
			 activities = getAllActivitiesWithDescriptionForCompanyByYear(companyId, year,"all");
		 }else{
			 activities = getAllActivitiesWithDescriptionForCompanyByYear(companyId, year,this.location);
		 }
		 Map<String, Map<String, Integer>> riskCount = getComplainceOverviewByRisk(activities);
		 
		 reportsMap.put("activities", activities);
		 reportsMap.put("riskCount", riskCount);
		 return reportsMap;
	}
	
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId,String month,String userId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonth(companyId,month,userId,this.isFullUser,userId);
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId,String month,String location,String userId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByMonth(companyId,month,this.isFullUser,location,userId);
	}
	
	/*public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(String companyId,String quarter,String userId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,this.isFullUser,quarter,userId);
	}
	*/
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(String companyId,String quarter,String location,String userId){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByQuarter(companyId,this.isFullUser,quarter,location,userId);
	}
	
	/*public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByYear(String companyId,String year){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYear(companyId,year,this.isFullUser);
	}*/
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByYear(String companyId,String year,String location){
		return this.dashBoardDao.getAllActivitiesWithDescriptionForCompanyByYear(companyId,year,this.isFullUser,location,this.userId);
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
			}else if(null!=activity.get("isComplied") && "1".equals(activity.get("isComplied").toString())
					&& null!=activity.get("isComplianceApproved").toString() && "1".equals(activity.get("isComplianceApproved").toString())){
				complainceOverview.put(COMPLIED, complainceOverview.get(COMPLIED)+1);
			}
		}
		
		return complianceDetailByLaw;
	}
	
	public static String finnancialYear(){
		String finnacialYear = "";
		int monthInt = Calendar.getInstance().get(Calendar.MONTH);
		int currentYear = LocalDate.now().getYear();
		if(monthInt>3){
			finnacialYear = currentYear + "-" + (currentYear+1);
		}else{
			finnacialYear = (currentYear-1) + "-" + (currentYear);
		}
		return finnacialYear;
	}
	
	public static String quarter(String quarterNum){
		String quarter = "April - June";
		switch (quarterNum) {
		case "0":
			quarter = "April - June";
			break;
		case "1":
			quarter = "July - September";
			break;
		case "2":
			quarter = "October - December";
			break;
		case "3":
			quarter = "January - March";
			break;
		default:
			break;
		}
		return quarter;
	}
	
	public Map<String,Object> generateReportNew(List<Map<String, Object>> compliedInTime,List<Map<String, Object>> compliedDelayed,
			List<Map<String, Object>> compliedOpen,List<GraphBean> graphBeans,List<ChartBean> chartBean,String companyId,String monthNum,String quarter,String year) throws  FileNotFoundException{
		String month = null,finnancialYear = null,monthEnd=null;
			if(monthNum!=null){
				monthEnd =month = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNum)-1];
				finnancialYear = finnancialYear();
			}else if(quarter!=null){
				//month = new DateFormatSymbols().getMonths()[Integer.parseInt(monthNum)-1];
				monthEnd = month = quarter(quarter);
				//Quarter finnacial year is always same so taking fy for may
				finnancialYear = finnancialYear();
			}else if(year!=null){
				month = "";
				finnancialYear = Integer.parseInt(year)+"-"+(Integer.parseInt(year)+1)+"";
				monthEnd = "31st of March'"+(Integer.parseInt(year)+1);
			}
		String[] companieids = companyId.split(",");
		
		List<String>companyNameList = new ArrayList<String>();
		for(String companieid:companieids){
			CompanyBean company = companyDao.getCompanyById(companieid);
			if(company!=null){
				companyNameList.add(company.getName());
			}
		}
		
		String companyName = String.join(",", companyNameList);
		String fileName = companyName+"-"+month+"("+finnancialYear+").pdf";
		byte[] pdfFile = null;
		Map<String, Object>resp = new HashMap<String, Object>(){{put("filename", fileName);}};
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			JRBeanCollectionDataSource beanCollectionDataAll = new JRBeanCollectionDataSource(new ArrayList(){{add(0);}});
			JRBeanCollectionDataSource beanCollectionDataSourceInTime = new JRBeanCollectionDataSource(compliedInTime);
	        JRBeanCollectionDataSource beanCollectionDataSourceInDelayed = new JRBeanCollectionDataSource(compliedDelayed);
	        //compliedOpen = compliedOpen.subList(5, 6);
	        JRBeanCollectionDataSource beanCollectionDataSourceInOpen = new JRBeanCollectionDataSource(compliedOpen);
	        JRBeanCollectionDataSource graphBeanDataSource = new JRBeanCollectionDataSource(graphBeans);
	        JRBeanCollectionDataSource chartBeanDataSource = new JRBeanCollectionDataSource(chartBean);
	        
	        params.put("CompliedInTimeDataSource", beanCollectionDataSourceInTime);
	        params.put("CompliedDelayedDataSource", beanCollectionDataSourceInDelayed);
	        params.put("CompliedOpenDataSource", beanCollectionDataSourceInOpen);
	        params.put("GraphDataSource", graphBeanDataSource);
	        params.put("ChartBeanDataSource", chartBeanDataSource);
	        params.put("TIME_PERIOD_MONTH",month);
	        params.put("TIME_PERIOD_MONTH_END",monthEnd);
	        params.put("COMPANY_NAME",companyName);
	        params.put("TIME_PERIOD",month+" ("+finnancialYear+")");
	        params.put("TIME_PERIOD_TYPE",monthNum!=null?"month":quarter!=null?"quarter":"year");
	        
	        JasperReport jasperCompliReport = JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/reports/ComplianceReport.jrxml"));
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperCompliReport, params,beanCollectionDataAll);

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        //JasperExportManager.exportReportToPdfFile(jasperPrint, "c:/report/report.pdf");
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
			if("1".equals(dt.get("isComplied").toString()) && "1".equals(dt.get("isComplianceApproved").toString()) && "0".equals(dt.get("isComplainceDelayed").toString())){
					dt.put("complianceState", "Complied activities");
					compliedInTime.add(dt);	
					if(dt.get("riskDes").toString().toLowerCase().equals("low")){
						lowComplied++;
					}else if(dt.get("riskDes").toString().toLowerCase().equals("medium")){
						mediumComplied++;
					}else if(dt.get("riskDes").toString().toLowerCase().equals("high")){
						highComplied++;
					}
			}else if("1".equals(dt.get("isComplainceDelayed").toString())){
				dt.put("complianceState", "Compliance delayed");
				compliedDelayed.add(dt);
				if(dt.get("riskDes").toString().toLowerCase().equals("low")){
					lowComplied++;
				}else if(dt.get("riskDes").toString().toLowerCase().equals("medium")){
					mediumComplied++;
				}else if(dt.get("riskDes").toString().toLowerCase().equals("high")){
					highComplied++;
				} 
			}else if("0".equals(dt.get("isComplied").toString())){
				dt.put("complianceState", "Open activities");
				try{
					String consequence = dt.get("consequence").toString();
					System.out.println("Consequences:before:###################"+consequence);
					consequence = consequence.replaceAll("\\r\\n|\\r|\\n", " ");
					System.out.println("Consequences:after:*****************"+consequence);
					dt.put("consequence",consequence);
					//dt.put("consequence", "Section 272A(2)(c) of Income TaxAct, 1961:Rs 100 per day from thedate of default or amount of taxdeductibleSection 271H of theIncome Tax Act,1961:In case offailure to furnish thestatement/furnishing the incorrectstatementChargeable with (a)Penalty between ten thousandrupees to one lakh rupeesSection234E of the Income Tax Act,1961:Incase of failure to furnish thestatementChargeable with(a) Penaltyof a sum of two hundred rupees forevery day subject to maximum of Section 272A(2)(c) of Income TaxAct, 1961:Rs 100 per day from thedate of default or amount of taxdeductibleSection 271H of theIncome Tax Act,1961:In case offailure to furnish thestatement/furnishing the incorrectstatementChargeable with (a)Penalty between ten thousandrupees to one lakh rupeesSection234E of the Income Tax Act,1961:Incase of failure to furnish thestatementChargeable with(a) Penaltyof a sum of two hundred rupees forevery day subject to maximum of");
				}catch(Exception e){e.printStackTrace();}
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
	
	public Map<String, Object> generateReport(String companyId,String month) throws FileNotFoundException{
		HashMap<String, Object> d = this.getReportsObject(companyId, month);
		
		List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("activities");
		HashMap<String, List> formattedDataMap = format(data);
		Map<String, Object> fileObject = this.generateReportNew(formattedDataMap.get("compliedInTime"),formattedDataMap.get("compliedDelayed"),
				formattedDataMap.get("compliedOpen"),formattedDataMap.get("graphBeans"),formattedDataMap.get("chartBeans"),companyId,month,null,null
				);	
		return fileObject;
	}
	
	public Map<String, Object> generateReportForQuarter(String companyId,String quarter) throws FileNotFoundException{
		HashMap<String, Object> d = this.getReportsObjectByQuarter(companyId, quarter);
		
		List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("activities");
		HashMap<String, List> formattedDataMap = format(data);
		Map<String, Object> fileObject = this.generateReportNew(formattedDataMap.get("compliedInTime"),formattedDataMap.get("compliedDelayed"),
				formattedDataMap.get("compliedOpen"),formattedDataMap.get("graphBeans"),formattedDataMap.get("chartBeans"),companyId,null,quarter,null
				);	
		return fileObject;
	}
	
	public Map<String, Object> generateReportForYear(String companyId,String year) throws FileNotFoundException{
		HashMap<String, Object> d = this.getReportsObjectByYear(companyId, year);
		
		List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("activities");
		HashMap<String, List> formattedDataMap = format(data);
		Map<String, Object> fileObject = this.generateReportNew(formattedDataMap.get("compliedInTime"),formattedDataMap.get("compliedDelayed"),
				formattedDataMap.get("compliedOpen"),formattedDataMap.get("graphBeans"),formattedDataMap.get("chartBeans"),companyId,null,null,year
				);	
		return fileObject;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		ReportsManager manager = new ReportsManager();
		manager.userId = "akshay";
		HashMap<String, Object> d = manager.getReportsObject("c42e21c7c70e439e", "1");
		
		List<Map<String, Object>> data = (List<Map<String, Object>>) d.get("activities");
		HashMap<String, List> formattedDataMap = format(data);
		manager.generateReportNew(formattedDataMap.get("compliedInTime"),formattedDataMap.get("compliedDelayed"),
				formattedDataMap.get("compliedOpen"),formattedDataMap.get("graphBeans"),formattedDataMap.get("chartBeans"),
				"c42e21c7c70e439e","1",null,null);		
	}
}

