package com.compli.util;

import com.compli.bean.dashboard.DashboardparamsBean;

import java.text.SimpleDateFormat;
import java.util.*;

public class Util {
	public static String formLocationId(String location){
		return location.toLowerCase().replace(" ", "");
	}
	
	public static String getPeriodicityId(String periodicity){
		return periodicity.replace("'", "").replace(" ", "");
	}
	
	public static int getFinnancialYearForMonth(int month){
		int fyYear = Calendar.getInstance().get(Calendar.YEAR);
		//Added + 1 because MONTH start from 0
		int currentMonth =  Calendar.getInstance().get(Calendar.MONTH)+1; 
		if(currentMonth<4){
			fyYear = month>3?fyYear-1:fyYear;
		}else{
			fyYear = month>3?fyYear:fyYear+1;
		}		
		return fyYear;
	}

	public static int getFinancialYearForMonth(int month,String fyYearStr){
		fyYearStr = fyYearStr.split("-")[0];
		int fyYear = Integer.parseInt(fyYearStr);
		fyYear = month>3 ? fyYear : (fyYear+1);
		return fyYear;
	}
	
	public static int getFYForQuarter(int quarterInt, String yearStr){
		//For quarted 3(STARTS WITH 0 AND JAN TO MARCH IS LAST QUARTER) fy is for month jan other for may
		String yearStart = yearStr.split("-")[0];
		int year = Integer.parseInt(yearStart);
		if(quarterInt == 3){
			year++;
		}
		return year;
	}
	
	public static String getLastDateOfQuarter(int quarterNo, String yearStr){
		int year = getFYForQuarter(quarterNo, yearStr);
		String date = null;
		if(quarterNo==0){
			date = year+"-6-30";
		}else if(quarterNo==1){
			date = year+"-9-30";
		}else if(quarterNo==2){
			date = year+"-12-31";
		}else {
			date = year+"-3-31";
		}
		return date;
	}

	public static String getFirstDateOfQuarter(int quarterNo, String yearStr){
		int year = getFYForQuarter(quarterNo,yearStr);
		String date = null;
		if(quarterNo==0){
			date = year+"-04-01";
		}else if(quarterNo==1){
			date = year+"-07-01";
		}else if(quarterNo==2){
			date = year+"-10-01";
		}else {
			date = year+"-01-01";
		}
		return date;
	}
	
	public static String getPeriodicityDateId(String periodicityDate){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		periodicityDate = dateFormat.format(new Date(periodicityDate));
		/*periodicityDate = periodicityDate.replace("/", "-");
		List<String> date= Arrays.asList(periodicityDate.split("-"));
		String year = date.get(2);
		String month = date.get(1);
		String day = date.get(0);
		return year+"-"+month+"-"+day;*/
		return periodicityDate;
	}
	
	public static void main(String[] args) {
		int fyY = getFinnancialYearForMonth(4);
		System.out.println(fyY);
	}

	public static String getFyStartDateForForBean(DashboardparamsBean bean){
		if ("year".equalsIgnoreCase(bean.getView())){
			return bean.getYear().split("-")[0]+"-04-01";
		}else if("quarter".equalsIgnoreCase(bean.getView())){
			return getFirstDateOfQuarter(bean.getQuarter(),bean.getYear());
		}else if("month".equalsIgnoreCase(bean.getView())){
			return getFinancialYearForMonth(bean.getMonth(),bean.getYear())+"-"+bean.getMonth()+"-01";
		}else{
			//This is lowest possible date on tool
			return "2014-01-01";
		}
	}

	public static String getFyStartDateForDashboardOverviewBean(DashboardparamsBean bean){
		if ("year".equalsIgnoreCase(bean.getView())){
			return bean.getYear().split("-")[0]+"-04-01";
		}else if("quarter".equalsIgnoreCase(bean.getView())){
			return getFirstDateOfQuarter(bean.getQuarter(), bean.getYear());
		}else if("month".equalsIgnoreCase(bean.getView())){
			int fyYearForMonth = getFinancialYearForMonth(bean.getMonth(),bean.getYear());
			String dateStr = get3MonthOldDate(fyYearForMonth+"",bean.getMonth()+"");
			return dateStr;
		}else{
			// 3 month old date
			Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("IST"));
			// instance.get(Calendar.MONTH) month start at 0 hence adding 1
			String dateStr = get3MonthOldDate(instance.get(Calendar.YEAR)+"",(instance.get(Calendar.MONTH)+1)+"");
			return dateStr;
		}
	}

	public static String get3MonthOldDate(String year,String month){
		Date referenceDate = new Date();
		Calendar c = Calendar.getInstance();
		int yearInt = Integer.parseInt(year.split("-")[0]);
		int monthInt = Integer.parseInt(month);
		c.set(Calendar.YEAR,yearInt);
		c.set(Calendar.MONTH,monthInt);
		c.set(Calendar.DAY_OF_MONTH,1);
		c.add(Calendar.MONTH, -3);
		Date endDate = c.getTime();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(endDate);
	}

	public static String getFyLastDateForForBean(DashboardparamsBean bean){
		if ("year".equalsIgnoreCase(bean.getView())){
			return "20"+bean.getYear().split("-")[1]+"-03-31";
		}else if("quarter".equalsIgnoreCase(bean.getView())){
			return getLastDateOfQuarter(bean.getQuarter(), bean.getYear());
		}else if("month".equalsIgnoreCase(bean.getView())){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH,bean.getMonth()-1);
			return getFinancialYearForMonth((bean.getMonth()),bean.getYear())+"-"+bean.getMonth()+"-"+cal.getActualMaximum(Calendar.DATE);
		}else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(new Date());
		}
	}
}
