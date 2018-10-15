package com.compli.db.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.LocationBean;
import com.compli.db.bean.PeriodicityDateMasterBean;
import com.compli.db.bean.PeriodicityMasterBean;


public class PeriodicityDateMasterDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<PeriodicityDateMasterBean> getAllPeriodictyDate() {
		List<PeriodicityDateMasterBean> periodicityDateData = this.jdbcTemplate.query("select * from periodicitydatemaster", new BeanPropertyRowMapper(PeriodicityDateMasterBean.class));
		return periodicityDateData;
	}
	
	public boolean addPeriodicityMasterForUpload(PeriodicityDateMasterBean locationDateBean){
		String sql = "insert into periodicitydatemaster(periodicityDateId,dueDate) values(?,?)";
		return this.jdbcTemplate.update(sql,locationDateBean.getPeriodicityDateId(),locationDateBean.getDueDate())>0;
	}
}
