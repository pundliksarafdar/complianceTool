package com.compli.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.compli.util.bean.CompanyBean;
import com.compli.util.bean.UserBean;
import com.compli.util.bean.UserCompanyBeans;
import com.compli.util.bean.UserTypeBeans;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataMigratorUtil {
	
	static int PERIODICITY_ID = 13;
	static  int PERIODICITY_DATE_ID = 12;
	static int LOCATION = 3;
	static int LAW_NAME = 5;
	static int ACT_NAME = 6;
	static int ACT_DSC = 7;
	static int LAW_DSC = 5;
	static int RISK_ID = 9;
	static int CONSEQUENCS = 10;
	static int ABBR = 2;
	static int COMPL_STATUS = 15;
	static int REMARK = 17;
	static int ASSIGNED_USR = 16;
	static int C_MANAGER=19;
	static int S_MANAGER = 20;
	static int C_OWNER = 21;
	static{
	 
	}
	public static List<List<String>> readFile() throws FileNotFoundException{
		
		List<List<String>> userIds = new ArrayList<List<String>>();
		List<List<String>> vvBeans = null;
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\maydata11.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		int intCount = 0;
		try {
			String jsonStr = ""; 
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
				List<String> list = Arrays.asList(line.split(","));
				String datas= "'"+getLawId(list.get(LAW_NAME),list.get(LOCATION),list.get(ACT_NAME))+"','"+
						list.get(ACT_NAME)+"','"+list.get(ACT_DSC).replaceAll("'", "\\'")+"','"+getPeriodicityId((String)list.get(PERIODICITY_ID))+"','"+
						list.get(RISK_ID).toLowerCase()+"','"+list.get(CONSEQUENCS).replaceAll("'", "\\'")+"','"+getPeriodicityDateId((String)list.get(PERIODICITY_DATE_ID))+"'";
				String sqlStr = "INSERT INTO `compli`.`activitymaster` (`activityId`, `lawId`, `activityName`, `description`, `periodicityId`, `riskId`, `consequence`, `periodicityDateId`) VALUES (COUNT,ALL_VALUES);";
				sqlStr = sqlStr.replace("ALL_VALUES", datas);
				sqlStr = sqlStr.replace("COUNT", ""+intCount++);
				System.out.println(sqlStr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			return vvBeans;
		}
	}
	
	public static void insertInToLawMaster() throws FileNotFoundException{
		try {
			System.setOut(new PrintStream(new FileOutputStream("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\lawMaster.txt")));	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\db_tracker.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		try {
			List<String>lawIds = new ArrayList<String>();
			String jsonStr = ""; 
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
					List<String> list = Arrays.asList(line.split(","));
					//String sqlStr = "INSERT INTO `compli`.`lawmaster`(`lawId`,`lawName`,`lawDesc`)VALUES(ALL_VALUES);";
					String sqlStr = "UPDATE `compli`.`lawmaster`SET `lawName` = 'LAW_NAME',`lawDesc` = 'LAW_DESC' WHERE `lawId` = 'LAW_ID';";
					
					if(!lawIds.contains(getLawId(list.get(LAW_NAME),list.get(LOCATION),list.get(ACT_NAME)))){
						String datas= "'"+getLawId(list.get(LAW_NAME),list.get(LOCATION),list.get(ACT_NAME))+"','"+list.get(LAW_NAME)+"','"+list.get(LAW_DSC)+"'";
						sqlStr = sqlStr.replace("LAW_NAME", list.get(LAW_NAME).replace("'", "\\\\'"));
						sqlStr = sqlStr.replace("LAW_DESC", list.get(LAW_DSC).replace("'", "\\\\'"));
						sqlStr = sqlStr.replace("LAW_ID", getLawId(list.get(LAW_NAME),list.get(LOCATION),list.get(ACT_NAME)));
						System.out.println(sqlStr);
						lawIds.add(getLawId(list.get(LAW_NAME),list.get(LOCATION),list.get(ACT_NAME)));
					}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertInToPeriodicityMaster() throws FileNotFoundException{
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\maydata11.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		try {
			List<String>periodicityIds = new ArrayList<String>();
			String jsonStr = ""; 
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
					List<String> list = Arrays.asList(line.split(","));
					String sqlStr = "INSERT INTO `compli`.`periodicitymaster`(`periodicityId`,`description`)VALUES(ALL_VALUES);";
					if(!periodicityIds.contains(getPeriodicityId((String)list.get(PERIODICITY_ID)))){
						String datas= "'"+getPeriodicityId((String)list.get(PERIODICITY_ID))+"','"+(String)list.get(PERIODICITY_ID).replace("'", "\\'")+"'";
						sqlStr = sqlStr.replace("ALL_VALUES", datas);
						System.out.println(sqlStr);
						periodicityIds.add(getPeriodicityId((String)list.get(PERIODICITY_ID)));
					}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertInToPeriodicityDateMaster() throws FileNotFoundException{
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\maydata11.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		try {
			List<String>periodicityIds = new ArrayList<String>();
			String jsonStr = ""; 
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
					List<String> list = Arrays.asList(line.split(","));
					String sqlStr = "INSERT INTO `compli`.`periodicitydatemaster`(`periodicityDateId`,`dueDate`)VALUES(ALL_VALUES);";
					
					if(!periodicityIds.contains(getPeriodicityDateId((String)list.get(PERIODICITY_DATE_ID)))){
						String datas= "'"+getPeriodicityDateId((String)list.get(PERIODICITY_DATE_ID))+"','"+getPeriodicityDateId((String)list.get(PERIODICITY_DATE_ID))+"'";
						sqlStr = sqlStr.replace("ALL_VALUES", datas);
						System.out.println(sqlStr);
						periodicityIds.add(getPeriodicityDateId((String)list.get(PERIODICITY_DATE_ID)));
					}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertInToActivityAsociation() throws FileNotFoundException{
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\maydata11.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		try {
			List<String>periodicityIds = new ArrayList<String>();
			String jsonStr = ""; 
			int count = 0;
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
					List<String> list = Arrays.asList(line.split(","));
					String sqlStr = "INSERT INTO `compli`.`activityassociation`(`associationId`,`activityId`,`fyRecordId`,`locationId`)VALUES(COUNT,ALL_VALUES);";
					
					String datas= "'"+count+"','AAA','"+list.get(LOCATION).toLowerCase().replace(" ","")+"'";
					sqlStr = sqlStr.replace("ALL_VALUES", datas);
					sqlStr = sqlStr.replace("COUNT", ""+count++);
					System.out.println(sqlStr);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws FileNotFoundException {
		//readFile();
		//insertInToLawMaster();
		//insertInToPeriodicityMaster();
		//insertInToPeriodicityDateMaster();
		//insertInToActivityAsociation();
		//insertInToUser();
		//insertInToCompany();
		//insertIntoActivityStatus();
		updateUserRoles();
	}
	
	public static void updateUserRoles() throws FileNotFoundException{
		try {
			System.setOut(new PrintStream(new FileOutputStream("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\userRole.txt")));	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<List<String>> userIds = new ArrayList<List<String>>();
		String sql = "UPDATE `compli`.`user` SET `userTypeId` = 'USERTYPE_PH' WHERE `email` = 'EMAIL_PH';";
		List<List<String>> vvBeans = null;
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\db_tracker.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		try {
			List<List<String> > allCom = new ArrayList<List<String> >();
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
				List<String> list = Arrays.asList(line.split(","));
				allCom.add(list);
			}
			List<String> allUserRole = new ArrayList<String>();
			for(List<String> comp:allCom){
				String cManager = comp.get(C_MANAGER);
				String sManager = comp.get(S_MANAGER);
				String cOwner = comp.get(C_OWNER);
				String sql1 = sql.replace("USERTYPE_PH", "cManager").replace("EMAIL_PH", cManager);
				String sql2 = sql.replace("USERTYPE_PH", "cOwner").replace("EMAIL_PH", cOwner);
				String sql3 = sql.replace("USERTYPE_PH", "sManager").replace("EMAIL_PH", sManager);
				if(!allUserRole.contains(sql1)){
					System.out.println(sql1);
					allUserRole.add(sql1);
				}
				if(!allUserRole.contains(sql2)){
					System.out.println(sql2);
					allUserRole.add(sql2);
				}
				if(!allUserRole.contains(sql3)){
					System.out.println(sql3);
					allUserRole.add(sql3);
				}
								
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
	
	}
	
	public static void insertIntoActivityStatus() throws FileNotFoundException{
		List<List<String>> userIds = new ArrayList<List<String>>();
		List<List<String>> vvBeans = null;
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\db_tracker.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		String sql = "INSERT INTO `compli`.`activity`(`activityId`,`companyId`,`isComplied`,`isComplianceApproved`,`isProofRequired`,`isComplianceRejected`,`isComplainceDelayed`,`remark`,`assignedUser`)VALUES(ALL_VALUES);";
		HashMap<String, String>compIdMap = new HashMap<String, String>();
		compIdMap.put("ISD", "039cdb85d9ca4481");
		compIdMap.put("KIN", "050a18deb5a24f0e");
		compIdMap.put("DEM", "0c546bd6a8894373");
		compIdMap.put("DEM", "11f90c2223744aba");
		compIdMap.put("BMG", "3bc198594ea545b9");
		compIdMap.put("OTY", "48a4c1df25244098");
		compIdMap.put("AMI", "54d71958a84f4cfe");
		compIdMap.put("ALC", "5cc29e07592b43ee");
		compIdMap.put("EDT", "6c9157a77a5c424a");
		compIdMap.put("demo", "8841d5c45d6d4daf");
		compIdMap.put("STS", "d333b29b328b4036");
		compIdMap.put("NEST", "f1e3a44e4c094aaa");
		try {
			String jsonStr = ""; 
			int countStart = 68;
			List<List<String> > allCom = new ArrayList<List<String> >();
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
				List<String> list = Arrays.asList(line.split(","));
				allCom.add(list);
			}
			for(List<String> comp:allCom){/*
				System.out.print(compIdMap.get(comp.get(ABBR)) + "-");
				System.out.print(countStart + "-");
				System.out.println(comp.get(COMPL_STATUS));*/
				String allData = "'"+countStart+"','"+compIdMap.get(comp.get(ABBR))+"',";
				//,`isComplied`,`isComplianceApproved`,`isProofRequired`,`isComplianceRejected`,`isComplainceDelayed`
				if("Complied - In Time".equals(comp.get(COMPL_STATUS))){
					allData += "true,true,false,false,false";
				}else if("Pending Review".equals(comp.get(COMPL_STATUS))){
					allData += "true,false,false,false,false";
				}else if("Pending Compliance".equals(comp.get(COMPL_STATUS))){
					allData += "false,false,false,false,false";
				}else if("Complied - Delayed".equals(comp.get(COMPL_STATUS))){
					allData += "true,true,false,false,true";
				}else if("Not Due".equals(comp.get(COMPL_STATUS))){
					allData += "true,true,false,false,true";
				}
				countStart++;
				allData+=",'"+comp.get(REMARK).replaceAll("'", "\\\\'") +"'";
				allData+=",'"+comp.get(ASSIGNED_USR ).replaceAll("'", "\\\\'") +"'";
				System.out.println(sql.replace("ALL_VALUES", allData));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
	
	}
	
	
	
	public static void insertInToUser() throws FileNotFoundException{
		List<List<String>> userIds = new ArrayList<List<String>>();
		List<List<String>> vvBeans = null;
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\db_tracker.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		try {
			String jsonStr = ""; 
			int countStart = 0;
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
				List<String> list = Arrays.asList(line.split(","));
				List<String> users = new ArrayList<String>();
				users.addAll(list.subList(18, 22));
				userIds.add(users);
			}
			List<UserTypeBeans> uData = getUserData(userIds);
			
			for(int i =0;i<uData.size();i++){
				String datas= "'"+uData.get(i).getComplianceOwner().getUsername()+"','"+uData.get(i).getComplianceOwner().getFirstName()+"','"+uData.get(i).getComplianceOwner().getLastName()+
						"','"+uData.get(i).getComplianceOwner().getEmail()+"','"+uData.get(i).getComplianceOwner().getPhone()+"','"+uData.get(i).getComplianceOwner().getPassword()+"'";
				String sqlStr = "INSERT INTO `compli`.`user` (`userId`, `firstname`, `lastname`, `email`, `phone`, `pass`) VALUES (ALL_VALUES);";
				sqlStr = sqlStr.replace("ALL_VALUES", datas);
				System.out.println(sqlStr);
				
				datas= "'"+uData.get(i).getManager().getUsername()+"','"+uData.get(i).getManager().getFirstName()+"','"+uData.get(i).getManager().getLastName()+
						"','"+uData.get(i).getManager().getEmail()+"','"+uData.get(i).getManager().getPhone()+"','"+uData.get(i).getManager().getPassword()+"'";
				sqlStr = "INSERT INTO `compli`.`user` (`userId`, `firstname`, `lastname`, `email`, `phone`, `pass`) VALUES (ALL_VALUES);";
				sqlStr = sqlStr.replace("ALL_VALUES", datas);
				System.out.println(sqlStr);
				
				datas= "'"+uData.get(i).getSuperManager().getUsername()+"','"+uData.get(i).getSuperManager().getFirstName()+"','"+uData.get(i).getSuperManager().getLastName()+
						"','"+uData.get(i).getSuperManager().getEmail()+"','"+uData.get(i).getSuperManager().getPhone()+"','"+uData.get(i).getSuperManager().getPassword()+"'";
				sqlStr = "INSERT INTO `compli`.`user` (`userId`, `firstname`, `lastname`, `email`, `phone`, `pass`) VALUES (ALL_VALUES);";
				sqlStr = sqlStr.replace("ALL_VALUES", datas);
				sqlStr = sqlStr.replace("COUNT", "'"+countStart+++"'");
				System.out.println(sqlStr);
			}
			getUsersIds(uData);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
	
	}
	
	public static void insertInToCompany() throws FileNotFoundException{
		List<List<String>> userIds = new ArrayList<List<String>>();
		List<List<String>> vvBeans = null;
		ObjectMapper mapper = new ObjectMapper();
		InputStream inputStream = new FileInputStream(new File("C:\\DDrive\\Source\\Compliance\\compliance-tool\\document\\db_tracker.csv"));//Thread.currentThread().getContextClassLoader().getSystemResourceAsStream("G:\\corex\\ComplianceTool\\document\\DummyData1.csv");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(inputStreamReader);
		List<CompanyBean> companyBeans = new ArrayList<CompanyBean>();
		try {
			for (String line; (line = reader.readLine())!=null && line.length()!=0;) {
				List<String> list = Arrays.asList(line.split(","));
				CompanyBean companyBean = new CompanyBean();
				companyBean.setCompanyAbbr(list.get(2));
				companyBean.setCompanyName(list.get(1));
				List<String> userEmails = list.subList(19, 22);
				List<String> userNames = new ArrayList<String>();
				for(int i=0;i<3;i++){
					userNames.add(userEmails.get(i).split("@")[0]);
				}
				companyBean.getUserName().addAll(userNames);
				if(!companyBeans.contains(companyBean)){
					if(companyBeans.size()==10){
						//System.out.println("Size 7 debug");
					}
					companyBeans.add(companyBean);
				}
			}
			
			
			for(int i=0;i<companyBeans.size();i++){
				String datas= "'"+companyBeans.get(i).getCompanyId()+"','"+companyBeans.get(i).getCompanyName()+"','"+companyBeans.get(i).getCompanyAbbr()+"'";
				String sqlStr = "INSERT INTO `compli`.`company`(`companyId`,`name`,`abbriviation`)VALUES(ALL_VALUES);";
				sqlStr = sqlStr.replace("ALL_VALUES", datas);
				System.out.println(sqlStr);

				List<String> users = companyBeans.get(i).getUserName();
				List<UserCompanyBeans> userCompanyBeans = new ArrayList<UserCompanyBeans>();
				for(int j=0;j<3;j++){
					UserCompanyBeans userCompanyBean = new UserCompanyBeans();
					userCompanyBean.setUsername(users.get(j));
					userCompanyBean.setCompanyId(companyBeans.get(i).getCompanyId());
					userCompanyBeans.add(userCompanyBean);
				}
				for(int j=0;j<userCompanyBeans.size();j++){
					String datas2= "'"+userCompanyBeans.get(j).getUsername()+"','"+userCompanyBeans.get(j).getCompanyId()+"'";
					String sqlStr2 = "INSERT INTO `compli`.`usercompany`(`userId`,`companyId`)VALUES(ALL_VALUES);";
					sqlStr2 = sqlStr2.replace("ALL_VALUES", datas2);
					System.out.println(sqlStr2);
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
		}
	
	}
	
	public static String getPeriodicityId(String periodicity){
		return periodicity.replace("'", "").replace(" ", "");
	}
	
	public static String getPeriodicityDateId(String periodicityDate){
		periodicityDate = periodicityDate.replace("/", "-");
		List<String> date= Arrays.asList(periodicityDate.split("-"));
		String year = date.get(2);
		String month = date.get(1);
		String day = date.get(0);
		return year+"-"+month+"-"+day;
	}
	
	public static String getLawId(String lawName,String location,String lawDesc){
		String lawId = "";
		String lawNameCh = (lawName.substring(0, 1).toLowerCase() + lawName.substring(1)).replace(" ", "");
			if(location.toLowerCase().contains("utta") || location.toLowerCase().contains("assa")){
				lawNameCh += location.substring(0, 4);
			}else{
				lawNameCh += location.substring(0, 3);
			}
		return lawNameCh+lawDesc.substring(0, 5).replaceAll(" ", "");
	}
	  
	public static void getUsersIds(List<UserTypeBeans> userTypeBeans){
		Set<String> allUserNames = new HashSet<String>(); 
		int userSize = userTypeBeans.size();
		for(int index=0;index<userSize;index++){
			UserTypeBeans userType = userTypeBeans.get(index);
			allUserNames.add(userType.getComplianceOwner().getUsername());
			allUserNames.add(userType.getManager().getUsername());
			allUserNames.add(userType.getComplianceOwner().getUsername());
		}
		//System.out.println(allUserNames);
	}
	//Get all emails per rows and form data
	public static List<UserTypeBeans> getUserData(List<List<String>>userEmailsPerRow){
		List<UserTypeBeans> userTypeBeans = new ArrayList<UserTypeBeans>();
		for(int i=0;i<userEmailsPerRow.size();i++){
			List<String> userEmail = userEmailsPerRow.get(i);
			UserBean manager = getUserBean(userEmail.get(1));
			UserBean superManager = getUserBean(userEmail.get(2));
			UserBean complianceOwner = getUserBean(userEmail.get(3));
			
			UserTypeBeans userTypeBean = new UserTypeBeans(superManager, manager, complianceOwner);
			if(!userTypeBeans.contains(userTypeBean)){
				userTypeBeans.add(userTypeBean);
			}			
		}
		//System.out.println(userTypeBeans.size());
		return userTypeBeans;
	}
	
	public static UserBean getUserBean(String email){
		UserBean userBean = new UserBean();
		userBean.setEmail(email);
		userBean.setUsername(email.split("@")[0]);
		return userBean;
	}
}
