package com.example.flannerapp.DatabaseUser;

public class Events {
  String EVENT, TIME, DATE, MONTH, YEAR, CARDVIEWCOLOR;

  public Events() {
  }

  public Events(String EVENT, String TIME, String DATE, String MONTH, String YEAR, String CARDVIEWCOLOR) {
    this.EVENT = EVENT;
    this.TIME = TIME;
    this.DATE = DATE;
    this.MONTH = MONTH;
    this.YEAR = YEAR;
    this.CARDVIEWCOLOR = CARDVIEWCOLOR;
  }

  public String getCARDVIEWCOLOR() {
    return CARDVIEWCOLOR;
  }

  public void setCARDVIEWCOLOR(String CARDVIEWCOLOR) {
    this.CARDVIEWCOLOR = CARDVIEWCOLOR;
  }

  public String getEVENT() {
    return EVENT;
  }

  public void setEVENT(String EVENT) {
    this.EVENT = EVENT;
  }

  public String getTIME() {
    return TIME;
  }

  public void setTIME(String TIME) {
    this.TIME = TIME;
  }

  public String getDATE() {
    return DATE;
  }

  public void setDATE(String DATE) {
    this.DATE = DATE;
  }

  public String getMONTH() {
    return MONTH;
  }

  public void setMONTH(String MONTH) {
    this.MONTH = MONTH;
  }

  public String getYEAR() {
    return YEAR;
  }

  public void setYEAR(String YEAR) {
    this.YEAR = YEAR;
  }
}
