package com.compli.bean.dashboard;

import java.util.List;

public class DashboardMonthlyOverviewObject {
    List<String> months ;
    List<DashboardMonthlyOverview> dashboardMonthlyOverviewList;

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }

    public List<DashboardMonthlyOverview> getDashboardMonthlyOverviewList() {
        return dashboardMonthlyOverviewList;
    }

    public void setDashboardMonthlyOverviewList(List<DashboardMonthlyOverview> dashboardMonthlyOverviewList) {
        this.dashboardMonthlyOverviewList = dashboardMonthlyOverviewList;
    }
}
