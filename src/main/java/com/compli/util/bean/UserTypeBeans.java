package com.compli.util.bean;

public class UserTypeBeans {
	
	public UserTypeBeans(UserBean superManager,
	UserBean manager,
	UserBean complianceOwner) {
		this.superManager = superManager;
		this.manager = manager;
		this.complianceOwner = complianceOwner;
	}
	UserBean superManager;
	UserBean manager;
	UserBean complianceOwner;
	
	public UserBean getSuperManager() {
		return superManager;
	}
	public void setSuperManager(UserBean superManager) {
		this.superManager = superManager;
	}
	public UserBean getManager() {
		return manager;
	}
	public void setManager(UserBean manager) {
		this.manager = manager;
	}
	public UserBean getComplianceOwner() {
		return complianceOwner;
	}
	public void setComplianceOwner(UserBean complianceOwner) {
		this.complianceOwner = complianceOwner;
	}
	
	@Override
	public boolean equals(Object obj) {
		return ((UserTypeBeans)obj).getComplianceOwner().equals(this.getComplianceOwner()) &&
				((UserTypeBeans)obj).getManager().equals(this.getManager()) &&
				((UserTypeBeans)obj).getSuperManager().equals(this.getSuperManager());
	}
}
