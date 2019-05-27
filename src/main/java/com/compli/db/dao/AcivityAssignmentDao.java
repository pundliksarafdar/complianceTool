package com.compli.db.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.Pair;
import com.compli.db.bean.migration.v2.ActivityAssociationBean;
import com.compli.util.bean.ActivityAssignnmentBean;

public class AcivityAssignmentDao {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public  void saveActivityAssignment(List<ActivityAssignnmentBean>actAssociationBean){
		String sql = "INSERT INTO activity_assignment(activityId,userId) VALUES (?,?) ON DUPLICATE KEY UPDATE modifiedOn=NOW()";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ActivityAssignnmentBean pair = actAssociationBean.get(i);
				ps.setString(1, pair.getActivityId());
				ps.setString(2, pair.getUserId());				
			}
			 
			@Override
			public int getBatchSize() {
				return actAssociationBean.size();
			}
		});
	}
	
	public  void removeActivityAssignment(List<ActivityAssignnmentBean>actAssociationBean){
		String sql = "delete from activity_assignment where activityId=? and userId=?;";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ActivityAssignnmentBean pair = actAssociationBean.get(i);
				ps.setString(1, pair.getActivityId());
				ps.setString(2, pair.getUserId());				
			}
			 
			@Override
			public int getBatchSize() {
				return actAssociationBean.size();
			}
		});
	}
	
	public List<ActivityAssignnmentBean> getActivityAssignment(){
		String sql = "select * from activity_assignment";
		return this.jdbcTemplate.query(sql, new BeanPropertyRowMapper(ActivityAssignnmentBean.class));
	}
}
