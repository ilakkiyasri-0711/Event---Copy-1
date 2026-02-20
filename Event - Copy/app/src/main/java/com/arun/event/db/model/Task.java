package com.arun.event.db.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "task")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public int dept;
    public int session;
    public String staffName;
    public String mobileNumber;
    public String notes;
    public Date date;

    @Ignore
    public Task() {
    }

    public Task(String title, int dept, int session, String staffName, String mobileNumber, String notes, Date date) {
        this.title = title;
        this.dept = dept;
        this.session = session;
        this.staffName = staffName;
        this.mobileNumber = mobileNumber;
        this.notes = notes;
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDept() {
        return dept;
    }

    public void setDept(int dept) {
        this.dept = dept;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
