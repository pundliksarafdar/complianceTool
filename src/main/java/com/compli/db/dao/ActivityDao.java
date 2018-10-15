package com.compli.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;

public class ActivityDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	//Using migration beans only
	public boolean addActivityForUpload(ActivityBean activityBean){
		String sql = "insert into activity(activityId,companyId,isComplied,isComplianceApproved,isProofRequired,isComplianceRejected,isComplainceDelayed,remark,assignedUser) values(?,?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(sql,activityBean.getActivityId(),activityBean.getCompanyId(),activityBean.isComplied(),
				activityBean.isComplianceApproved(),activityBean.isProofRequired(),activityBean.isComplianceRejected(),activityBean.isComplainceDelayed(),
				activityBean.getRemark(),activityBean.getAssignedUser())>0;
	}
	
	public List<ActivityBean> getAllActivityData(){
		String sql = "select * from activity";
		return this.jdbcTemplate.query(sql,new BeanPropertyRowMapper(ActivityBean.class));
	}
}
