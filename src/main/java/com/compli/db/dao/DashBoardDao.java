package com.compli.db.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compli.bean.dashboard.DashboardparamsBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.compli.util.Constants;
import com.compli.util.Util;

public class DashBoardDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
///////////////////////////////////////// Activity query //////////////////////////////////////////////////////////////////////////////////////
	private String activityQuery =  
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
			"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activity_assignment.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity_assignment inner join "+
					"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
					"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and ((periodicitydatemaster.duedate<NOW() and true=:tillDate) or ((periodicitydatemaster.duedate<=LAST_DAY(NOW()) and true=:tillMonthEnd)))) companyWithPerDate  "+
			"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc) allActivities "+
		"on allActivities.activityId=activity_assignment.activityId where userId=:userId";
	private String activityQueryWithLocation = 
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and ((periodicitydatemaster.duedate<NOW() and true=?) or ((periodicitydatemaster.duedate<NOW() and true=?)))) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc; ";
	
	private String activityQueryFullUserWithLocation = 
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and ((periodicitydatemaster.duedate<NOW() and true=?) or ((periodicitydatemaster.duedate<NOW() and true=?)))) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc;";
	
	///////////////////////////////////////////////// Thiese are only open activities //////////////////////////////////////////////////////////////////////////////
	private String activityQueryForRisk = 
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
			"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,allActivities.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,isComplied ,isComplianceApproved,isComplianceRejected,isComplainceDelayed, isProofRequired,reOpen,arTechRemark,assignedUser,remark,completionDate from activity_assignment inner join "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false and  isComplied=false order by periodicityDateId desc)allActivities "+
		"on allActivities.activityId=activity_assignment.activityId where userId=?";
	
	/**************************** Activity for last 3 month********************************************/
	private String activityLast3MonthQuery = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
	
	private String activityLast3MonthQueryFullUser =
			"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activity_assignment.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity_assignment inner join "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId WHERE DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') >= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -2 MONTH),'%Y%m') and DATE_FORMAT(periodicitydatemaster.duedate,'%Y%m') <= DATE_FORMAT(DATE_ADD(NOW(), INTERVAL 0 MONTH),'%Y%m')) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false)allActivities " +
	"on allActivities.activityId=activity_assignment.activityId where userId=:userId";
	
	private String activityLast3MonthQueryWithLocation = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
	
	private String activityLast3MonthQueryFullUserWithLocation = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
			"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,allActivities.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate  from activity_assignment inner join "+
			"(select * from "+		
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable where ((month(duedate)=:dueMonth and year(duedate)=:dueYear) and isComplied=true and(isComplianceApproved=true || isComplainceDelayed=true)) or (DATE_FORMAT(duedate,'%Y%m')<=DATE_FORMAT(:dueDate,'%Y%m') and isComplied=false))allActivities "
		+ "on allActivities.activityId=activity_assignment.activityId where userId=:userId";
	
	//THis will be used by only master user
	private String activityForMonthQueryMasterUser =
			"select * from "+		
		"(select companyWithPerDate.companyId,abbriviation,name,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
			"(select companyId,abbriviation,name,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicitydatemaster.periodicityDateId,consequence,periodicitydatemaster.duedate from periodicitydatemaster inner join "+
				"(select companyId,abbriviation,name,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicitymaster.periodicityId,periodicitymaster.description as periodicityDesc,periodicityDateId,consequence from periodicitymaster inner join "+
					"(select companyId,abbriviation,name,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskmaster.riskId,riskmaster.description riskDes,periodicityId,periodicityDateId,consequence from riskmaster inner join "+
						"(select companyId,abbriviation,name,locationId,locationName,activityId,activityName,riskId,lawmaster.lawName,lawmaster.lawId,lawmaster.lawDesc,periodicityId,periodicityDateId,consequence from lawmaster inner join "+
							"(select companyId,abbriviation,name,lawId,locationId,locationName,activitymaster.activityId,activitymaster.activityName,riskId,periodicityId,periodicityDateId,consequence from activitymaster inner join "+
								"(select companyId,abbriviation,name,activityassociation.locationId,locationName,activityId from activityassociation inner join "+ 
									"(select companyId,abbriviation,name,location.locationId,locationName from location inner join	"+
										"(select cc.companyId,abbriviation,name,locationId from company cc inner join companylocation "+
										"on cc.companyId = companylocation.companyId where cc.companyId in (?)  or 'all'=:companyId) companyWithLocation "+
									"on location.locationId = companyWithLocation.locationId) companyWithLocationName "+
								"on companyWithLocationName.locationId = activityassociation.locationId   and (activityassociation.locationId=:locationId or 'all'=:locationId)) companyWithActivityId "+
							"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
						"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
					"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
				"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
			"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
			"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable  WHERE ((DATE_FORMAT(duedate,'%Y%m') >= DATE_FORMAT(:fromYear,'%Y%m') and DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(:toYear,'%Y%m')))";
	
	/************************************************************************/
	private String activityForMonthIncludingRejectedQuery = 
			"select * from "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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

    /******************************* Activity for year *****************************************/
	//Need to modify
	private String activityForYearQuery = 
			"select * from "+
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
				"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
				"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,allActivities.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,dueMonth,isComplied ,isComplianceApproved,isComplianceRejected,isComplainceDelayed,isProofRequired,reOpen,arTechRemark,assignedUser,remark,completionDate  from activity_assignment inner join "+
				"(select * from "+
			"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
			"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable  WHERE ((DATE_FORMAT(duedate,'%Y%m') >= DATE_FORMAT(:fromYear,'%Y%m') and DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(:toYear,'%Y%m')) and isComplied=true) or (DATE_FORMAT(duedate,'%Y%m') < DATE_FORMAT(:toYear,'%Y%m') and isComplied=false))allActivities "
		+ "on allActivities.activityId=activity_assignment.activityId where userId=:userId";


	/******************************* Activity for year *****************************************/
	//Need to modify 
	private String activityForQuarterQuery = 
			"select * from "+
			"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
			"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,allActivities.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,dueMonth,isComplied ,isComplianceApproved,isComplianceRejected,isComplainceDelayed,isProofRequired,reOpen,arTechRemark,assignedUser,remark,completionDate  from activity_assignment inner join "+
			"(select * from "+
		"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false  order by periodicityDateId desc) newtable  WHERE (quarter(duedate)=:dueQuarter and YEAR(duedate)=:dueYear and isComplied=true) or (DATE_FORMAT(duedate,'%Y%m')<=DATE_FORMAT(:quarterLastDay,'%Y%m') and isComplied=false))allActivities "+
		 "on allActivities.activityId=activity_assignment.activityId where userId=:userId";

    String changeActivityStatusQuery = "INSERT INTO activity (companyId,activityId, isComplied,isComplianceApproved,remark,isProofRequired,reOpen,activityStatus) VALUES(?, ?, ?, ?,?,false,false,?) ON DUPLICATE KEY UPDATE companyId =?,activityId=?, isComplied=?,isComplianceApproved=?,remark=?,isProofRequired=false,reOpen=false,activityStatus=?";
	String changeActivityStatusQueryArtec = "INSERT INTO activity (companyId,activityId, isComplied,isComplianceApproved,isProofRequired,isComplainceDelayed,completionDate,reOpen,activityStatus) VALUES(?, ?, ?, ?,?,?,?,false,?) ON DUPLICATE KEY UPDATE companyId =?,activityId=?, isComplied=?,isComplianceApproved=?,isProofRequired=?,isComplainceDelayed=?,completionDate=?,reOpen=false,activityStatus=?";
	String changeActivityPendingForDescripancyStatusQueryArtec = "INSERT INTO activity (companyId,activityId, isComplied,isComplianceApproved,isProofRequired,isComplainceDelayed,arTechRemark,reOpen,activityStatus) VALUES(?, ?, ?, ?,?,?,?,false,?) ON DUPLICATE KEY UPDATE companyId =?,activityId=?, isComplied=?,isComplianceApproved=?,isProofRequired=?,isComplainceDelayed=?,arTechRemark=?,reOpen=false,activityStatus=?";
	String changeActivityRejectedForDescripancyStatusQueryArtec = "UPDATE activity set isComplied=true,isComplianceApproved=false,isProofRequired=false,isComplainceDelayed=false,reOpen=false,isComplianceRejected=true,activityStatus=? where companyId =? and activityId=?";
	
	/********************************* Dashboard pending activity Count for next10days. *****************************************************/

	private String rCountPending10Days = "select count(*) as count,pp.riskId from periodicitydatemaster right join (select periodicityDateId,riskId from activity right join activitymaster on activity.activityId = activitymaster.activityId where isComplianceApproved =false and isComplianceRejected=false and isComplied=false and isComplainceDelayed=false  and activity.companyId in (?)) pp "+
				"on periodicitydatemaster.periodicityDateId=pp.periodicityDateId where periodicitydatemaster.duedate<NOW() group by pp.riskId;";
		
	
	private String rCountPending10DaysFullUser = 
			"select count(*) as count,p1.riskId from periodicitydatemaster inner join ("
				+ "select periodicityDateId,riskId,activity_assignment.activityId from activity_assignment join ("
						+ "select periodicityDateId,riskId,activity.activityId from activity inner join activitymaster on activity.activityId = activitymaster.activityId where isComplianceApproved =false and isComplianceRejected=false and isComplied=false and isComplainceDelayed=false  and activity.companyId in (?)) pp  "
					+ "on activity_assignment.activityId=pp.activityId where activity_assignment.userId=?)p1 "+
			"on periodicitydatemaster.periodicityDateId=p1.periodicityDateId where periodicitydatemaster.duedate<=? and periodicitydatemaster.duedate>=? group by p1.riskId;";
	
	private String rCountPending10DaysWithLocation = "select count(*) as count,pp.riskId from periodicitydatemaster right join (select periodicityDateId,riskId from activity right join activitymaster on activity.activityId = activitymaster.activityId where isComplianceApproved =false and isComplianceRejected=false and isComplied=false and isComplainceDelayed=false  and activity.companyId in (?) and"+
			" activitymaster.activityId in (select activityId from activityassociation where locationId=?)) pp "+
			"on periodicitydatemaster.periodicityDateId=pp.periodicityDateId where periodicitydatemaster.duedate<NOW() group by pp.riskId;";
	

	private String rCountPending10DaysFullUserWithLocation = "select count(*) as count,pp.riskId from periodicitydatemaster inner join (select periodicityDateId,riskId from activity inner join activitymaster on activity.activityId = activitymaster.activityId where isComplianceApproved =false and isComplianceRejected=false and isComplied=false and isComplainceDelayed=false  and activity.companyId in (?) and" + 
			" activitymaster.activityId in (select activityId from activityassociation where locationId=?)) pp "+
			"on periodicitydatemaster.periodicityDateId=pp.periodicityDateId where periodicitydatemaster.duedate<NOW() group by pp.riskId;";
	/**************************************************************************************/
	private String complianceOverviewFullUser = "select "+
			"sum(case when (isComplied=true and isComplianceApproved=true and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as intimeCompliance, "+
			"sum(case when (isComplied=true and isComplianceApproved=false and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as pendingReview, "+
			"sum(case when (isComplied=false and isComplianceApproved=false and isComplianceRejected=false and isComplainceDelayed=false) then 1 ELSE 0 END)as pendingCompliance, "+
			"sum(case when (isComplied=true and isComplianceApproved=true and isComplianceRejected=false and isComplainceDelayed=true) then 1 ELSE 0 END)as delayedCompliance "+
			"from activity where companyId in (?) and (activityId in (select activityId from activityassociation where locationId=:locationId) or 'all'=:locationId) and "
			+ "activityId in(select activitymaster.activityId from activitymaster right join activity_assignment on activitymaster.activityId=activity_assignment.activityId "+
            "where userId=:userId and CAST(activitymaster.periodicityDateId AS datetime)<=CAST(:endDate AS datetime) and CAST(activitymaster.periodicityDateId AS datetime)>=CAST(:startDate AS datetime));";

	private String activityCountByLaw = "select activityStatus,count(activityStatus) as count,lawmaster.lawId,lawmaster.lawname from lawmaster join (" +
			"select activityStatus,lawId,companyId from activity join (" +
				"select actAssociation.activityId,lawId from activitymaster join (" +
					"select activityassociation.activityId,activityassociation.locationId from activityassociation join activity_assignment on activityassociation.activityId = activity_assignment.activityId where activity_assignment.userId=:userId" +
				")actAssociation on actAssociation.activityId = activitymaster.activityId where (actAssociation.locationId=:locationId or 'all' = :locationId) and cast(periodicityDateId as date)<=:endDate and cast(periodicityDateId as date)>=:startDate" +
			")activityNLocation on activityNLocation.activityId=activity.activityId" +
			")activityall on activityall.lawId = lawmaster.lawId where (companyId = :companyId or 'all' = :companyId) group by lawName,activityStatus;";

