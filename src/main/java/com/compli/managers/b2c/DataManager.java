package com.compli.managers.b2c;

import com.compli.bean.ActivityForAddNewActivity;
import com.compli.util.datamigration.v2.DBMigrationUtilV2ActivityMasterUpload;

import java.util.List;

public class DataManager {
    public static void setActivitiesForLocation(List<ActivityForAddNewActivity> activities){

    }

    public static void uploadActivityMaster(List<ActivityForAddNewActivity> activities,List<String> companyIds,int activityCount){
        System.out.println("Upload Activity master");
        int index=0;
        for(String companyId:companyIds){
            activityCount = activities.size()*index+activityCount;
            DBMigrationUtilV2ActivityMasterUpload.init(companyId);
            DBMigrationUtilV2ActivityMasterUpload.createActivityMaster(activities,activityCount);
            index++;
        }
    }
}
