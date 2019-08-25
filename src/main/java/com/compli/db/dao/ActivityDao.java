package com.compli.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.compli.bean.ActivityUser;
import com.compli.bean.stats.RiskIdCount;
import com.compli.db.bean.UserBean;
import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.util.Constants;
import com.notifier.emailbean.PendingActivitiesForMail;
import com.notifier.emailbean.PendingForDiscrepancy;

public class ActivityDao {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public void setActivityUser(ActivityUser activityUser){
		String updateSql = "update activity set assignedUser=:assignedUser where activityId in (:ids);";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("ids", activityUser.getActivityIds());
		param.addValue("assignedUser", activityUser.getActivityUser());
		this.namedParameterJdbcTemplate.update(updateSql, param);		
	}
	//Using migration beans only
	String ACTIVITY_ID = "select user.userId,firstname,lastname,email,userTypeId from user join	(select userId from activity_assignment where activityId=?)userforactivity on user.userId=userforactivity.userId;";
	
	public int getMaximumActivityId(){
		List<Map<String, Object>> maxActivity = this.jdbcTemplate.queryForList("select CAST(activityId AS UNSIGNED)as maxactivitycount from activity order by maxactivitycount desc limit 1;");
		return Integer.parseInt((String) maxActivity.get(0).get("maxactivitycount").toString());
	}
	
	public List<UserBean> getUsersForActivity(String activityId){
		return this.jdbcTemplate.query(ACTIVITY_ID,new Object[]{activityId},new BeanPropertyRowMapper(UserBean.class));
	}
	