///////////////////////////////////////// Activity query by lawarea and status//////////////////////////////////////////////////////////////////////////////////////
private String activityQueryByLawAndStatus = 
"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,allActivities.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,isComplied ,isComplianceApproved, isComplianceRejected, isComplainceDelayed,isProofRequired,reOpen,arTechRemark,assignedUser,remark,completionDate from activity_assignment inner join "+
"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId and (activityassociation.locationId=:locationId or 'all'=:locationId)) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId and lawmaster.lawName = :lawName) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and periodicitydatemaster.duedate<NOW()) companyWithPerDate  "+
"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false and isComplied=:isComplied and isComplianceApproved=:isComplianceApproved and isComplainceDelayed=:isComplainceDelayed and isComplianceRejected=:isComplianceRejected and isProofRequired=:isProofRequired order by periodicityDateId desc)allActivities "
+ "on allActivities.activityId=activity_assignment.activityId where userId=:userId";

///////////////////////////////////////// Activity query by lawarea and status//////////////////////////////////////////////////////////////////////////////////////
private String activityQueryByMonthAndStatus = 
"select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity right join "+
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
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and month(periodicitydatemaster.duedate)=? and year(periodicitydatemaster.duedate)=?)) companyWithPerDate  "+
"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false order by periodicityDateId desc;";

