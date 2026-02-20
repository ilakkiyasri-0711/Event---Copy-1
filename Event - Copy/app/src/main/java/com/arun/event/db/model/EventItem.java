package com.arun.event.db.model;

import java.util.Date;

public class EventItem {

    int count;
    Date date;

    public EventItem(int count, Date date) {
        this.count = count;
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
