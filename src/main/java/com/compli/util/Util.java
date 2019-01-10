package com.compli.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
			fyYear = month>4?fyYear-1:fyYear;
		}else{
			fyYear = month>4?fyYear:fyYear+1;
		}		
		return fyYear;
	}
	
	public static int getFYForQuarter(int quarterInt){
		//For quarted 3(STARTS WITH 0 AND JAN TO MARCH IS LAST QUARTER) fy is for month jan other for may
		int year ;
		if(quarterInt == 3){
			year = Util.getFinnancialYearForMonth(1);
		}else{
			year = Util.getFinnancialYearForMonth(5);
		}
		return year;
	}
	
	public static String getLastDateOfQuarter(int quarterNo){
		int year = getFYForQuarter(quarterNo);
		String date = null;
		if(quarterNo==0){
			date = year+"-06-31";
		}else if(quarterNo==1){
			date = year+"-09-30";
		}else if(quarterNo==2){
			date = year+"-12-31";
		}else {
			date = year+"-03-31";
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
}
