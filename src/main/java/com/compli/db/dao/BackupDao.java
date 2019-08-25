package com.compli.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.compli.bean.backup.User;

public class BackupDao {
private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public List<Object> getUserData(){
		List<Object> userData = new ArrayList<Object>();
		String sql = "select * from user";
		userData = this.namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper(User.class));
		return userData;
	}
}