	public boolean addActivityForUpload(ActivityBean activityBean){
		String sql = "insert into activity(activityId,companyId,isComplied,isComplianceApproved,isProofRequired,isComplianceRejected,isComplainceDelayed,remark,assignedUser,completionDate,activityStatus) values(?,?,?,?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(sql,activityBean.getActivityId(),activityBean.getCompanyId(),activityBean.isComplied(),
				activityBean.isComplianceApproved(),activityBean.isProofRequired(),activityBean.isComplianceRejected(),activityBean.isComplainceDelayed(),
				activityBean.getRemark(),activityBean.getAssignedUser(),activityBean.getCompletionDate(),activityBean.getActivityStatus())>0;
	}
	
	public List<ActivityBean> getAllActivityData(){
		String sql = "select * from activity";
		return this.jdbcTemplate.query(sql,new BeanPropertyRowMapper(ActivityBean.class));
	}
	
	public ActivityBean getActivityData(String activityId){
		String sql = "select * from activity where activityId=?";
		List<ActivityBean> list = this.jdbcTemplate.query(sql,new Object[]{activityId},new BeanPropertyRowMapper(ActivityBean.class));
		return list.get(0);
	}
	
	public PendingForDiscrepancy getActivityDataForMail(String activityId){
		String sql = "select user.userId,firstname as user,userTypeId,email,activityName,dueDate as periodictyDate from user inner join "+ 
									"(select userId,activityName,dueDate from usercompany inner join "+
										"(select activity.activityId,activity.companyId,activityName,dueDate from activity inner join "+ 
											"(SELECT activityId,activityName,dueDate FROM compli.activitymaster inner join periodicitydatemaster on activitymaster.periodicityDateId=periodicitydatemaster.periodicityDateId) tt "+ 
										"on activity.activityId = tt.activityId and tt.activityId=?) activityFull "+
									"on usercompany.companyId= activityFull.companyId) userAndActivity "+
									"on user.userId=userAndActivity.userId where userTypeId='cOwner';";
		List<PendingForDiscrepancy> list = this.jdbcTemplate.query(sql,new Object[]{activityId},new BeanPropertyRowMapper(PendingForDiscrepancy.class));
		return list!=null?list.get(0):null;
	}
	
	public PendingForDiscrepancy getActivityDataForArTechMail(String activityId){
		String sql = "select user.userId,firstname as user,userTypeId,email,activityName,dueDate as periodictyDate from user inner join "+ 
									"(select userId,activityName,dueDate from usercompany inner join "+
										"(select activity.activityId,activity.companyId,activityName,dueDate from activity inner join "+ 
											"(SELECT activityId,activityName,dueDate FROM compli.activitymaster inner join periodicitydatemaster on activitymaster.periodicityDateId=periodicitydatemaster.periodicityDateId) tt "+ 
										"on activity.activityId = tt.activityId and tt.activityId=?) activityFull "+
									"on usercompany.companyId= activityFull.companyId) userAndActivity "+
									"on user.userId=userAndActivity.userId where userTypeId='ArTechUser';";
		List<PendingForDiscrepancy> list = this.jdbcTemplate.query(sql,new Object[]{activityId},new BeanPropertyRowMapper(PendingForDiscrepancy.class));
		return list!=null?list.get(0):null;
	}
	
	//This is request to reopen
	public void reopenActivity(String activityId, String companyId) {
		String sql = "UPDATE activity set reOpen=true,activityStatus=? where activityId=? and companyId=?";
		this.jdbcTemplate.update(sql,Constants.REQUESTED_REOPEN, activityId,companyId);
	}
	
	public void changeToOpen(String activityId, String companyId) {
		String sql = "UPDATE activity set reOpen=false,isComplied=false,isProofRequired=false where activityId=? and companyId=?";
		this.jdbcTemplate.update(sql, activityId,companyId);
	}
	
	public List<PendingActivitiesForMail> getOlderActivitiesForMail(int maxDateInterval,String userType){
		String sql = "select description,lawname as complianceArea,locationName,companyId,activityId,activityName,desc1,userId,firstname,userTypeId,email,dueDate,googleId from periodicitymaster inner join "+
	"(select lawname,locationName,periodicityId,companyId,activityId,activityName,desc1,userId,firstname,userTypeId,email,dueDate,googleId from lawmaster inner join "+
		"(select locationName,lawId,periodicityId,companyId,activityId,activityName,desc1,userId,firstname,userTypeId,email,dueDate,googleId from location inner join "+
			"(select locationId,lawId,periodicityId,companyId,activityUser.activityId,activityName,desc1,userId,firstname,userTypeId,email,dueDate,googleId from activityassociation inner join "+ 
				"(select lawId,periodicityId,companyId,activityId,activityName,desc1,user.userId,firstname,userTypeId,email,dueDate,googleId from user inner join  "+
					"(select lawId,periodicityId,usercompany.companyId,isComplied,activityId,activityName,desc1,userId,dueDate from usercompany join "+ 
						"(select lawId,periodicityId,companyId,isComplied,activityId,activityName,desc1,dueDate from periodicitydatemaster join  "+
							"(SELECT activity.companyId,activitymaster.lawId,activitymaster.periodicityId,activitymaster.periodicityDateId,activity.isComplied,activity.activityId,activitymaster.activityName,description as desc1 FROM activity join "+ 
								"activitymaster on activity.activityId = activitymaster.activityId) activityDueDate  "+
							"on periodicitydatemaster.periodicityDateId=activityDueDate.periodicityDateId where dueDate = adddate(DATE(now()),?)  and isComplied=false) activityWithCompany "+ 
						"on activityWithCompany.companyId = usercompany.companyId)userCOmpanyMergedTable "+ 
				"on userCOmpanyMergedTable.userId = user.userId where userTypeId=?) activityUser "+
			"on activityUser.activityId=activityassociation.activityId)activityWithAssociation "+
		"on activityWithAssociation.locationId = location.locationId) activityWithLocation "+
	"on activityWithLocation.lawId = lawmaster.lawId) activitywithLaw "+
"on activitywithLaw.periodicityId = periodicitymaster.periodicityId";
		return this.jdbcTemplate.query(sql, new Object[]{maxDateInterval,userType},new BeanPropertyRowMapper(PendingActivitiesForMail.class));
	}
	
	//THis and above function dont have any difference execpt here we calculate till data and in above we take to date
	public List<PendingActivitiesForMail> getOlderActivitiesForMailTillDays(int maxDateInterval,String userType,String googleId){
		String sql = "select description,lawname as complianceArea,locationName,companyId,activityId,activityName,desc1,userId,firstname,userTypeId,email,dueDate,googleId from periodicitymaster inner join "+
	"(select lawname,locationName,periodicityId,companyId,activityId,activityName,desc1,userId,firstname,userTypeId,email,dueDate,googleId from lawmaster inner join "+
		"(select locationName,lawId,periodicityId,companyId,activityId,activityName,desc1,userId,firstname,userTypeId,email,dueDate,googleId from location inner join "+
			"(select locationId,lawId,periodicityId,companyId,activityUser.activityId,activityName,desc1,userId,firstname,userTypeId,email,dueDate,googleId from activityassociation inner join "+ 
				"(select lawId,periodicityId,companyId,activityId,activityName,desc1,user.userId,firstname,userTypeId,email,dueDate,googleId from user inner join  "+
					"(select lawId,periodicityId,usercompany.companyId,isComplied,activityId,activityName,desc1,userId,dueDate from usercompany join "+ 
						"(select lawId,periodicityId,companyId,isComplied,activityId,activityName,desc1,dueDate from periodicitydatemaster join  "+
							"(SELECT activity.companyId,activitymaster.lawId,activitymaster.periodicityId,activitymaster.periodicityDateId,activity.isComplied,activity.activityId,activitymaster.activityName,description as desc1 FROM activity join "+ 
								"activitymaster on activity.activityId = activitymaster.activityId) activityDueDate  "+
							"on periodicitydatemaster.periodicityDateId=activityDueDate.periodicityDateId where dueDate <= adddate(DATE(now()),?)  and isComplied=false) activityWithCompany "+ 
						"on activityWithCompany.companyId = usercompany.companyId)userCOmpanyMergedTable "+ 
				"on userCOmpanyMergedTable.userId = user.userId where userTypeId=?) activityUser "+
			"on activityUser.activityId=activityassociation.activityId)activityWithAssociation "+
		"on activityWithAssociation.locationId = location.locationId) activityWithLocation "+
	"on activityWithLocation.lawId = lawmaster.lawId) activitywithLaw "+
"on activitywithLaw.periodicityId = periodicitymaster.periodicityId  where googleId=?";
		return this.jdbcTemplate.query(sql, new Object[]{maxDateInterval,userType,googleId},new BeanPropertyRowMapper(PendingActivitiesForMail.class));
	}
	
	public void updatePeriodicityDateOfActivity(List<String>activityIds,String date){
		String updateSql = "update activitymaster set periodicityDateId=:date where activityId in (:ids);";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("ids", activityIds);
		param.addValue("date", date);
		this.namedParameterJdbcTemplate.update(updateSql, param);
	}
	
	public void removeUserForCompany(String userId,String companyId){
		String deleteQueryToDeleteActivityAssignement = "delete from activity_assignment where activityId in(select activityId from activity where companyId=:companyId) and userId=:userId";
		String deleteUserFromCompany = "delete from usercompany where userId=:userId and companyId=:companyId";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("companyId", companyId);
		this.namedParameterJdbcTemplate.update(deleteQueryToDeleteActivityAssignement, param);
		this.namedParameterJdbcTemplate.update(deleteUserFromCompany, param);
	}
	
	public void updateActivityRisk(String lawId,String updateToLaw){
		String udateQuery = "update activitymaster set activitymaster.riskId=:updateToLaw where activityId in ( "+
				  "select activityId from(	 "+
					"select activityId from activitymaster "+ 
					"join periodicitydatemaster on periodicitydatemaster.periodicityDateId=activitymaster.periodicityDateId "+ 
					"where activityId in (select activityId from activity where activityStatus='pendingCompliance') "+ 
					"and periodicitydatemaster.dueDate = date(now()-interval 3 month) "+
					"and activitymaster.riskId=:lawId)as c "+
				");";
		
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("lawId", lawId);
		param.addValue("updateToLaw", updateToLaw);
		this.namedParameterJdbcTemplate.update(udateQuery, param);	
	}
	
	public List<RiskIdCount> getRiskUpdateCountForToday(){
		String query = "select riskId,count(riskId)as count from activitymaster "+
			"join periodicitydatemaster on periodicitydatemaster.periodicityDateId=activitymaster.periodicityDateId "+ 
			"where activityId in (select activityId from activity where activityStatus='pendingCompliance')  "+
			"and periodicitydatemaster.dueDate = date(now()-interval 3 month) group by riskId";
		return this.jdbcTemplate.query(query, new BeanPropertyRowMapper(RiskIdCount.class));
	}
	
	//this method will give access to userId from "fromDate"
	public void updateUserAccess(String userId,String fromDate,List<String> lawIds){
		String query = "insert into activity_assignment(activityId,userId,modifiedOn) "+ 
										"select activityId,:userId,now() from periodicitydatemaster inner join "+ 
											"(select activitymaster.activityId,activitymaster.periodicityDateId from activitymaster inner join lawmaster on activitymaster.lawId=lawmaster.lawId "+ 
											"where lawmaster.lawName in (:lawIds)) aMaster "+
										"on aMaster.periodicityDateId=periodicitydatemaster.periodicityDateId where DATE_FORMAT(periodicitydatemaster.dueDate,'%Y%m')>=DATE_FORMAT(:fromDate,'%Y%m') "+
									"on duplicate key UPDATE modifiedOn=now()";
		
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("fromDate", fromDate);
		param.addValue("lawIds", lawIds);
		this.namedParameterJdbcTemplate.update(query, param);
	}
}
