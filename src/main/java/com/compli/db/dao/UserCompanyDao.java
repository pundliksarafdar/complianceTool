package com.compli.db.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.UsersForCompany;
import com.compli.db.bean.migration.v2.UserCompanyBean;

public class UserCompanyDao {
	private JdbcTemplate jdbcTemplate;
	private static String USER_FOR_COMPANY = "select firstname,lastname,user.userId,email,userTypeId from user join usercompany on user.userId=usercompany.userId where usercompany.companyId=?;";
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<UserCompanyBean> getAllUsersCompany(){
		List<UserCompanyBean> userCompanyBeans = (List<UserCompanyBean>) this.jdbcTemplate.query("select * from usercompany", new BeanPropertyRowMapper(UserCompanyBean.class));
		return userCompanyBeans;
	}
	
	public List<UsersForCompany> getAllUsersForCompany(String companyId){
		List<UsersForCompany> userCompanyBeans = (List<UsersForCompany>) this.jdbcTemplate.query(USER_FOR_COMPANY,new Object[]{companyId}, new BeanPropertyRowMapper(UsersForCompany.class));
		
		return userCompanyBeans;
	}
	
	
	public boolean addUserCompanyForUpload(UserCompanyBean companyBean){
		String sql = "insert into usercompany(userId,companyId) values(?,?)";
		return this.jdbcTemplate.update(sql,companyBean.getUserId(),companyBean.getCompanyId())>0;
		
	}
}
