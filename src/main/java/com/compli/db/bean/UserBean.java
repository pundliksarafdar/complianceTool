package com.compli.db.bean;

import java.util.Date;
import java.util.UUID;

import com.compli.annotation.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Table(tableName="user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBean {
	String userId;
	String firstName;
	String lastName;
	String email;
	String phone;
	String pass;
	String regId = com.compli.util.UUID.getUID();
	Boolean isactive = false;
	Boolean isDeleted = false;
	String createdBy;
	Date cratedOn;
	String modifiedBy;
	Date modifiedOn;
	Boolean isPrimaryUser;
	String userTypeId;
	Boolean isFullUser;
	String image;
	String googleId;
	int unreadMessage;

	public int getUnreadMessage() {
		return unreadMessage;
	}

	public void setUnreadMessage(int unreadMessage) {
		this.unreadMessage = unreadMessage;
	}

	public String getImage() {
		return image;
	}
	public String getGoogleId() {
		return googleId;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public Boolean isIsactive() {
		return isactive;
	}
	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}
	public Boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCratedOn() {
		return cratedOn;
	}
	public void setCratedOn(Date cratedOn) {
		this.cratedOn = cratedOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public Boolean isPrimaryUser() {
		return isPrimaryUser;
	}
	public void setPrimaryUser(Boolean isPrimaryUser) {
		this.isPrimaryUser = isPrimaryUser;
	}
	public String getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(String userTypeId) {
		this.userTypeId = userTypeId;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public Boolean isFullUser() {
		return isFullUser;
	}
	public void setIsFullUser(Boolean isFullUser) {
		this.isFullUser = isFullUser;
	}
	
	
}
