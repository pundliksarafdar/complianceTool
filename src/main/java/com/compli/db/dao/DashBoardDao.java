package com.compli.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.compli.bean.CompanyBean;
import com.compli.db.bean.UserBean;
import com.compli.util.DatabaseUtils;
import com.mysql.cj.api.jdbc.Statement;

public class DashBoardDao {
	private JdbcTemplate jdbcTemplate;
	private String activityQuery = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,isComplied,isComplianceApproved,isComplianceRejected from activity right join "+
			"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicitydatemaster.periodicityDateId,consequence,periodicitydatemaster.duedate from periodicitydatemaster inner join "+
		"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicitymaster.periodicityId,periodicitymaster.description as periodicityDesc,periodicityDateId,consequence from periodicitymaster inner join "+
			"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskmaster.riskId,riskmaster.description riskDes,periodicityId,periodicityDateId,consequence from riskmaster inner join "+
				"(select companyId,abbriviation,locationId,locationName,activityId,activityName,riskId,lawmaster.lawName,lawmaster.lawId,lawmaster.lawDesc,periodicityId,periodicityDateId,consequence from lawmaster inner join "+
					"(select companyId,abbriviation,lawId,locationId,locationName,activitymaster.activityId,activitymaster.activityName,riskId,periodicityId,periodicityDateId,consequence from activitymaster inner join "+
						"(select companyId,abbriviation,activityassociation.locationId,locationName,activityId from activityassociation inner join "+ 
							"(select companyId,abbriviation,location.locationId,locationName from location inner join	"+
								"(select cc.companyId,abbriviation,locationId from company cc inner join companylocation "+
									"on cc.companyId = companylocation.companyId where cc.companyId=?) companyWithLocation "+
							"on location.locationId = companyWithLocation.locationId) companyWithLocationName "+
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId";
	
	private String activityLast3MonthQuery = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,isComplied,isComplianceApproved,isComplianceRejected from activity right join "+
			"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicitydatemaster.periodicityDateId,consequence,periodicitydatemaster.duedate from periodicitydatemaster inner join "+
		"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicitymaster.periodicityId,periodicitymaster.description as periodicityDesc,periodicityDateId,consequence from periodicitymaster inner join "+
			"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskmaster.riskId,riskmaster.description riskDes,periodicityId,periodicityDateId,consequence from riskmaster inner join "+
				"(select companyId,abbriviation,locationId,locationName,activityId,activityName,riskId,lawmaster.lawName,lawmaster.lawId,lawmaster.lawDesc,periodicityId,periodicityDateId,consequence from lawmaster inner join "+
					"(select companyId,abbriviation,lawId,locationId,locationName,activitymaster.activityId,activitymaster.activityName,riskId,periodicityId,periodicityDateId,consequence from activitymaster inner join "+
						"(select companyId,abbriviation,activityassociation.locationId,locationName,activityId from activityassociation inner join "+ 
							"(select companyId,abbriviation,location.locationId,locationName from location inner join	"+
								"(select cc.companyId,abbriviation,locationId from company cc inner join companylocation "+
									"on cc.companyId = companylocation.companyId where cc.companyId=?) companyWithLocation "+
							"on location.locationId = companyWithLocation.locationId) companyWithLocationName "+
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId";
	
	private String activityForMonthQuery = "select companyWithPerDate.companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,companyWithPerDate.activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicityDateId,consequence,duedate,month(dueDate) as dueMonth,isComplied,isComplianceApproved,isComplianceRejected from activity right join "+
			"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicityId,periodicityDesc,periodicitydatemaster.periodicityDateId,consequence,periodicitydatemaster.duedate from periodicitydatemaster inner join "+
		"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskId,riskDes,periodicitymaster.periodicityId,periodicitymaster.description as periodicityDesc,periodicityDateId,consequence from periodicitymaster inner join "+
			"(select companyId,abbriviation,lawId,lawDesc,lawName,locationId,locationName,activityId,activityName,riskmaster.riskId,riskmaster.description riskDes,periodicityId,periodicityDateId,consequence from riskmaster inner join "+
				"(select companyId,abbriviation,locationId,locationName,activityId,activityName,riskId,lawmaster.lawName,lawmaster.lawId,lawmaster.lawDesc,periodicityId,periodicityDateId,consequence from lawmaster inner join "+
					"(select companyId,abbriviation,lawId,locationId,locationName,activitymaster.activityId,activitymaster.activityName,riskId,periodicityId,periodicityDateId,consequence from activitymaster inner join "+
						"(select companyId,abbriviation,activityassociation.locationId,locationName,activityId from activityassociation inner join "+ 
							"(select companyId,abbriviation,location.locationId,locationName from location inner join	"+
								"(select cc.companyId,abbriviation,locationId from company cc inner join companylocation "+
									"on cc.companyId = companylocation.companyId where cc.companyId=?) companyWithLocation "+
							"on location.locationId = companyWithLocation.locationId) companyWithLocationName "+
						"on companyWithLocationName.locationId = activityassociation.locationId) companyWithActivityId "+
					"on companyWithActivityId.activityId = activitymaster.activityId) companyWithActivity "+
				"on companyWithActivity.lawId = lawmaster.lawId) companyWithLaw	"+	
			"on riskmaster.riskId = companyWithLaw.riskId) companyWithRisk "+
		"on periodicitymaster.periodicityId = companyWithRisk.periodicityId) companyWithPeriodicity "+ 
	"on periodicitydatemaster.periodicityDateId = companyWithPeriodicity.periodicityDateId  where month(periodicitydatemaster.duedate)=?) companyWithPerDate  "+
		"on companyWithPerDate.companyId = activity.companyId and companyWithPerDate.activityId = activity.activityId";
	
	String changeActivityStatusQuery = "INSERT INTO activity (companyId,activityId, isComplied,isComplianceApproved) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE companyId=?,activityId=?, isComplied=?,isComplianceApproved=?";
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Map<String, Object>> getAllActivitiesForCompany(String companyId){
		List<Map<String, Object>> activities = this.jdbcTemplate.queryForList("select * from activitymaster inner join (select activityId from activityassociation where locationId = (select locationId from companylocation where companyId=?)) activityLocation on activitymaster.activityId = activityLocation.activityId;", companyId);
		return activities;
	}
	
	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompany(String companyId){
		List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityQuery,companyId);
		return activities;
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyByMonth(String companyId, String month) {
		List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityForMonthQuery,companyId,month);
		return activities;
	}

	public boolean changeActivityStatus(String companyId, String activityId,boolean status) {
		boolean isDemoUser = true;
		this.jdbcTemplate.update(changeActivityStatusQuery, companyId,activityId,status,isDemoUser, companyId,activityId,status,isDemoUser);
		return true;
	}

	public List<Map<String, Object>> getAllActivitiesWithDescriptionForCompanyLast3Months(String companyId,
			int month) {
		List<Map<String, Object>> activities = this.jdbcTemplate.queryForList(activityLast3MonthQuery, companyId);
		return activities;
	}
}
