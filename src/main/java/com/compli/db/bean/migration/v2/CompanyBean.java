package com.compli.db.bean.migration.v2;

import java.util.ArrayList;
import java.util.List;

import com.compli.util.UUID;

public class CompanyBean {
	String companyId;
	String companyName;
	String companyAbbr;
	List<String>companyLocations;
	
	public CompanyBean(String companyId, String companyName, String companyAbbr) {
		this.companyId = null!=companyId?companyId:UUID.getUID();
		this.companyName = companyName;
		this.companyAbbr = companyAbbr;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((companyAbbr == null) ? 0 : companyAbbr.hashCode());
		result = prime * result
				+ ((companyName == null) ? 0 : companyName.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyBean other = (CompanyBean) obj;
		if (companyAbbr == null) {
			if (other.companyAbbr != null)
				return false;
		} else if (!companyAbbr.equals(other.companyAbbr))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		return true;
	}



	public String getCompanyId() {
		return this.companyId;
	}
	public String getCompanyName() {
		return this.companyName;
	}
	public String getCompanyAbbr() {
		return this.companyAbbr;
	}
	public List<String> getCompanyLocations() {
		if(null == this.companyLocations){
			this.companyLocations = new ArrayList<String>();
		}
		return this.companyLocations;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setCompanyAbbr(String companyAbbr) {
		this.companyAbbr = companyAbbr;
	}
	public void setCompanyLocations(List<String> companyLocations) {
		this.companyLocations = companyLocations;
	}
	
	
}
