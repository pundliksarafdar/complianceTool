package com.compli.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.compli.db.bean.Files;
import com.compli.db.bean.PeriodicityDateMasterBean;
import com.compli.db.bean.PeriodicityMasterBean;
import com.compli.db.bean.UserBean;

public class PeriodicityMasterDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<PeriodicityMasterBean> getAllPeriodicty() {
		List<PeriodicityMasterBean> periodicityData = this.jdbcTemplate.query("select * from periodicitymaster", new BeanPropertyRowMapper(PeriodicityMasterBean.class));
		return periodicityData;
	}	
	
	public boolean addPeriodicityMasterForUpload(PeriodicityMasterBean locationBean){
		String sql = "insert into periodicitymaster(periodicityId,description) values(?,?)";
		return this.jdbcTemplate.update(sql,locationBean.getPeriodicityId(),locationBean.getDescription())>0;
	}
}
