package com.compli.db.dao;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

public class DashBoardDao {
	private JdbcTemplate jdbcTemplate;
	
///////////////////////////////////////// Activity query //////////////////////////////////////////////////////////////////////////////////////
	private String activityQuery =  
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc;";
	
	private String activityQueryFullUser = 
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and periodicitydatemaster.duedate<NOW()) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc;";
	
	private String activityQueryWithLocation = 
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId  and activityassociation.locationId=?) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and periodicitydatemaster.duedate<NOW()) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc; ";
	
	private String activityQueryFullUserWithLocation = 
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId and activityassociation.locationId=?) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and periodicitydatemaster.duedate<NOW()) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc;";
	
	///////////////////////////////////////////////// Thiese are only open activities //////////////////////////////////////////////////////////////////////////////
	private String activityQueryForRisk = 
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId and companyWithLaw.riskId like ?) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and periodicitydatemaster.duedate<NOW()) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false and isComplied=false order by periodicityDateId desc";
	
	private String activityQueryForRiskFullUser = 
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId and companyWithLaw.riskId like ?) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and periodicitydatemaster.duedate<NOW()) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false and  isComplied=false order by periodicityDateId desc;";
	
	/**************************** Activity for last 3 month********************************************/
	private String activityLast3MonthQuery = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 MONTH),'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') <= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 0 MONTH),'%Y%m')) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false";
	
	private String activityLast3MonthQueryFullUser = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 MONTH),'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') <= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 0 MONTH),'%Y%m')) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false";
	
	private String activityLast3MonthQueryWithLocation = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId  and activityassociation.locationId=?) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 MONTH),'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') <= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 0 MONTH),'%Y%m')) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false";
	
	private String activityLast3MonthQueryFullUserWithLocation = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId  and activityassociation.locationId=?) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 MONTH),'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') <= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 0 MONTH),'%Y%m')) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false";
	
	/************************************************************************/
	private String activityForMonthQuery = 
			"select * from "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
		"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		 "on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable where ((month(duedate)=? and year(duedate)=?) and isComplied=true) or (DATE_FORMAT(duedate,'%Y%m') <= DATE_FORMAT(now(),'%Y%m') and isComplied=false);";
	
	private String activityForMonthQueryFullUser =
			"select * from "+
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
 "on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable where ((month(duedate)=? and year(duedate)=?) and isComplied=true and(isComplianceApproved=true || isComplainceDelayed=true)) or (month(duedate)<=? and year(duedate)<=? and isComplied=false);";
	
	/************************************************************************/
	private String activityForMonthIncludingRejectedQuery = 
			"select * from "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
		"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		 "on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId order by periodicityDateId desc) newtable where month(duedate)=? and year(duedate)=?;";
	
	private String activityForMonthIncludingRejectedQueryFullUser =
			"select * from "+
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
 "on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId  order by periodicityDateId desc) newtable where month(duedate)=? and year(duedate)=?;";
	
	
	/************************************************************************/
	private String activityForMonthQueryWithLocation = 
			"select * from "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId and activityassociation.locationId=?) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
		"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		 "on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable where ((month(duedate)=? and year(duedate)=?) and isComplied=true) or (DATE_FORMAT(duedate,'%Y%m') <= DATE_FORMAT(now(),'%Y%m') and isComplied=false);";
	
	private String activityForMonthQueryFullUserWithLocation =
			"select * from "+
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId  and activityassociation.locationId=?) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
 "on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable where ((month(duedate)=? and year(duedate)=?) and isComplied=true) or (DATE_FORMAT(duedate,'%Y%m') <= DATE_FORMAT(now(),'%Y%m') and isComplied=false);";

	/************************************************************************/
	private String activityForMonthQueryIncludingRejectedWithLocation = 
			"select * from "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId and activityassociation.locationId=?) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
		"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId  order by periodicityDateId desc) newtable where month(duedate)=? and year(duedate)=?;";
	
	private String activityForMonthQueryIncludingRejectFullUserWithLocation =
			"select * from "+
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId  and activityassociation.locationId=?) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
	"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId  order by periodicityDateId desc) newtable where month(duedate)=? and year(duedate)=?;";

	/******************************* Activity for year *****************************************/
	//Need to modify
	private String activityForYearQuery = 
			"select * from "+
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId  WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(?,'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') < DATE_FORMAT(?,'%Y%m')) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc;";
	
	private String activityForYearQueryFullUser = 
			"select * from "+
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable  WHERE ((DATE_FORMAT(duedate,'%Y%m') >= DATE_FORMAT(?,'%Y%m') and DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(?,'%Y%m')) and isComplied=true) or (DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(?,'%Y%m') and isComplied=false)";

	//Need to modify
		private String activityForYearQueryWithLocation = 
				"select * from "+
				"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
							"on companyWithLocationName.locationId = activityassociation.locationId and activityassociation.locationId=?) companyWithActivityId "+
						"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
					"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
				"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
			"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
		"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId  WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(?,'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') < DATE_FORMAT(?,'%Y%m')) companyWithPerDate  "+
			"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc;";
		
		private String activityForYearQueryFullUserWithLocation = 
				"select * from "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
							"on companyWithLocationName.locationId = activityassociation.locationId and activityassociation.locationId=?) companyWithActivityId "+
						"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
					"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
				"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
			"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
		"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
			"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable  WHERE ((DATE_FORMAT(duedate,'%Y%m') >= DATE_FORMAT(?,'%Y%m') and DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(?,'%Y%m')) and isComplied=true) or (DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(?,'%Y%m') and isComplied=false)";


	/******************************* Activity for year *****************************************/
	//Need to modify 
	private String activityForQuarterQuery = 
			"select * from "+
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId  WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(?,'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') < DATE_FORMAT(?,'%Y%m')) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc;";
	
	private String activityForQuarterQueryFullUser = 
			"select * from "+
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable  WHERE (quarter(duedate)=? and YEAR(duedate)=? and isComplied=true) or (quarter(duedate)<=? and YEAR(duedate)=? and isComplied=false)";

	//Need to modify 
		private String activityForQuarterQueryWithLocation = 
				"select * from "+
				"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
							"on companyWithLocationName.locationId = activityassociation.locationId and activityassociation.locationId=?) companyWithActivityId "+
						"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
					"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
				"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
			"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
		"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId  WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(?,'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') < DATE_FORMAT(?,'%Y%m')) companyWithPerDate  "+
			"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc;";
		
		private String activityForQuarterQueryFullUserWithLocation = 
				"select * from "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
							"on companyWithLocationName.locationId = activityassociation.locationId and activityassociation.locationId=?) companyWithActivityId "+
						"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
					"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
				"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
			"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
		"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
			"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable  WHERE (quarter(duedate)=? and YEAR(duedate)=? and isComplied=true) or (quarter(duedate)<=? and YEAR(duedate)=? and isComplied=false)";

	
	String changeActivityStatusQuery = "INSERT INTO activity (companyId,activityId, isComplied,isComplianceApproved,remark) VALUES(?, ?, ?, ?,?) ON DUPLICATE KEY UPDATE companyId =?,activityId=?, isComplied=?,isComplianceApproved=?,remark=?";
	String changeActivityStatusQueryArtec = "INSERT INTO activity (companyId,activityId, isComplied,isComplianceApproved,isProofRequired,isComplainceDelayed) VALUES(?, ?, ?, ?,?,?) ON DUPLICATE KEY UPDATE companyId =?,activityId=?, isComplied=?,isComplianceApproved=?,isProofRequired=?,isComplainceDelayed=?";
	
	/********************************* Dashboard pending activity Count for next10days. *****************************************************/

	private String rCountPending10Days = "select count(*) as count,pp.riskId from periodicitydatemaster right join (select periodicityDateId,riskId from activity right join activitymaster on activity.activityId = activitymaster.activityId where isComplianceApproved =false and isComplianceRejected=false and isComplied=false and isComplainceDelayed=false  and activity.companyId in (?)) pp "+
				"on periodicitydatemaster.periodicityDateId=pp.periodicityDateId where periodicitydatemaster.duedate<NOW() group by pp.riskId;";
		
	
	private String rCountPending10DaysFullUser = "select count(*) as count,pp.riskId from periodicitydatemaster inner join (select periodicityDateId,riskId from activity inner join activitymaster on activity.activityId = activitymaster.activityId where isComplianceApproved =false and isComplianceRejected=false and isComplied=false and isComplainceDelayed=false  and activity.companyId in (?)) pp "+
			"on periodicitydatemaster.periodicityDateId=pp.periodicityDateId where periodicitydatemaster.duedate<NOW() group by pp.riskId;";
	
	private String rCountPending10DaysWithLocation = "select count(*) as count,pp.riskId from periodicitydatemaster right join (select periodicityDateId,riskId from activity right join activitymaster on activity.activityId = activitymaster.activityId where isComplianceApproved =false and isComplianceRejected=false and isComplied=false and isComplainceDelayed=false  and activity.companyId in (?) and"+
			" activitymaster.activityId in (select activityId from activityassociation where locationId=?)) pp "+
			"on periodicitydatemaster.periodicityDateId=pp.periodicityDateId where periodicitydatemaster.duedate<NOW() group by pp.riskId;";
	

	private String rCountPending10DaysFullUserWithLocation = "select count(*) as count,pp.riskId from periodicitydatemaster inner join (select periodicityDateId,riskId from activity inner join activitymaster on activity.activityId = activitymaster.activityId where isComplianceApproved =false and isComplianceRejected=false and isComplied=false and isComplainceDelayed=false  and activity.companyId in (?) and" + 
			" activitymaster.activityId in (select activityId from activityassociation where locationId=?)) pp "+
			"on periodicitydatemaster.periodicityDateId=pp.periodicityDateId where periodicitydatemaster.duedate<NOW() group by pp.riskId;";
	/**************************************************************************************/
	private String complainceOverviewFullUser = "select "+ 
			"sum(case when (isComplied=true and isComplianceApproved=true and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as intimeCompliance, "+
			"sum(case when (isComplied=true and isComplianceApproved=false and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as pendingReview, "+
			"sum(case when (isComplied=false and isComplianceApproved=false and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as pendingCompliance, "+
			"sum(case when (isComplied=true and isComplianceApproved=true and isComplianceRejected=false and isComplainceDelayed=true) then 1 ELSE 0 END)as delayedCompliance "+
			"from activity where companyId in (?) and activityId in(select activityId from activitymaster where activitymaster.periodicityDateId<NOW());";
	
	private String complainceOverviewFullUserWithLocation = "select "+ 
			"sum(case when (isComplied=true and isComplianceApproved=true and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as intimeCompliance, "+
			"sum(case when (isComplied=true and isComplianceApproved=false and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as pendingReview, "+
			"sum(case when (isComplied=false and isComplianceApproved=false and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as pendingCompliance, "+
			"sum(case when (isComplied=true and isComplianceApproved=true and isComplianceRejected=false and isComplainceDelayed=true) then 1 ELSE 0 END)as delayedCompliance "+
			"from activity where companyId in (?)	 and activityId in (select activityId from activityassociation where locationId=?) and activityId in(select activityId from activitymaster where activitymaster.periodicityDateId<NOW());";

