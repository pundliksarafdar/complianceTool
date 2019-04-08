package com.compli.db.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.migration.v2.UserCompanyBean;

public class UserCompanyDao {
	private JdbcTemplate jdbcTemplate;
	private static String USER_FOR_COMPANY = "select user.userId,firstname,lastname,email,userTypeId from user join	(select userId from userCompany where companyId=?)userForActivityon user.userId=userForActivity.userId;";
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<UserCompanyBean> getAllUsersCompany(){
		List<UserCompanyBean> userCompanyBeans = (List<UserCompanyBean>) this.jdbcTemplate.query("select * from usercompany", new BeanPropertyRowMapper(UserCompanyBean.class));
		return userCompanyBeans;
	}
	
	public List<UserCompanyBean> getAllUsersForCompany(String companyId){
		List<UserCompanyBean> userCompanyBeans = null;//(List<UserCompanyBean>) this.jdbcTemplate.query(USER_FOR_COMPANY,companyId, new BeanPropertyRowMapper(UserCompanyBean.class));
		return userCompanyBeans;
	}
	
	
	public boolean addUserCompanyForUpload(UserCompanyBean companyBean){
		String sql = "insert into usercompany(userId,companyId) values(?,?)";
		return this.jdbcTemplate.update(sql,companyBean.getUserId(),companyBean.getCompanyId())>0;
		
	}
}
