package com.compli.util.bean;

import java.util.ArrayList;
import java.util.List;

import com.compli.util.UUID;

public class CompanyBean {
	String companyName;
	String companyAbbr;
	String companyId = UUID.getUID();
	List<String> userName = new ArrayList<String>();
	
	public String getCompanyName() {
		return companyName;
	}
	public String getCompanyAbbr() {
		return companyAbbr;
	}
	public String getCompanyId() {
		return companyId;
	}
	public List<String> getUserName() {
		return userName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setCompanyAbbr(String companyAbbr) {
		this.companyAbbr = companyAbbr;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public void setUserName(List<String> userName) {
		this.userName = userName;
	}
	
	@Override
	public boolean equals(Object obj) {
		CompanyBean companyBean = (CompanyBean)obj;
		return companyBean.getCompanyName().equals(this.companyName) 
				&& companyBean.getCompanyAbbr().equals(this.companyAbbr);
	}
}
