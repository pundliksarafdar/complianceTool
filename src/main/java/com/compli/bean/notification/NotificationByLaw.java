package com.compli.bean.notification;

import java.util.Date;
import java.util.List;

public class NotificationByLaw {
    private String title;
    private String notification;
    private String type;
    private boolean allLocation;
    private List<String> locations;
    private String userType;
    private String expiry;
    private Date creationDate;
    private Boolean read;
    private String severity;
    private boolean sendEmail;
    private String lawArea;

    public String getLawArea() {
        return lawArea;
    }

    public void setLawArea(String lawArea) {
        this.lawArea = lawArea;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public boolean isAllLocation() {
        return allLocation;
    }

    public void setAllLocation(boolean allLocation) {
        this.allLocation = allLocation;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }
}
