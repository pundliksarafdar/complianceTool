package com.compli.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.migration.v2.ActivityMasterBean;

public class ActivityMasterDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//Using migration beans only
	public boolean addActivityForUpload(ActivityMasterBean activityMasterBean){
		String sql = "insert into activitymaster(activityId,lawId,activityName,description,periodicityId,riskId,consequence,periodicityDateId) values(?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(sql,activityMasterBean.getActivityId(),activityMasterBean.getLawId(),activityMasterBean.getActivityName(),activityMasterBean.getActivityDesc(),
					activityMasterBean.getPeriodicityId(),activityMasterBean.getRisk(),activityMasterBean.getConsequences(),activityMasterBean.getPeriodicityDateId())>0;
	}
	
	public List<ActivityMasterBean> getAllActivityMasterData(){
		String sql = "select activityId,lawId,activityName,description as activityDesc,periodicityId,riskId as risk,consequence as consequences,periodicityDateId from activitymaster";
		return this.jdbcTemplate.query(sql,new BeanPropertyRowMapper(ActivityMasterBean.class));
	}
}
