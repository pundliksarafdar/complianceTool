package com.compli.managers;

import com.compli.bean.ActivityForAddNewActivity;
import com.compli.db.bean.MasterDataBean;
import com.compli.db.bean.migration.v2.PeriodicityMasterBean;
import com.compli.db.dao.ActivityDao;
import com.compli.db.dao.LocationDao;
import com.compli.db.dao.MasterDataDao;
import com.compli.db.dao.PeriodicityMasterDao;
import com.compli.util.MasterDataUtil;
import com.compli.util.Util;
import com.compli.util.datamigration.v2.DataBaseMigrationUtilV2UpdateDB;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.stream.Collectors;

public class MasterManager {
    MasterDataDao masterDataDao;
    static PeriodicityMasterDao periodicityMasterDao;
    ActivityDao activityDao;
    LocationDao locationDao;
    com.compli.db.dao.b2c.ActivityDao activityDaoB2c;


    public MasterManager(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        masterDataDao = (MasterDataDao)ctx.getBean("masterDataDao");
        this.periodicityMasterDao = (PeriodicityMasterDao) ctx.getBean("periodicityDao");
        this.activityDao = (ActivityDao) ctx.getBean("activityDao");
        this.locationDao = (LocationDao) ctx.getBean("locationDao");

        this.activityDaoB2c = DaoManager.getApplicationDao();
    }



    public static boolean startLoadingActivityForLocationForMonth(List<String> location,String companyId,String userId){
        MasterManager masterManager = new MasterManager();
        Date currentDate = new Date();
        List<MasterDataBean> mDataoforLoc = masterManager.masterDataDao.getMasterDataLocations(location);
        masterManager.setActivity(mDataoforLoc,companyId, userId);
        return true;
    }

    public static void main(String[] args) {
        List<String> location = new ArrayList<String>(){{add("assam");}};
        startLoadingActivityForLocationForMonth(location, "bb82ce817599494a", "");
    }

    public static boolean startLoadingActivityForBranchLocationForMonth(List<String> location,String companyId,String userId){
        MasterManager masterManager = new MasterManager();
        Date currentDate = new Date();
        List<MasterDataBean> mDataoforLoc = masterManager.masterDataDao.getMasterDataForBranchLocations(location);
        masterManager.setActivity(mDataoforLoc,companyId, userId);
        return true;
    }

    //This method is written for demo user when it creates id and company then all data will be added
    private void setActivity(List<MasterDataBean> mDataoforLoc,String compnyid,String userId){
        int maxActivityCount = activityDaoB2c.getMaximumActivityId();
        maxActivityCount++;
        List companies = new ArrayList(){{add(compnyid);}};
        List<ActivityForAddNewActivity> activities = new ArrayList<>();

        for (MasterDataBean masterDataBean : mDataoforLoc) {
            Enum periodicityEnm = MasterDataUtil.getEnumFromString(MasterDataUtil.PERIODICITY.class,masterDataBean.getPeriodicity());
            Enum dueDateEnum =  MasterDataUtil.getEnumFromString(MasterDataUtil.DUE_DATE.class,masterDataBean.getDueDate());
            String mmdd = null;
            if (dueDateEnum == null){
                mmdd = masterDataBean.getDueDate();
            }
            String periodicityDueDate = MasterDataUtil.getDueDateForPeriodicity(new Date(),(MasterDataUtil.PERIODICITY)periodicityEnm,
                    (MasterDataUtil.DUE_DATE)dueDateEnum,mmdd);

            ActivityForAddNewActivity activity = new ActivityForAddNewActivity(
                    masterDataBean.getLocationId(),
                    masterDataBean.getComplianceArea(),
                    masterDataBean.getActivityname(),
                    "description",
                    masterDataBean.getLawdescription(),
                    masterDataBean.getRisk(),
                    masterDataBean.getConsequences(),
                    masterDataBean.getForm(),
                    periodicityDueDate,
                    "Apr'14 to Mar'15"
            );
            activities.add(activity);
        }

        DataManager.setPeriodicityMaster(activities);
        DataManager.setPeriodicityDate(activities);
        DataManager.setLawMaster(activities);

        com.compli.managers.b2c.DataManager.uploadActivityMaster(activities,companies,maxActivityCount);
        DataManager.uploadActivityAssociation(activities,companies,maxActivityCount);
        DataManager.uploadActivity(activities,companies,maxActivityCount);

        int startCount = maxActivityCount;
        int endCount = maxActivityCount+activities.size();
        DataManager.assigneActivities(startCount,endCount,userId);

        int i =0;
    }


}
