package com.compli.db.dao;

import com.compli.util.Constants;
import com.compli.util.Util;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class RepositoryDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private String activityForMonthQueryFullUser =
			"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,allActivities.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate,activityStatus  from activity_assignment inner join "+
			"(select * from "+		
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate,activityStatus from activity inner join "+
			"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicitydatemaster.periodicityDateId,consequence,periodicitydatemaster.duedate from periodicitydatemaster inner join "+
				"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicitymaster.periodicityId,periodicitymaster.description as periodicityDesc,periodicityDateId,consequence from periodicitymaster inner join "+
					"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskmaster.riskId,riskmaster.description riskDes,periodicityId,periodicityDateId,consequence from riskmaster inner join "+
						"(select companyId,abbriviation,locationId,locationName,activityId,activityName,riskId,lawmaster.lawName,lawmaster.lawId,lawmaster.lawDesc,periodicityId,periodicityDateId,consequence from lawmaster inner join "+
							"(select companyId,abbriviation,lawId,locationId,locationName,activitymaster.activityId,activitymaster.activityName,riskId,periodicityId,periodicityDateId,consequence from activitymaster inner join "+
								"(select companyId,abbriviation,activityassociation.locationId,locationName,activityId from activityassociation inner join "+ 
									"(select companyId,abbriviation,location.locationId,locationName from location inner join	"+
										"(select cc.companyId,abbriviation,locationId from company cc inner join companylocation "+
										"on cc.companyId = companylocation.companyId where cc.companyId in (?)) companyWithLocation "+
									"on location.locationId = companyWithLocation.locationId) companyWithLocationName "+
								"on companyWithLocationName.locationId = activityassociation.locationId   and (activityassociation.locationId=:locationId or 'all'=:locationId)) companyWithActivityId "+
							"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
						"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
					"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
				"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
			"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and (activity.activityStatus in (:activityStatus) or 'all'=:activityStatusJoin) order by periodicityDateId desc) newtable where ((month(duedate)=:dueMonth and year(duedate)=:dueYear) and activityStatus!='pendingCompliance') or (DATE_FORMAT(duedate,'%Y%m')<=DATE_FORMAT(:dueDate,'%Y%m') and activityStatus='pendingCompliance'))allActivities "
		+ "on allActivities.activityId=activity_assignment.activityId where userId=:userId";
	

	/******************************* Activity for year *****************************************/
	private String activityForQuarterQueryFullUser =
			"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,allActivities.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,dueMonth,isComplied ,isComplianceApproved,isComplianceRejected,isComplainceDelayed,isProofRequired,reOpen,arTechRemark,assignedUser,remark,completionDate,activityStatus  from activity_assignment inner join "+
			"(select * from "+
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate,activityStatus from activity inner join "+
		"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicitydatemaster.periodicityDateId,consequence,periodicitydatemaster.duedate from periodicitydatemaster inner join "+
		"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicitymaster.periodicityId,periodicitymaster.description as periodicityDesc,periodicityDateId,consequence from periodicitymaster inner join "+
			"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskmaster.riskId,riskmaster.description riskDes,periodicityId,periodicityDateId,consequence from riskmaster inner join "+
				"(select companyId,abbriviation,locationId,locationName,activityId,activityName,riskId,lawmaster.lawName,lawmaster.lawId,lawmaster.lawDesc,periodicityId,periodicityDateId,consequence from lawmaster inner join "+
					"(select companyId,abbriviation,lawId,locationId,locationName,activitymaster.activityId,activitymaster.activityName,riskId,periodicityId,periodicityDateId,consequence from activitymaster inner join "+
						"(select companyId,abbriviation,activityassociation.locationId,locationName,activityId from activityassociation inner join "+ 
							"(select companyId,abbriviation,location.locationId,locationName from location inner join	"+
								"(select cc.companyId,abbriviation,locationId from company cc inner join companylocation "+
									"on cc.companyId = companylocation.companyId where cc.companyId in (?)) companyWithLocation "+
							"on location.locationId = companyWithLocation.locationId) companyWithLocationName "+
						"on companyWithLocationName.locationId = activityassociation.locationId  and (activityassociation.locationId=:locationId or 'all'=:locationId)) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and (activity.activityStatus in (:activityStatus) or 'all'=:activityStatusJoin) order by periodicityDateId desc) newtable  WHERE (quarter(duedate)=:dueQuarter and YEAR(duedate)=:dueYear and  activityStatus!='pendingCompliance') or (DATE_FORMAT(duedate,'%Y%m')<=DATE_FORMAT(:quarterLastDay,'%Y%m') and activityStatus='pendingCompliance'))allActivities "+
		 "on allActivities.activityId=activity_assignment.activityId where userId=:userId";


	private String activityForYearQueryFullUserWithLocation =
			"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,allActivities.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,dueMonth,isComplied ,isComplianceApproved,isComplianceRejected,isComplainceDelayed,isProofRequired,reOpen,arTechRemark,assignedUser,remark,completionDate,activityStatus  from activity_assignment inner join "+
					"(select * from "+
					"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate,activityStatus from activity inner join "+
					"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicitydatemaster.periodicityDateId,consequence,periodicitydatemaster.duedate from periodicitydatemaster inner join "+
					"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicitymaster.periodicityId,periodicitymaster.description as periodicityDesc,periodicityDateId,consequence from periodicitymaster inner join "+
					"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskmaster.riskId,riskmaster.description riskDes,periodicityId,periodicityDateId,consequence from riskmaster inner join "+
					"(select companyId,abbriviation,locationId,locationName,activityId,activityName,riskId,lawmaster.lawName,lawmaster.lawId,lawmaster.lawDesc,periodicityId,periodicityDateId,consequence from lawmaster inner join "+
					"(select companyId,abbriviation,lawId,locationId,locationName,activitymaster.activityId,activitymaster.activityName,riskId,periodicityId,periodicityDateId,consequence from activitymaster inner join "+
					"(select companyId,abbriviation,activityassociation.locationId,locationName,activityId from activityassociation inner join "+
					"(select companyId,abbriviation,location.locationId,locationName from location inner join	"+
					"(select cc.companyId,abbriviation,locationId from company cc inner join companylocation "+
					"on cc.companyId = companylocation.companyId where cc.companyId in (?)) companyWithLocation "+
					"on location.locationId = companyWithLocation.locationId) companyWithLocationName "+
					"on companyWithLocationName.locationId = activityassociation.locationId   and (activityassociation.locationId=:locationId or 'all'=:locationId)) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
					"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+
					"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
					"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+
					"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
					"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and (activity.activityStatus in (:activityStatus) or 'all'=:activityStatusJoin) order by periodicityDateId desc) newtable  WHERE ((DATE_FORMAT(duedate,'%Y%m') >= DATE_FORMAT(:fromYear,'%Y%m') and DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(:toYear,'%Y%m')) and activityStatus!='pendingCompliance') or (DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(:toYear,'%Y%m') and activityStatus='pendingCompliance'))allActivities "
					+ "on allActivities.activityId=activity_assignment.activityId where userId=:userId";


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}


	/*	If year is null then current finnancial year will be considered
	 * other wise for year and month data will be returned
	 */
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthAndYear(String companyId, String month,String year,String userId,boolean isFullUser,String location,List<String> activityStatus) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		int mon = Integer.parseInt(month);
		
		//Year is null then we need data for specific month and year combination other wise send data for current finnancial year
		int yearData = Util.getFinnancialYearForMonth(mon);
		if(year != null){
			yearData = Integer.parseInt(year);
		}
		if(isFullUser){
			//Sql date formater need date i nYYYY-mm-dd hence forming date
			String dateFormatted = Util.getFinnancialYearForMonth(mon)+"-"+month+"-01";
			activityForMonthQueryFullUser = activityForMonthQueryFullUser.replace("(?)", companyId);
			HashMap namedMap = new HashMap();
			namedMap.put("userId", userId);
			namedMap.put("dueMonth", month);
			namedMap.put("dueYear", yearData);
			namedMap.put("dueDate", dateFormatted);
			namedMap.put("locationId", location);
			namedMap.put("activityStatusJoin", activityStatus.size()==0?"all":"na");
			if (activityStatus.size()>0){
				namedMap.put("activityStatus", activityStatus);
			}else{
				namedMap.put("activityStatus", new ArrayList<String>(){{add("na");}});
			}

			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityForMonthQueryFullUser,namedMap);
			return activities;
		}
		return null;
	}


	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(
			String companyId, String yearStr, boolean isFullUser,String quarter,String location,String userId,List<String> activityStatus) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		int year = Calendar.getInstance().get(Calendar.YEAR);
		//Indian quarter start at April so adding 1
		int quarterInt = Integer.parseInt(quarter);
		year = Util.getFYForQuarter(quarterInt, yearStr);
		String lastdayOfQuarter = Util.getLastDateOfQuarter(quarterInt, yearStr);
		//UI quarter starts with 0 so adding 1 in quarter
		quarterInt++;
		if(quarterInt == 4){
			quarterInt = 1;
		}else{
			quarterInt ++;
		}
		quarter = quarterInt+"";
		if(isFullUser){
			Map namedMap = new HashMap();
			String locationH = location!=null?location:"all";
			namedMap.put("locationId", locationH);
			namedMap.put("userId", userId);
			namedMap.put("dueQuarter", quarter);
			namedMap.put("dueYear", year);
			namedMap.put("quarterLastDay", lastdayOfQuarter);
			namedMap.put("activityStatusJoin", activityStatus.size()==0?"all":"na");
			if (activityStatus.size()>0){
				namedMap.put("activityStatus", activityStatus);
			}else{
				namedMap.put("activityStatus", new ArrayList<String>(){{add("na");}});
			}
			activityForQuarterQueryFullUser = activityForQuarterQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityForQuarterQueryFullUser,namedMap);
			return activities;
		}
		return null;
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByYearWithRejected(
			String companyId, String year, boolean isFullUser,String location,String userId,List<String> activityStatus) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		String fromYear = year+"-04-01";
		String toYear = (Integer.parseInt(year)+1)+"-04-01";
		if(isFullUser){
			Map namedQuery = new HashMap();
			namedQuery.put("userId", userId);
			namedQuery.put("locationId", location);
			namedQuery.put("fromYear", fromYear);
			namedQuery.put("toYear", toYear);
			namedQuery.put("activityStatusJoin", activityStatus.size()==0?"all":"na");
			if (activityStatus.size()>0){
				namedQuery.put("activityStatus", activityStatus);
			}else{
				namedQuery.put("activityStatus", new ArrayList<String>(){{add("na");}});
			}
			this.activityForYearQueryFullUserWithLocation = this.activityForYearQueryFullUserWithLocation.replace("and activity.isComplianceRejected=false", "");
			this.activityForYearQueryFullUserWithLocation = this.activityForYearQueryFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(this.activityForYearQueryFullUserWithLocation,namedQuery);
			return activities;
		}
		return null;
	}

}
