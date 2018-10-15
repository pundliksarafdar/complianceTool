package com.compli.db.bean.migration.v2;

public class UserBean {
	String username;
	String password = "Passwd@123";
	String firstname;
	String lastname;
	String email;
	boolean active = true;
	boolean isFullUser = true;
	String userType;
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getEmail().trim().equals(((UserBean)obj).getEmail().trim());
	}
	
	@Override
	public int hashCode() {
		return this.getEmail().hashCode();
	}
	@Override
	public String toString() {
		return "UserBean [username=" + username + ", password=" + password
				+ ", firstname=" + firstname + ", lastname=" + lastname
				+ ", email=" + email + ", active=" + active + ", isFullUser="
				+ isFullUser + ", userType=" + userType + "]";
	}
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public String getEmail() {
		return email;
	}
	public boolean isActive() {
		return active;
	}
	public boolean isFullUser() {
		return isFullUser;
	}
	public String getUserType() {
		return userType;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public void setFullUser(boolean isFullUser) {
		this.isFullUser = isFullUser;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
}
