package com.compli.util.bean;

public class UserBean {
	String email;
	String username;
	String password = "Passwd@123";
	
	String firstName="";
	String lastName="";
	String phone = "";
	 
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Override
	public boolean equals(Object obj) {
		return ((UserBean)obj).getUsername().equals(this.getUsername()) &&
				((UserBean)obj).getEmail().equals(this.getEmail()) &&
				((UserBean)obj).getPassword().equals(this.getPassword());
	}
}

