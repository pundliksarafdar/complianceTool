package com.compli.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;
import com.notifier.emailbean.PendingActivitiesForMail;
import com.notifier.emailbean.PendingForDiscrepancy;

public class ActivityDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	//Using migration beans only
	public boolean addActivityForUpload(ActivityBean activityBean){
		String sql = "insert into activity(activityId,companyId,isComplied,isComplianceApproved,isProofRequired,isComplianceRejected,isComplainceDelayed,remark,assignedUser,completionDate) values(?,?,?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(sql,activityBean.getActivityId(),activityBean.getCompanyId(),activityBean.isComplied(),
				activityBean.isComplianceApproved(),activityBean.isProofRequired(),activityBean.isComplianceRejected(),activityBean.isComplainceDelayed(),
				activityBean.getRemark(),activityBean.getAssignedUser(),activityBean.getCompletionDate())>0;
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
											"(SELECT activityId,activityName,dueDate FROM compli.activityMaster inner join periodicitydatemaster on activityMaster.periodicityDateId=periodicitydatemaster.periodicityDateId) tt "+ 
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
											"(SELECT activityId,activityName,dueDate FROM compli.activityMaster inner join periodicitydatemaster on activityMaster.periodicityDateId=periodicitydatemaster.periodicityDateId) tt "+ 
										"on activity.activityId = tt.activityId and tt.activityId=?) activityFull "+
									"on usercompany.companyId= activityFull.companyId) userAndActivity "+
									"on user.userId=userAndActivity.userId where userTypeId='ArTechUser';";
		List<PendingForDiscrepancy> list = this.jdbcTemplate.query(sql,new Object[]{activityId},new BeanPropertyRowMapper(PendingForDiscrepancy.class));
		return list!=null?list.get(0):null;
	}
	
	//This is request to reopen
	public void reopenActivity(String activityId, String companyId) {
		String sql = "UPDATE activity set reOpen=true where activityId=? and companyId=?";
		this.jdbcTemplate.update(sql, activityId,companyId);
	}
	
	public void changeToOpen(String activityId, String companyId) {
		String sql = "UPDATE activity set reOpen=false,isComplied=false,isProofRequired=false where activityId=? and companyId=?";
		this.jdbcTemplate.update(sql, activityId,companyId);
	}
	
	public List<PendingActivitiesForMail> getOlderActivitiesForMail(int maxDateInterval,String userType){
		String sql = "select description,lawname as complianceArea,locationName,companyId,activityId,activityName,userId,firstname,userTypeId,email,dueDate from periodicitymaster inner join "+
	"(select lawname,locationName,periodicityId,companyId,activityId,activityName,userId,firstname,userTypeId,email,dueDate from lawmaster inner join "+
		"(select locationName,lawId,periodicityId,companyId,activityId,activityName,userId,firstname,userTypeId,email,dueDate from location inner join "+
			"(select locationId,lawId,periodicityId,companyId,activityUser.activityId,activityName,userId,firstname,userTypeId,email,dueDate from activityassociation inner join "+ 
				"(select lawId,periodicityId,companyId,activityId,activityName,user.userId,firstname,userTypeId,email,dueDate from user inner join  "+
					"(select lawId,periodicityId,usercompany.companyId,isComplied,activityId,activityName,userId,dueDate from usercompany join "+ 
						"(select lawId,periodicityId,companyId,isComplied,activityId,activityName,dueDate from periodicitydatemaster join  "+
							"(SELECT activity.companyId,activityMaster.lawId,activityMaster.periodicityId,activityMaster.periodicityDateId,activity.isComplied,activity.activityId,activityMaster.activityName FROM activity join "+ 
								"activityMaster on activity.activityId = activityMaster.activityId) activityDueDate  "+
							"on periodicitydatemaster.periodicityDateId=activityDueDate.periodicityDateId where dueDate = adddate(DATE(now()),-?)  and isComplied=false) activityWithCompany "+ 
						"on activityWithCompany.companyId = usercompany.companyId)userCOmpanyMergedTable "+ 
				"on userCOmpanyMergedTable.userId = user.userId where userTypeId=?) activityUser "+
			"on activityUser.activityId=activityassociation.activityId)activityWithAssociation "+
		"on activityWithAssociation.locationId = location.locationId) activityWithLocation "+
	"on activityWithLocation.lawId = lawmaster.lawId) activitywithLaw "+
"on activitywithLaw.periodicityId = periodicitymaster.periodicityId";
		return this.jdbcTemplate.query(sql, new Object[]{maxDateInterval,userType},new BeanPropertyRowMapper(PendingActivitiesForMail.class));
	}
}
