package com.compli.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.UserBean;
import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.compli.util.Constants;
import com.notifier.emailbean.PendingActivitiesForMail;
import com.notifier.emailbean.PendingForDiscrepancy;

public class ActivityDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	//Using migration beans only
	String ACTIVITY_ID = "select user.userId,firstname,lastname,email,userTypeId from user join	(select userId from activity_assignment where activityId=?)userforactivity on user.userId=userforactivity.userId;";
	
	public int getMaximumActivityId(){
		Map<String, Object> maxActivity = this.jdbcTemplate.queryForMap("select max(activityId) as maxActivityCount from activity;");
		return Integer.parseInt((String) maxActivity.get("maxactivitycount"));
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
}
