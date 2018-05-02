package com.compli.bean;

import com.compli.db.bean.UserBean;

public class RegisterBean {
	UserBean userBean;
	CompanyBean companyBean;
	
	public UserBean getUserBean() {
		return userBean;
	}
	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}
	public CompanyBean getCompanyBean() {
		return companyBean;
	}
	public void setCompanyBean(CompanyBean companyBean) {
		this.companyBean = companyBean;
	}
	
	
}