private String activityQueryByMonthAndStatusFullUser = 
"select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activity_assignment.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,isComplied ,isComplianceApproved, isComplianceRejected, isComplainceDelayed,isProofRequired,reOpen,arTechRemark,assignedUser,remark,completionDate from activity_assignment inner join "+
"(select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,ifnull(isComplied,false)as isComplied ,ifnull(isComplianceApproved,false) as isComplianceApproved,ifnull(isComplianceRejected,false) as isComplianceRejected,ifnull(isComplainceDelayed,false) as isComplainceDelayed,ifnull(isProofRequired,false) as isProofRequired,ifnull(reOpen,false) as reOpen,arTechRemark,assignedUser,remark,completionDate from activity inner join "+
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
						"on companyWithLocationName.locationId = activityassociation.locationId and (activityassociation.locationId=:locationId or 'all'=:locationId)) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId and month(periodicitydatemaster.duedate)=:dueMonth and year(periodicitydatemaster.duedate)=:dueYear) companyWithPerDate  "+
"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId and activity.isComplianceRejected=false and isComplied=:isComplied and isComplianceApproved=:isComplianceApproved and isComplainceDelayed=:isComplainceDelayed and isComplianceRejected=isComplianceRejected and isProofRequired=:isProofRequired order by periodicityDateId desc)allActivities "
+ "on allActivities.activityId=activity_assignment.activityId where userId=:userId";

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}



	public List<Map<String, Object>> getAllActivitiesForCompany(String companyId){
		companyId = "('"+companyId.replace(",", "','")+"')";
		List<Map<String, Object>> activities = this.jdbcTemplate.queryForList("select * from activitymaster inner join (select activityId from activityassociation where locationId in (select locationId from companylocation where companyId in "+companyId+")) activityLocation on activitymaster.activityId = activityLocation.activityId;");
		return activities;
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId,boolean isFullUser,boolean tillDate,boolean tillMonthEnd,String userId,String locationId){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			MapSqlParameterSource namedParameter = new MapSqlParameterSource();
			namedParameter.addValue("tillDate",tillDate );
			namedParameter.addValue("tillMonthEnd",tillMonthEnd );
			namedParameter.addValue("userId",userId );
			namedParameter.addValue("locationId",locationId );
			activityQueryFullUser = activityQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityQueryFullUser,namedParameter);
			return activities;
		}else{
			activityQuery = activityQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQuery);
			return activities;
		}
	}

	public List<Map<String, Object>> getComplianceOverviewByLaw(String userId,String locationId,String companyId,DashboardparamsBean dParam){
		String startDate = Util.getFyStartDateForForBean(dParam);
		String endDate = Util.getFyLastDateForForBean(dParam);
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("endDate",endDate );
		namedParameter.addValue("startDate",startDate );
		//If company id contains "," then need to send all company data sience we dont have dashboard for multiselect companies
		companyId = companyId.indexOf(",") >0 ? "all" : companyId;
		namedParameter.addValue("companyId",companyId);
		namedParameter.addValue("userId",userId );
		locationId = locationId == null ? "all":locationId;
		namedParameter.addValue("locationId",locationId );
		this.jdbcTemplate.execute("set session sql_mode='TRADITIONAL'");
		List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityCountByLaw,namedParameter);
		return activities;
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyWithRisk(String companyId,String riskId,String userId,boolean isFullUser){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			activityQueryForRiskFullUser = activityQueryForRiskFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQueryForRiskFullUser,riskId,userId);
			return activities;
		}else{
			activityQueryForRisk = activityQueryForRisk.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQueryForRisk,riskId);
			return activities;
		}
	}
	
	/*	If year is null then current finnancial year will be considered
	 * other wise for year and month data will be returned
	 */
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthAndYear(String companyId, String month,String year,String userId,boolean isFullUser,String location) {
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
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityForMonthQueryFullUser,namedMap);
			return activities;
		}else{
			activityForMonthQuery = activityForMonthQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQuery,month,Util.getFinnancialYearForMonth(mon));
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByYearWithRejected(
			String companyId, String year, boolean isFullUser,String location,String userId) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		String fromYear = year+"-04-01";
		String toYear = (Integer.parseInt(year)+1)+"-04-01";
		if(isFullUser){
			Map namedQuery = new HashMap();
			namedQuery.put("userId", userId);
			namedQuery.put("locationId", location);
			namedQuery.put("fromYear", fromYear);
			namedQuery.put("toYear", toYear);
			this.activityForYearQueryFullUserWithLocation = this.activityForYearQueryFullUserWithLocation.replace("and activity.isComplianceRejected=false", "");
			this.activityForYearQueryFullUserWithLocation = this.activityForYearQueryFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(this.activityForYearQueryFullUserWithLocation,namedQuery);
			return activities;
		}else{
			activityForYearQueryWithLocation = activityForYearQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForYearQueryWithLocation,location,fromYear,toYear);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(String companyId, String month,String year,boolean isFullUser,String location,String userId) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		//Year is null then we need data for specific month and year combination other wise send data for current finnancial year
		int mon = Integer.parseInt(month);
		int yearData = Util.getFinnancialYearForMonth(mon);
		if(year != null){
			yearData = Integer.parseInt(year);
		}
		if(isFullUser){
			String dateFormatted = Util.getFinnancialYearForMonth(mon)+"-"+month+"-01";
			Map namedMap = new HashMap();
			namedMap.put("userId", userId);
			namedMap.put("dueMonth", month);
			namedMap.put("dueYear", yearData);
			namedMap.put("dueDate", dateFormatted);
			namedMap.put("locationId", location);
			this.activityForMonthQueryFullUser = this.activityForMonthQueryFullUser.replace("and activity.isComplianceRejected=false", "");
			this.activityForMonthQueryFullUser = this.activityForMonthQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(this.activityForMonthQueryFullUser,namedMap);
			return activities;
		}else{
			activityForMonthQuery = activityForMonthQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthIncludingRejectedQuery,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}
	}
	
	//THis method is only used by master user 
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(String companyId, String month,String year) {
		String companyIdStr = companyId;
		companyId = "('"+companyId.replace(",", "','")+"')";
			int mon = 4;
			String fromYear = "2014-"+mon+"-01";
			String toYear = "2034-"+mon+"-01";
			
			if(!"all".equals(year)){
				fromYear = year+"-"+mon+"-01";
				toYear = (Integer.parseInt(year)+1)+"-"+mon+"-01";
			}
			Map namedMap = new HashMap();
			//namedMap.put("dueMonth", month);
			//namedMap.put("dueYear", Util.getFinnancialYearForMonth(mon));
			namedMap.put("fromYear", fromYear);
			namedMap.put("toYear", toYear);
			namedMap.put("companyId", companyIdStr);
			namedMap.put("locationId", "all");
			this.activityForMonthQueryMasterUser = this.activityForMonthQueryMasterUser.replace("and activity.isComplianceRejected=false", "");
			this.activityForMonthQueryMasterUser = this.activityForMonthQueryMasterUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(this.activityForMonthQueryMasterUser,namedMap);
			if(!"all".equals(month)){
				activities = filterData(activities, Integer.parseInt(month));
			}
			return activities;		
	}
	
	//Heremonth start with 1
	public List<Map<String, Object>> filterData(List<Map<String, Object>> activities,int month){
		List<Map<String, Object>> activitiesFilterd = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> activity:activities){
			String dateStr = activity.get("duedate").toString();
			try {
				Date date=new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				int m1 = localDate.getMonthValue();
				if(m1==month){
					activitiesFilterd.add(activity);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}  
			
		}
		return activitiesFilterd;
	}
	
	//This function is only for repository
