package com.flanner.flannerapp.DatabaseUser;

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

    public float convertChronoTime() {
        float result = 0;
        String tempString[] = this.getChronoTime().split(":");

        if (tempString.length == 3) {
            result = Float.parseFloat(tempString[0]);
            result += (Float.parseFloat(tempString[1]) / 60);
            result += (Float.parseFloat(tempString[2]) / 3600);
        } else if (tempString.length == 2) {
            result = (Float.parseFloat(tempString[0]) / 60);
            result += (Float.parseFloat(tempString[1]) / 3600);
        }

        return result;
    }
}
