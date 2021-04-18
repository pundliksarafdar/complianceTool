package com.compli.bean.registration;

import com.compli.util.UUID;
import com.compli.util.Util;

public class GoogleRegistrationBean {
    String firstname;
    String lastname;
    String imageUrl;
    String email;
    String id;
    String userId;
    public GoogleRegistrationBean(){

    }

    public GoogleRegistrationBean(String firstname,String lastname,String imageUrl,String email,String id){
        this.firstname = firstname;
        this.lastname = lastname;
        this.imageUrl = imageUrl;
        this.email = email;
        this.id = id;
    }

    public String getUserId(){
        return UUID.getUID();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

