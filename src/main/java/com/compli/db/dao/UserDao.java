package com.compli.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.compli.bean.CompanyBean;
import com.compli.db.bean.UserBean;
import com.compli.util.DatabaseUtils;
import com.mysql.cj.api.jdbc.Statement;

public class UserDao {
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public UserBean getAllData(){
		UserBean userBean = null;
		List<UserBean> userBeans = (List<UserBean>) this.jdbcTemplate.query("select * from user", new BeanPropertyRowMapper(UserBean.class));
		if(userBeans!=null && userBeans.size() >0){
			userBean = userBeans.get(0);
		}
		return userBean;
	}
	
	public UserBean getUserData(String username,String password){
		UserBean userBean = null;
		List<UserBean> userBeans = (List<UserBean>) this.jdbcTemplate.query("select * from user where userId=? and pass=?",new Object[]{username,password}, new BeanPropertyRowMapper(UserBean.class));
		if(userBeans!=null && userBeans.size() >0){
			userBean = userBeans.get(0);
		}
		return userBean;
	}
	
	public boolean insertUserValues(UserBean userBean){
		/*
		try {
			HashMap<String, List> instval = DatabaseUtils.formInsertStatement(userBean);
			System.out.println(instval);
			int[] dest = new int[instval.get("type").toArray().length];
			//System.arraycopy(instval.get("type").toArray(), 0, dest, 0, instval.get("type").toArray().length);
			int index = 0;
			for(Object objInt:instval.get("type")){
				int objIntI = (Integer)objInt;
				System.out.println(objIntI);
				dest[index++] = objIntI;
			}
			this.jdbcTemplate.update(instval.get("query").get(0).toString(), 
					instval.get("value").toArray(), 
					dest);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		*/
		String insertUserQuery = "INSERT INTO user(isPrimaryUser, phone, regId, userId, isDeleted, email, pass, firstName, lastName, isactive) VALUES(?,?,?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(insertUserQuery,true,userBean.getPhone(),userBean.getRegId(),userBean.getUserId(),false,userBean.getEmail(),
				userBean.getPass(),userBean.getFirstName(),userBean.getLastName(),false)>0;
	}
	
	public void insertCompanyValues(CompanyBean companyBean){
		/*
		this.jdbcTemplate.update("insert into company(name) value(?)",companyBean.getCompanyName());
		this.jdbcTemplate.update("insert into company(name) value(?)",companyBean.getCompanyName());
		*/
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = 
		                connection.prepareStatement("insert into company(name) value(?)", 
		                    Statement.RETURN_GENERATED_KEYS);
		            ps.setString(1, companyBean.getCompanyName());
		            
		            return ps;
			}
		},keyHolder);
		System.out.println(keyHolder.getKeyList());
	}

	public boolean isUserExist(String userId) {
		return !this.jdbcTemplate.queryForList("select userId from user where userId = ?",userId).isEmpty();
	}
	
	public List<Map<String, Object>> geetUserCompanies(String userId) {
		return this.jdbcTemplate.queryForList("SELECT usercompany.companyId,name FROM usercompany inner join company on usercompany.companyId=company.companyId where usercompany.userId=?;",userId);
	}
	
	public boolean validateEmail(String registrationId) {
		return this.jdbcTemplate.update("update user set isactive=true where regId=?;",registrationId)>-1;		
	}
}
