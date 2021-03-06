package com.compli.db.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.Files;
import com.compli.db.bean.UserBean;

public class FilesDao {
	private JdbcTemplate jdbcTemplate;
	//in this query delete=false and null are same
	private String filesForUser = "select * from files where activityId=? and companyId=? and (isDeleted=false or isNull(isDeleted))"; 
	private String savefilesForUser = "INSERT INTO files(activityId,companyId,fileId,filename,createdOn,isDeleted)VALUES(?,?,?,?,?,false);";
	private String updateFileIdForUser = "UPDATE files SET fileId=? WHERE filename=?;";
	private String markFileAsDeleted = "UPDATE files SET isDeleted=true WHERE fileId=?;";
	private String fileById = "select * from files where  fileId=? and (isDeleted=false or isNull(isDeleted))"; 
	
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
	
	public boolean markFileAsDeleted(String fileId){
		return this.jdbcTemplate.update(markFileAsDeleted,new Object[]{fileId})>0;
	}

	public boolean saveFile(String activityId,String companyId,String fileId,String filename,Date createdOn) {
		return this.jdbcTemplate.update(savefilesForUser,new Object[]{activityId,companyId,fileId,filename,createdOn})>0;		
	}
	
	public boolean updateFileId(String fileId,String filename) {
		return this.jdbcTemplate.update(updateFileIdForUser,new Object[]{fileId,filename})>0;		
	}
	
	public List<Files> getAlFiles() {
		List<Files> files = (List<Files>) this.jdbcTemplate.query("select * from files", new BeanPropertyRowMapper(Files.class));
		return files;
	}
}
