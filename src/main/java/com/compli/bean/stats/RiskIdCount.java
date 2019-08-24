package com.compli.bean.stats;

import java.io.Serializable;

public class RiskIdCount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3995056089434846272L;
	public String riskId;
	public int count;
	
	public String getRiskId() {
		return riskId;
	}
	public int getCount() {
		return count;
	}
	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
