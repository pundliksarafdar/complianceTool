package com.compli.bean.notification;

import java.util.Date;

public class EmailLogBean {
    private String email;
    private String subject;
    private String content;
    private Date creationDate;

    public EmailLogBean(){

    }

    public EmailLogBean(String email, String subject, String content){
        this.email = email;
        this.subject = subject;
        this.content = content;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
