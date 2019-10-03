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
	
	private String UPDATE_USER_DATA = "update user set firstname=? , lastname=? , image=? , googleId = ? , phone = ? where userId=?";
	private String UPDATE_USER_DATA_FOR_MASTER = "update user set firstname=? ,lastname=? , email=? , pass=? , phone = ?,userTypeId = ? where userId=?";
	private String UPDATE_USER_PASS = "update user set pass=? where userId=?";

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<UserBean> getAllData(){
		UserBean userBean = null;
		List<UserBean> userBeans = (List<UserBean>) this.jdbcTemplate.query("select * from user", new BeanPropertyRowMapper(UserBean.class));
		return userBeans;
	}
	
	public UserBean getUserData(String username,String password){
		UserBean userBean = null;
		List<UserBean> userBeans = (List<UserBean>) this.jdbcTemplate.query("select * from user where ((userId=? or email=?) and pass=?)",new Object[]{username,username,password}, new BeanPropertyRowMapper(UserBean.class));
		if(userBeans!=null && userBeans.size() >0){
			userBean = userBeans.get(0);
		}
		return userBean;
	}
	
	public boolean insertUserValues(UserBean userBean){
		String insertUserQuery = "INSERT INTO user(isPrimaryUser, phone, regId, userId, isDeleted, email, pass, firstName, lastName, isactive) VALUES(?,?,?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(insertUserQuery,true,userBean.getPhone(),userBean.getRegId(),userBean.getUserId(),false,userBean.getEmail(),
				userBean.getPass(),userBean.getFirstName(),userBean.getLastName(),false)>0;
	}
	
	
	/** This method is used to insert user, User will be added directly without activation
	 * This is to inset user into data base written for master user admin panel add user 
	 * @param userBean
	 * @return
	 */
	public boolean insertUserValuesForMaster(UserBean userBean){
		String insertUserQuery = "INSERT INTO user(isPrimaryUser, phone, regId, userId, isDeleted, email, pass, firstName, lastName, isactive,isFullUser,userTypeId) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(insertUserQuery,true,userBean.getPhone(),userBean.getRegId(),userBean.getUserId(),false,userBean.getEmail(),
				userBean.getPass(),userBean.getFirstName(),userBean.getLastName(),true,true,userBean.getUserTypeId())>0;
	}
	
	public boolean insertUserValuesForUpload(UserBean userBean){
		String insertUserQuery = "INSERT INTO user(isPrimaryUser, phone, regId, userId, isDeleted, email, pass, firstName, lastName, isactive,userTypeId,isFullUser) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		return this.jdbcTemplate.update(insertUserQuery,true,"",userBean.getRegId(),userBean.getUserId(),false,userBean.getEmail(),
				userBean.getPass(),"","",true,userBean.getUserTypeId(),true)>0;
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
	
	public boolean updateUserData(UserBean userBean){
		this.jdbcTemplate.update(UPDATE_USER_DATA,userBean.getFirstName(),userBean.getLastName(),userBean.getImage(),userBean.getGoogleId(),userBean.getPhone() , userBean.getUserId());
		if(userBean.getPass()!=null && !userBean.getPass().trim().isEmpty()){
			this.jdbcTemplate.update(UPDATE_USER_PASS,userBean.getPass(),userBean.getUserId());
		}
		return true;
	}
	
	public boolean updateUserDataForMaster(UserBean userBean){
		this.jdbcTemplate.update(UPDATE_USER_DATA_FOR_MASTER,userBean.getFirstName(),userBean.getLastName(),userBean.getEmail(),userBean.getPass(),userBean.getPhone(),userBean.getUserTypeId(),userBean.getUserId());
		if(userBean.getPass()!=null && !userBean.getPass().trim().isEmpty()){
			this.jdbcTemplate.update(UPDATE_USER_PASS,userBean.getPass(),userBean.getUserId());
		}
		return true;
	}
	
	public boolean updateUserDetails(String firstName,String lastName,String email) {
		return this.jdbcTemplate.update("update user set firstname=?,lastname=? where email=?;",firstName,lastName,email)>-1;		
	}
}
