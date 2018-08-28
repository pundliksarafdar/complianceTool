package com.compli.db.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.Files;
import com.compli.db.bean.UserBean;

public class FilesDao {
	private JdbcTemplate jdbcTemplate;
	private String filesForUser = "select * from files where activityId=? and companyId=?"; 
	private String savefilesForUser = "INSERT INTO files(activityId,companyId,fileId,filename)VALUES(?,?,?,?);"; 
	private String fileById = "select * from files where  fileId=?"; 
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> getFilesForActivity(String activityId, String companyId) {
		List<Map<String, Object>> files = this.jdbcTemplate.queryForList(filesForUser,activityId,companyId);
		return files;
	}

	public List<Files> getFileById(String fileId) {
		List<Files> files = (List<Files>) this.jdbcTemplate.query(fileById,new Object[]{fileId}, new BeanPropertyRowMapper(Files.class));
		return files;
	}

	public boolean saveFile(String activityId,String companyId,String fileId,String filename) {
		return this.jdbcTemplate.update(savefilesForUser,new Object[]{activityId,companyId,fileId,filename})>0;		
	}
	
}
