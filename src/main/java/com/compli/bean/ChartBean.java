package com.compli.bean;

public class ChartBean {
	String category;
	String series;
	int value;
	
	
	public ChartBean(String category, String series, int value) {
		this.category = category;
		this.series = series;
		this.value = value;
	}
	
	public String getCategory() {
		return category;
	}
	public String getSeries() {
		return series;
	}
	public int getValue() {
		return value;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
}
