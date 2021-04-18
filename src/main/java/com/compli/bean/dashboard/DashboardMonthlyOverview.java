package com.compli.bean.dashboard;

import java.util.List;

public class DashboardMonthlyOverview {
    String label;
    List<Integer> data;

    public DashboardMonthlyOverview(String label,List<Integer> data){
        this.label = label;
        this.data = data;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
