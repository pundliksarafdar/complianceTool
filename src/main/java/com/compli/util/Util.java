package com.compli.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Util {
	public static String formLocationId(String location){
		return location.toLowerCase().replace(" ", "");
	}
	
	public static String getPeriodicityId(String periodicity){
		return periodicity.replace("'", "").replace(" ", "");
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
}
