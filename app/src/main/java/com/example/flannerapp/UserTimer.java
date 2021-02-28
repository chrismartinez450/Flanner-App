package com.example.flannerapp;

import java.util.Date;

public class UserTimer {
    private String date;
    private String chronoTime;
    private String subject;


    public UserTimer(String date, String chronoTime, String subject) {
        this.date = date;
        this.chronoTime = chronoTime;
        this.subject = subject;

    }

    public UserTimer(){
        // no arg constructor.
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChronoTime() {
        return chronoTime;
    }

    public void setChronoTime(String chronoTime) {
        this.chronoTime = chronoTime;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
