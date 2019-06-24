package com.compli.db.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.compli.db.bean.CompanyBean;
import com.compli.db.bean.CompanyLocationBean;
import com.compli.db.bean.UserBean;
import com.compli.db.bean.UserCompanyBean;
import com.compli.util.DatabaseUtils;

public class CompanyDao {
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<CompanyBean> getAllCompany(){
		List<CompanyBean> companyBeans = (List<CompanyBean>) this.jdbcTemplate.query("select * from company", new BeanPropertyRowMapper(CompanyBean.class));
		return companyBeans;
	}
	
	public boolean addCompany(CompanyBean companyBean){
		/*
		try {
			HashMap<String, List> instval = DatabaseUtils.formInsertStatement(companyBean);
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
			e.printStackTrace();
			return false;
		}
		*/
		
		String sql = "insert into company(companyId,name) values(?,?)";
		this.jdbcTemplate.update(sql,companyBean.getCompanyId(),companyBean.getName());
		return true;
	}
	
	//This function is used for master user to add company
	public boolean addCompanyForMasterUser(CompanyBean companyBean){
		String sql = "insert into company(companyId,name,abbriviation) values(?,?,?)";
		return this.jdbcTemplate.update(sql,companyBean.getCompanyId(),companyBean.getName(),companyBean.getAbbriviation())>0;		
	}
	
	public boolean addCompanyForUpload(CompanyBean companyBean){
		String sql = "insert into company(companyId,name,abbriviation) values(?,?,?)";
		return this.jdbcTemplate.update(sql,companyBean.getCompanyId(),companyBean.getName(),companyBean.getAbbriviation())>0;
		
	}
	
	public boolean setUserCompany(UserCompanyBean userCompanyBean){
		/*try {
			HashMap<String, List> instval = DatabaseUtils.formInsertStatement(userCompanyBean);
			System.out.println(instval);
			int[] dest = new int[instval.get("type").toArray().length];
			int index = 0;
			for(Object objInt:instval.get("type")){
				int objIntI = (Integer)objInt;
				dest[index++] = objIntI;
			}
			this.jdbcTemplate.update(instval.get("query").get(0).toString(), 
					instval.get("value").toArray(), 
					dest);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}*/
		String sql = "insert into usercompany(userId,companyId) values(?,?)";
		this.jdbcTemplate.update(sql,userCompanyBean.getUserId(),userCompanyBean.getCompanyId());
		
		return true;
	}
	
	public boolean setCompanyLocation(CompanyLocationBean companyLocationBean){
		String sql = "insert into companylocation(locationId,companyId) values(?,?)";
		this.jdbcTemplate.update(sql,companyLocationBean.getLocationId(),companyLocationBean.getCompanyId());
		return true;
	}
	
	public List<Map<String, Object>> getAllComapanyAndLocations(){
		return this.jdbcTemplate.queryForList("Select company.companyId,name,abbriviation,locationId from company left join compli.companylocation on company.companyId=companylocation.companyId;");
	}
	
	public CompanyBean getCompanyById(String companyId){
		List<CompanyBean> companyBeans = (List<CompanyBean>) this.jdbcTemplate.query("select * from company where companyId = ?",new Object[]{companyId}, new BeanPropertyRowMapper(CompanyBean.class));
		return companyBeans!=null && companyBeans.size()>0?companyBeans.get(0):null;
	}
}
