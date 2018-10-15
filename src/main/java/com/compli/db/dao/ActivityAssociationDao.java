package com.compli.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.db.bean.migration.v2.ActivityMasterBean;

public class ActivityAssociationDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	//Using migration beans only
	public boolean addActivityAssociationForUpload(ActivityAssociationBean activityMasterBean){
		String sql = "insert into activityassociation(associationId,activityId,fyRecordId,locationId) values(?,?,?,?)";
		return this.jdbcTemplate.update(sql,activityMasterBean.getAssociationId(),activityMasterBean.getActivityId(),activityMasterBean.getFyRecordId(),activityMasterBean.getLocationId())>0;
	}
	
	public List<ActivityAssociationBean> getAllActivityAssociationData(){
		String sql = "select * from activityassociation";
		return this.jdbcTemplate.query(sql,new BeanPropertyRowMapper(ActivityAssociationBean.class));
	}
}
