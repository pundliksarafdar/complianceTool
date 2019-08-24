package com.compli.db.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.compli.bean.ActivityUser;
import com.compli.bean.access.UserAccess;
import com.compli.db.bean.migration.v2.ActivityBean;
import com.google.gson.Gson;

public class UserAccessDao {
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public List<com.compli.db.bean.UserAccess> getAllUserAccess(){
		String getAllUsersAccess = "select user.userId,accesscontrol,firstname,lastname,userTypeId from users_access right join user on user.userId=users_access.userId where user.userTypeId='demoUser'";
		return this.namedParameterJdbcTemplate.query(getAllUsersAccess,new BeanPropertyRowMapper(com.compli.db.bean.UserAccess.class));
	}
	
	public boolean updateUserAccess(com.compli.db.bean.UserAccess userAccess){
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userAccess.getUserId());
		param.addValue("accesscontrol", userAccess.getAccesscontrol());
		String updateAccess = "insert into users_access set userId=:userId,accesscontrol=:accesscontrol on duplicate key  update accesscontrol=:accesscontrol";
		return this.namedParameterJdbcTemplate.update(updateAccess, param)>0;
	}
	
	public com.compli.db.bean.UserAccess getUsersAccess(String userId){
		String getAllAccess = "select * from users_access where userId=:userId";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		List<com.compli.db.bean.UserAccess> accesses = this.namedParameterJdbcTemplate.query(getAllAccess,param,new BeanPropertyRowMapper(com.compli.db.bean.UserAccess.class));
		if(accesses!=null && accesses.size()>0){
			return accesses.get(0);	
		}else{
			return null;
		}		 
	}
	
	public void setActivityUser(ActivityUser activityUser){
		String updateSql = "update activity set assignedUser=:assignedUser where activityId in (:ids);";
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("ids", activityUser.getActivityIds());
		param.addValue("assignedUser", activityUser.getActivityUser());
		this.namedParameterJdbcTemplate.update(updateSql, param);
		
	}
	}
