package com.compli.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class MasterDataUtil {

    public static enum PERIODICITY {
        MONTHLY,QUARTERLY,YEARLY
    }

    public static enum DUE_DATE {
        FIRST_DAY,LAST_DAY
    }

    public static String getDueDateForPeriodicity(Date date, PERIODICITY periodicity, DUE_DATE dueDate, String mmdd){
        String periodicityDate = null;
        switch (periodicity){
            case YEARLY:{
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                periodicityDate = getDateForYear(year,periodicity,dueDate,mmdd);
                break;
            }
            case QUARTERLY:{
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                periodicityDate = getDateForQuarter(periodicity,dueDate,mmdd);
                break;
            }
            case MONTHLY:{
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                periodicityDate = getDateForMonth(year,month,periodicity,dueDate,mmdd);
                break;
            }
        }
        return periodicityDate;

    }

    //mmdd format should be mm-dd
    //year is start year of financial year
    //mmdd should be mm-dd
    public static String getDateForYear(int year,PERIODICITY periodicity,DUE_DATE dueDate,String mmdd){
        String dateOfCompliance = "";
        if (dueDate!=null){
            if (DUE_DATE.FIRST_DAY.equals(dueDate)){
                dateOfCompliance = year + "-04-01";
            }else if (DUE_DATE.LAST_DAY.equals(dueDate)){
                //last day falls in next year
                dateOfCompliance = (year+1) + "-03-30";
            }else if (mmdd !=null){
                dateOfCompliance = year + "-" + mmdd;
            }
        }
        return dateOfCompliance;
    }

    //yearshould be start year of finnancial year , month should be in month in number like 01 for jan
    //mmdd should be mm-dd
    public static String getDateForMonth(int year,int month,PERIODICITY periodicity,DUE_DATE dueDate,String mmdd){
        String dateOfCompliance = "";
        Calendar inst = Calendar.getInstance();
        inst.set(Calendar.YEAR,year);
        inst.set(Calendar.MONTH,month);
        //Get last day of given year and month
        int lastDay = inst.getActualMaximum(Calendar.DATE);
        if (DUE_DATE.FIRST_DAY.equals(dueDate)){
            //Added 1 in month as month start from 0 here
            dateOfCompliance = year + "/" + (month+1) +"/01";
        }else if (DUE_DATE.LAST_DAY.equals(dueDate)){
            //Added 1 in month as month start from 0 here
            dateOfCompliance = year + "/" + (month+1) +"/"+lastDay;
        }else if (mmdd !=null){
            dateOfCompliance = year + "/" + mmdd;
        }
        return dateOfCompliance;
    }

    //TBD
    public static String getDateForQuarter(PERIODICITY periodicity, DUE_DATE dueDate, String mmdd){
        String dateOfCompliance = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        if (DUE_DATE.FIRST_DAY.equals(dueDate)){
            //Added 1 in month as month start from 0 here
            Date firstDay = getFirstDayOfQuarter(date);
            dateOfCompliance = simpleDateFormat.format(firstDay);
        }else if (DUE_DATE.LAST_DAY.equals(dueDate)){
            //Added 1 in month as month start from 0 here
            Date firstDay = getLastDayOfQuarter(date);
            dateOfCompliance = simpleDateFormat.format(firstDay);
        }else if (mmdd !=null){
            dateOfCompliance = 0 + "/" + mmdd;
        }
        return dateOfCompliance;
    }

    private static Date getFirstDayOfQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)/3 * 3);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    private static Date getLastDayOfQuarter(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)/3 * 3 + 2);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static void main(String[] args) {
        Date date = new Date("2019/11/01");
        String firstDayOfMonth = getDueDateForPeriodicity(date,PERIODICITY.MONTHLY,DUE_DATE.FIRST_DAY,null);
        String lastDayOfMonth = getDueDateForPeriodicity(date,PERIODICITY.MONTHLY,DUE_DATE.LAST_DAY,null);
        String specificDayOfMonth = getDueDateForPeriodicity(date,PERIODICITY.MONTHLY,null,"01-12");
        String lastDayOfQuarter = getDueDateForPeriodicity(date,PERIODICITY.QUARTERLY,DUE_DATE.LAST_DAY,null);
        String firstayOfQuarter = getDueDateForPeriodicity(date,PERIODICITY.QUARTERLY,DUE_DATE.FIRST_DAY,null);
/*
        System.out.println(firstDayOfMonth);
        System.out.println(lastDayOfMonth);
        System.out.println(specificDayOfMonth);

        ApplicationContext ctx=new ClassPathXmlApplicationContext("applicationContext.xml");;
        MasterDataDao masterDataDao = (MasterDataDao)ctx.getBean("masterDataDao");
        List<MasterDataBean> data = masterDataDao.getMasterDataLocations(new ArrayList<String>() {{
            add("bihar");
            add("assam");
        }});

        System.out.println(data);*//*
        List location = new ArrayList<String>() {{
            add("bihar");
            add("assam");
        }};
        getActivitiesForLocationsFromMasterData(location);*/
        List<String> locationId = new ArrayList<String>(){{add("assam");add("maharashtra");}};
        //boolean activities = MasterManager.startLoadingActivityForLocationForMonth(locationId,"fc8c2113080e4e66");
        
    }

    public static void getActivitiesForLocationsFromMasterData(List<String> locationid){

    }
    
    public static Enum getEnumFromString(Class enumClass,String enumName){
        Enum enm = null;
        try{
            enm = Enum.valueOf(enumClass, enumName);    
        }catch (Exception e){

        }
        return enm;
    }
}