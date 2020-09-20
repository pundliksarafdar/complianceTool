package com.compli.bean.notification;

import java.util.List;

public class EmailLog {
    private List<EmailLogBean> data;
    private long count;

    public EmailLog(List<EmailLogBean> data, long count){
        this.data = data;
        this.count = count;
    }

    public List<EmailLogBean> getData() {
        return data;
    }

    public void setData(List<EmailLogBean> data) {
        this.data = data;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
