package com.example.flannerapp;

import java.util.Date;

public class UserTimer {
    private String date;
    private String chronoTime;
    private String subject;
    private long timeInMilli;


    public UserTimer(String date, String chronoTime, String subject, long timeInMilli) {
        this.date = date;
        this.chronoTime = chronoTime;
        this.subject = subject;
        this.timeInMilli = timeInMilli;

    }

    public UserTimer() {
        // no arg constructor.
    }

    public long getTimeInMilli() {
        return timeInMilli;
    }

    public void setTimeInMilli(long timeInMilli) {
        this.timeInMilli = timeInMilli;
    }

    public String getDay() {
        String[] day = new String[3];
        day = date.split("-");

        return day[2];
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

    public int convertChronoTimeToHours() {
        int result = 0;
        String tempString[] = this.getChronoTime().split(":");

        if (tempString.length == 3) result = Integer.parseInt(tempString[0]);

        return result;
    }
}