///////////////////////////////////////// Activity query by lawarea and status//////////////////////////////////////////////////////////////////////////////////////
private String activityQueryByLawAndStatus = 
"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity right join "+
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
"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
"on companyWithActivity.lawId = lawmaster.lawId  and companyWithActivity.lawId like ?) companyWithLaw	"+	
"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and periodicitydatemaster.duedate<NOW()) companyWithPerDate  "+
"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc;";

private String activityQueryByLawAndStatusFullUser = 
"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId and lawmaster.lawName = ?) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and periodicitydatemaster.duedate<NOW()) companyWithPerDate  "+
"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false and isComplied=? and isComplianceApproved=? and isComplainceDelayed=? and isComplianceRejected=? order by periodicityDateId desc;";

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Map<String, Object>> getAllActivitiesForCompany(String companyId){
		companyId = "('"+companyId.replace(",", "','")+"')";
		List<Map<String, Object>> activities = this.jdbcTemplate.queryForList("select * from activitymaster inner join (select activityId from activityassociation where locationId in (select locationId from companylocation where companyId in "+companyId+")) activityLocation on activitymaster.activityId = activityLocation.activityId;");
		return activities;
	}
	
	public List<Map<String, Object>> getAllOpenActivitiesWithDescriptionForCompany(String companyId,boolean isFullUser){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityQueryFullUser = activityQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQueryFullUser);
			return activities;
		}else{
			activityQuery = activityQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQuery);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId,boolean isFullUser){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityQueryFullUser = activityQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQueryFullUser);
			return activities;
		}else{
			activityQuery = activityQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQuery);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId,boolean isFullUser,String locationId){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityQueryFullUserWithLocation = activityQueryFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQueryFullUserWithLocation,locationId);
			return activities;
		}else{
			activityQueryWithLocation = activityQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQueryWithLocation,locationId);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithRisk(String companyId,String riskId,boolean isFullUser){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityQueryForRiskFullUser = activityQueryForRiskFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQueryForRiskFullUser,riskId);
			return activities;
		}else{
			activityQueryForRisk = activityQueryForRisk.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQueryForRisk,riskId);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId, String month,boolean isFullUser) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityForMonthQueryFullUser = activityForMonthQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQueryFullUser,month,Calendar.getInstance().get(Calendar.YEAR),month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}else{
			activityForMonthQuery = activityForMonthQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQuery,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(String companyId, String month,boolean isFullUser) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityForMonthIncludingRejectedQueryFullUser = activityForMonthIncludingRejectedQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthIncludingRejectedQueryFullUser,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}else{
			activityForMonthQuery = activityForMonthQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthIncludingRejectedQuery,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId, String month,boolean isFullUser,String location) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityForMonthQueryFullUserWithLocation = activityForMonthQueryFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQueryFullUserWithLocation,location,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}else{
			activityForMonthQueryWithLocation = activityForMonthQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQueryWithLocation,location,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(String companyId, String month,boolean isFullUser,String location) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityForMonthQueryIncludingRejectFullUserWithLocation = activityForMonthQueryIncludingRejectFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQueryIncludingRejectFullUserWithLocation,location,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}else{
			activityForMonthQueryIncludingRejectedWithLocation = activityForMonthQueryIncludingRejectedWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQueryIncludingRejectedWithLocation,location,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}
	}


	public boolean changeActivityStatus(String companyId, String activityId,boolean status,String remark) {
		boolean isDemoUser = false;
		this.jdbcTemplate.update(changeActivityStatusQuery, companyId,activityId,status,isDemoUser, remark,companyId,activityId,status,isDemoUser,remark);
		return true;
	}
	
	public boolean changeActivityStatusApproved(String companyId, String activityId) {
		this.jdbcTemplate.update(changeActivityStatusQueryArtec, companyId,activityId,true,true,false,false,companyId,activityId,true,true,false,false);
		return true;
	}
	
	public boolean changeActivityStatusPendingComplied(String companyId, String activityId) {
		this.jdbcTemplate.update(changeActivityStatusQueryArtec, companyId,activityId,false,false,false,false,companyId,activityId,false,false,false,false);
		return true;
	}
	
	public boolean changeActivityStatusComplainceDelayed(String companyId, String activityId) {
		this.jdbcTemplate.update(changeActivityStatusQueryArtec, companyId,activityId,true,false,false,true,companyId,activityId,true,false,false,true);
		return true;
	}
	
	public boolean changeActivityStatusPendingDecrepancy(String companyId, String activityId) {
		this.jdbcTemplate.update(changeActivityStatusQueryArtec, companyId,activityId,false,false,true,false,companyId,activityId,false,false,true,false);
		return true;
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,
			int month,boolean isFullUser) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityLast3MonthQueryFullUser = activityLast3MonthQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityLast3MonthQueryFullUser);
			return activities;
		}else{
			activityLast3MonthQuery = activityLast3MonthQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityLast3MonthQuery);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,
			int month,boolean isFullUser,String locationId) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityLast3MonthQueryFullUserWithLocation = activityLast3MonthQueryFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityLast3MonthQueryFullUserWithLocation,locationId);
			return activities;
		}else{
			activityLast3MonthQueryWithLocation = activityLast3MonthQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityLast3MonthQueryWithLocation,locationId);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getPendingActivityCoutForNext10Days(String companyId,boolean isFullUser){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			rCountPending10DaysFullUser = rCountPending10DaysFullUser.replace("(?)", companyId);
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(rCountPending10DaysFullUser);
			return activitiesCount;
		}else{
			rCountPending10Days = rCountPending10Days.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(rCountPending10Days);
			return activitiesCount;
		}
	}
	
	public List<Map<String, Object>> getPendingActivityCoutForNext10Days(String companyId,boolean isFullUser,String locationId){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			rCountPending10DaysFullUserWithLocation = rCountPending10DaysFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(rCountPending10DaysFullUserWithLocation,locationId);
			return activitiesCount;
		}else{
			rCountPending10DaysWithLocation = rCountPending10DaysWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(rCountPending10DaysWithLocation,locationId);
			return activitiesCount;
		}
	}
	
	//THis function is always for full user
	public Map<String, Object> getComplianceOverview(String companyId,String locationId){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(null==locationId){
			complainceOverviewFullUser = complainceOverviewFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(complainceOverviewFullUser);
			return activitiesCount.get(0);
		}else{
			complainceOverviewFullUserWithLocation = complainceOverviewFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(complainceOverviewFullUserWithLocation,locationId);
			return activitiesCount.get(0);
		}
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByLawAndStatus(
			String companyId,String lawId,String status, boolean isFullUser) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		boolean isComplied = false,isComplianceApproved = false,isComplainceDelayed = false,isComplianceRejected = false;
		if("Pending compliance".equalsIgnoreCase(status)){
			isComplied = false;isComplianceApproved = false;isComplainceDelayed = false;isComplianceRejected = false;
		}else if("Complied-Delayed".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = true;isComplainceDelayed = true;isComplianceRejected = false;
		}else if("Pending for review".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = false;isComplainceDelayed = false;isComplianceRejected = false;
		}else if("Complied- In time".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = true;isComplainceDelayed = false;isComplianceRejected = false;
		}
		
		if(isFullUser){
			activityQueryByLawAndStatusFullUser = activityQueryByLawAndStatusFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(activityQueryByLawAndStatusFullUser,lawId,isComplied,isComplianceApproved,isComplainceDelayed,isComplianceRejected);
			return activitiesCount;
		}else{
			activityQueryByLawAndStatus = activityQueryByLawAndStatus.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(activityQueryByLawAndStatus,lawId,isComplied,isComplianceApproved,isComplainceDelayed,isComplianceRejected);
			return activitiesCount;
		}
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByYear(
			String companyId, String year, boolean isFullUser) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		String fromYear = year+"-04-01";
		String toYear = (Integer.parseInt(year)+1)+"-04-01";
		if(isFullUser){
			activityForYearQueryFullUser = activityForYearQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForYearQueryFullUser,fromYear,toYear,toYear);
			return activities;
		}else{
			activityForYearQuery = activityForYearQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForYearQuery,fromYear,toYear);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByYear(
			String companyId, String year, boolean isFullUser,String location) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		String fromYear = year+"-04-01";
		String toYear = (Integer.parseInt(year)+1)+"-04-01";
		if(isFullUser){
			activityForYearQueryFullUserWithLocation = activityForYearQueryFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForYearQueryFullUserWithLocation,location,fromYear,toYear,toYear);
			return activities;
		}else{
			activityForYearQueryWithLocation = activityForYearQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForYearQueryWithLocation,location,fromYear,toYear);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(
			String companyId, boolean isFullUser,String quarter) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		int year = Calendar.getInstance().get(Calendar.YEAR);

		//Indian quarter start at April so adding 1 
		int quarterInt = Integer.parseInt(quarter);
		//UI quarter starts with 0 so adding 1 in quarter
		quarterInt++;
		if(quarterInt == 4){
			quarterInt = 1;
		}else{
			quarterInt ++;
		}
		quarter = quarterInt+"";
		if(isFullUser){
			activityForQuarterQueryFullUser = activityForQuarterQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForQuarterQueryFullUser,quarter,year,quarter,year);
			return activities;
		}else{
			activityForQuarterQuery = activityForQuarterQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForQuarterQuery,quarter,year);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(
			String companyId, boolean isFullUser,String quarter,String location) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		int year = Calendar.getInstance().get(Calendar.YEAR);

		//Indian quarter start at April so adding 1 
		int quarterInt = Integer.parseInt(quarter);
		//UI quarter starts with 0 so adding 1 in quarter
		quarterInt++;
		if(quarterInt == 4){
			quarterInt = 1;
		}else{
			quarterInt ++;
		}
		quarter = quarterInt+"";
		if(isFullUser){
			activityForQuarterQueryFullUserWithLocation = activityForQuarterQueryFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForQuarterQueryFullUserWithLocation,location,quarter,year,quarter,year);
			return activities;
		}else{
			activityForQuarterQueryWithLocation = activityForQuarterQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForQuarterQueryWithLocation,location,quarter,year);
			return activities;
		}
	}
	
}
