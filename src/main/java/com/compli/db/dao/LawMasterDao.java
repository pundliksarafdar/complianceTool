package com.compli.db.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.LawMasterBean;
import com.compli.db.bean.PeriodicityDateMasterBean;


public class LawMasterDao {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<LawMasterBean> getAllLaw() {
		List<LawMasterBean> lawData = this.jdbcTemplate.query("select * from lawmaster", new BeanPropertyRowMapper(LawMasterBean.class));
		return lawData;
	}
	
	public boolean addLawForUpload(LawMasterBean lawMasterBean){
		String sql = "insert into lawmaster(lawId,lawName,lawDesc) values(?,?,?)";
		return this.jdbcTemplate.update(sql,lawMasterBean.getLawId(),lawMasterBean.getLawName(),lawMasterBean.getLawDesc())>0;
	}
}
