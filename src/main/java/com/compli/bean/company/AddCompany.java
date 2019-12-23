package com.compli.bean.company;

import com.compli.util.UUID;

import java.util.List;

public class AddCompany {
    public String id = UUID.getUID();
    public String companyname;
    public String abbr;
    public String headQuarterLocation;
    public List<String> branchLocation;
    public String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getHeadQuarterLocation() {
        return headQuarterLocation;
    }

    public void setHeadQuarterLocation(String headQuarterLocation) {
        this.headQuarterLocation = headQuarterLocation;
    }

    public List<String> getBranchLocation() {
        return branchLocation;
    }

    public void setBranchLocation(List<String> branchLocation) {
        this.branchLocation = branchLocation;
    }
}