/*	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarterWithRejected(String companyId, String quarter,boolean isFullUser) {
		//removing is rejected from query
		this.activityForQuarterQueryFullUser = this.activityForQuarterQueryFullUser.replace("and activity.isComplianceRejected=false", "");
		List<Map<String, Object>> activities = getAllActivitiesWithDescriptionForCompanyByQuarter(companyId, isFullUser, quarter);
		return activities;
	}
*/	
	//This function is only for repository
		public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarterWithRejected(String companyId, String quarter,boolean isFullUser,String location,String userId) {
			//companyId = "('"+companyId.replace(",", "','")+"')";
			int year = Calendar.getInstance().get(Calendar.YEAR);
			//Indian quarter start at April so adding 1 
			int quarterInt = Integer.parseInt(quarter);
			year = Util.getFYForQuarter(quarterInt);
			String lastdayOfQuarter = Util.getLastDateOfQuarter(quarterInt);
			//UI quarter starts with 0 so adding 1 in quarter
			quarterInt++;
			if(quarterInt == 4){
				quarterInt = 1;
			}else{
				quarterInt ++;
			}
			quarter = quarterInt+"";
			Map namedMap = new HashMap();
			//removing is rejected from query
			String locationH = location!=null?location:"all";
			namedMap.put("locationId", locationH);
			namedMap.put("userId", userId);
			namedMap.put("dueQuarter", quarter);
			namedMap.put("dueYear", year);
			namedMap.put("quarterLastDay", lastdayOfQuarter);
			this.activityForQuarterQueryFullUser = this.activityForQuarterQueryFullUser.replace("and activity.isComplianceRejected=false", "");
			List<Map<String, Object>> activities = getAllActivitiesWithDescriptionForCompanyByQuarter(companyId, isFullUser, quarter,location,userId);
			return activities;
		}
	
		//Need to fix
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId, String month,boolean isFullUser,String location,String userId) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		int mon = Integer.parseInt(month);
		if(isFullUser){
			String dateFormatted = Util.getFinnancialYearForMonth(mon)+"-"+month+"-01";
			Map namedMap = new HashMap();
			namedMap.put("userId", userId);
			namedMap.put("dueMonth", month);
			namedMap.put("dueYear", Util.getFinnancialYearForMonth(mon));
			namedMap.put("dueDate", dateFormatted);
			namedMap.put("locationId", location);
			
			activityForMonthQueryFullUser = activityForMonthQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityForMonthQueryFullUser,namedMap);
			return activities;
		}else{
			activityForMonthQueryWithLocation = activityForMonthQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQueryWithLocation,location,month,Calendar.getInstance().get(Calendar.YEAR));
			return activities;
		}
	}
	
	/*public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthWithRejected(String companyId, String month,boolean isFullUser,String location) {
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
	}*/


	public boolean changeActivityStatus(String companyId, String activityId,boolean status,String remark,String activityStatus) {
		boolean isDemoUser = false;
		this.jdbcTemplate.update(changeActivityStatusQuery, companyId,activityId,status,isDemoUser, remark,activityStatus,companyId,activityId,status,isDemoUser,remark,activityStatus);
		return true;
	}
	
	public boolean changeActivityStatusApproved(String companyId, String activityId,Date complainceDate) {
		this.jdbcTemplate.update(changeActivityStatusQueryArtec, companyId,activityId,true,true,false,false,complainceDate,Constants.COMP_INTIME,companyId,activityId,true,true,false,false,complainceDate,Constants.COMP_INTIME);
		return true; 
	}
	
	public boolean changeActivityStatusPendingComplied(String companyId, String activityId) {
		this.jdbcTemplate.update(changeActivityStatusQueryArtec, companyId,activityId,false,false,false,false,new Date(),Constants.PENDING_COMPLIE,companyId,activityId,false,false,false,false,new Date(),Constants.PENDING_COMPLIE);
		return true;
	}
	
	public boolean changeActivityStatusComplainceDelayed(String companyId, String activityId,Date complainceDate) {
		this.jdbcTemplate.update(changeActivityStatusQueryArtec, companyId,activityId,true,true,false,true,complainceDate,Constants.COM_DELAYED,companyId,activityId,true,true,false,true,complainceDate,Constants.COM_DELAYED);
		return true;
	}
	
	public boolean changeActivityStatusPendingDecrepancy(String companyId, String activityId,String arTechRemark) {
		this.jdbcTemplate.update(changeActivityPendingForDescripancyStatusQueryArtec, companyId,activityId,true,false,true,false,arTechRemark,Constants.COM_DESC,companyId,activityId,true,false,true,false,arTechRemark,Constants.COM_DESC);
		return true;
	}
	
	public boolean changeActivityStatusRejected(String companyId, String activityId) {
		this.jdbcTemplate.update(changeActivityRejectedForDescripancyStatusQueryArtec,Constants.COM_REJE,companyId,activityId);
		return true;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,
			int month,String userId,boolean isFullUser,String locationId) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			HashMap< String, String>namedParameter = new HashMap<String, String>();
			namedParameter.put("userId", userId);
			namedParameter.put("locationId", locationId);
			activityLast3MonthQueryFullUser = activityLast3MonthQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityLast3MonthQueryFullUser,namedParameter);
			return activities;
		}else{
			activityLast3MonthQuery = activityLast3MonthQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityLast3MonthQuery);
			return activities;
		}
	}
	
	/*public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,
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
	}*/
	
	public List<Map<String, Object>> getPendingActivityCoutForNext10Days(String companyId, String userId, boolean isFullUser, DashboardparamsBean dParam){
		companyId = "('"+companyId.replace(",", "','")+"')";
		if(isFullUser){
			rCountPending10DaysFullUser = rCountPending10DaysFullUser.replace("(?)", companyId);
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			String startDate = Util.getFyStartDateForForBean(dParam);
            String endDate = Util.getFyLastDateForForBean(dParam);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(rCountPending10DaysFullUser,userId,endDate,startDate);
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
	public Map<String, Object> getComplianceOverview(String companyId,String locationId,String userId,DashboardparamsBean dParam){
		companyId = "('"+companyId.replace(",", "','")+"')";
		Map namedParams = new HashMap();
        String startDate = Util.getFyStartDateForForBean(dParam);
        String endDate = Util.getFyLastDateForForBean(dParam);

        namedParams.put("locationId", locationId!=null?locationId:"all");
		namedParams.put("userId", userId);
        namedParams.put("startDate", startDate);
        namedParams.put("endDate", endDate);
        complianceOverviewFullUser = complianceOverviewFullUser.replace("(?)", companyId);
		List<Map<String, Object>> activitiesCount = this.namedParameterJdbcTemplate.queryForList(complianceOverviewFullUser,namedParams);
		return activitiesCount.get(0);
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByLawAndStatus(
			String companyId,String lawId,String status, String userId,boolean isFullUser,String locationId) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		Boolean isComplied = false,isComplianceApproved = false,isComplainceDelayed = false,isComplianceRejected = false,isProofRequired=false,isReOpenRequested=false;
		if("Pending compliance".equalsIgnoreCase(status)){
			isComplied = false;isComplianceApproved = false;isComplainceDelayed = false;isComplianceRejected = false;isProofRequired=false;isReOpenRequested=false;
		}else if("Complied-Delayed".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = true;isComplainceDelayed = true;isComplianceRejected = false;isProofRequired=false;isReOpenRequested=false;
		}else if("Pending for review".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = false;isComplainceDelayed = false;isComplianceRejected = false;isProofRequired=false;isReOpenRequested=false;
		}else if("Complied- In time".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = true;isComplainceDelayed = false;isComplianceRejected = false;isProofRequired=false;isReOpenRequested=false;
		}else if("Pending for Discrepancy".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = false;isComplianceApproved = false;isComplianceRejected = false;isProofRequired=true;isReOpenRequested=false;
		}
		
		if(isFullUser){
			Map namedMap = new HashMap();
			namedMap.put("isComplied",isComplied );
			namedMap.put("isComplianceApproved", isComplianceApproved);
			namedMap.put("isComplainceDelayed",isComplainceDelayed );
			namedMap.put("isComplianceRejected", isComplianceRejected);
			namedMap.put("isProofRequired", isProofRequired);
			namedMap.put("userId", userId);
			namedMap.put("locationId", locationId);
			namedMap.put("lawName", lawId);
			activityQueryByLawAndStatusFullUser = activityQueryByLawAndStatusFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.namedParameterJdbcTemplate.queryForList(activityQueryByLawAndStatusFullUser,namedMap);
			return activitiesCount;
		}else{
			//This is having a problem
			activityQueryByLawAndStatus = activityQueryByLawAndStatus.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(activityQueryByLawAndStatus,lawId,isComplied,isComplianceApproved,isComplainceDelayed,isComplianceRejected);
			return activitiesCount;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonthAndStatus(
			String companyId,String month,String status, String userId,boolean isFullUser,String locationId) {
		String months[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		List<String> monthsList = Arrays.asList(months);
		companyId = "('"+companyId.replace(",", "','")+"')";
		Boolean isComplied = false,isComplianceApproved = false,isComplainceDelayed = false,isComplianceRejected = false,isProofRequired=false,isReOpenRequested=false;
		if("Pending compliance".equalsIgnoreCase(status)){
			isComplied = false;isComplianceApproved = false;isComplainceDelayed = false;isComplianceRejected = false;isProofRequired=false;isReOpenRequested=false;
		}else if("Complied-Delayed".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = true;isComplainceDelayed = true;isComplianceRejected = false;isProofRequired=false;isReOpenRequested=false;
		}else if("Pending for review".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = false;isComplainceDelayed = false;isComplianceRejected = false;isProofRequired=false;isReOpenRequested=false;
		}else if("Complied- In time".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = true;isComplainceDelayed = false;isComplianceRejected = false;isProofRequired=false;isReOpenRequested=false;
		}else if("Pending for Discrepancy".equalsIgnoreCase(status)){
			isComplied = true;isComplianceApproved = false;isComplainceDelayed = false;isComplianceRejected = false;isProofRequired=true;isReOpenRequested=false;
		}
		
		int year = Util.getFinnancialYearForMonth(Integer.parseInt(month));
		
		Map namedQuery = new HashMap();
		namedQuery.put("isComplied",isComplied );
		namedQuery.put("isComplianceApproved",isComplianceApproved );
		namedQuery.put("isComplainceDelayed", isComplainceDelayed);
		namedQuery.put("isComplianceRejected", isComplianceRejected);
		namedQuery.put("isProofRequired", isProofRequired);
		namedQuery.put("locationId", locationId);
		namedQuery.put("dueMonth", month);
		namedQuery.put("dueYear", year);
		namedQuery.put("userId", userId);
		
		if(isFullUser){
			activityQueryByMonthAndStatusFullUser = activityQueryByMonthAndStatusFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.namedParameterJdbcTemplate.queryForList(activityQueryByMonthAndStatusFullUser,namedQuery);
			return activitiesCount;
		}else{
			//This is having a problem
			activityQueryByMonthAndStatus = activityQueryByMonthAndStatus.replace("(?)", companyId);
			List<Map<String, Object>> activitiesCount = this.jdbcTemplate.queryForList(activityQueryByMonthAndStatus,month,year,isComplied,isComplianceApproved,isComplainceDelayed,isComplianceRejected);
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
			String companyId, String year, boolean isFullUser,String location,String userId) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		String fromYear = year+"-04-01";
		String toYear = (Integer.parseInt(year)+1)+"-04-01";
		if(isFullUser){
			Map namedQuery = new HashMap();
			namedQuery.put("userId", userId);
			namedQuery.put("locationId", location);
			namedQuery.put("fromYear", fromYear);
			namedQuery.put("toYear", toYear);
			activityForYearQueryFullUserWithLocation = activityForYearQueryFullUserWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityForYearQueryFullUserWithLocation,namedQuery);
			return activities;
		}else{
			activityForYearQueryWithLocation = activityForYearQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForYearQueryWithLocation,location,fromYear,toYear);
			return activities;
		}
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(
			String companyId, boolean isFullUser,String quarter,String location,String userId) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		int year = Calendar.getInstance().get(Calendar.YEAR);
		//Indian quarter start at April so adding 1 
		int quarterInt = Integer.parseInt(quarter);
		year = Util.getFYForQuarter(quarterInt);
		String lastdayOfQuarter = Util.getLastDateOfQuarter(quarterInt);
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
			activityForQuarterQueryFullUser = activityForQuarterQueryFullUser.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.namedParameterJdbcTemplate.queryForList(activityForQuarterQueryFullUser,namedMap);
			return activities;
		}else{
			activityForQuarterQuery = activityForQuarterQuery.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForQuarterQuery,quarter,year);
			return activities;
		}
	}
	
	/*public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByQuarter(
			String companyId, boolean isFullUser,String quarter,String location) {
		companyId = "('"+companyId.replace(",", "','")+"')";
		int year = Calendar.getInstance().get(Calendar.YEAR);

		//Indian quarter start at April so adding 1 
		int quarterInt = Integer.parseInt(quarter);
		year = Util.getFYForQuarter(quarterInt);
		String lastdayOfQuarter = Util.getLastDateOfQuarter(quarterInt);
		
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
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForQuarterQueryFullUserWithLocation,location,quarter,year,lastdayOfQuarter);
			return activities;
		}else{
			activityForQuarterQueryWithLocation = activityForQuarterQueryWithLocation.replace("(?)", companyId);
			List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForQuarterQueryWithLocation,location,quarter,year);
			return activities;
		}
	}
*/	
}
