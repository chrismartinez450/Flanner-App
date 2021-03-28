package com.example.flannerapp;

public class EventCalendarCardView {
  private String eventTime;
  private String eventName;
  private String eventDate;
  private String backgroundCVColor;
  private String docId;


  public EventCalendarCardView(String eventTime, String eventName, String eventDate, String backgroundCVColor, String docId) {
    this.eventTime = eventTime;
    this.eventName = eventName;
    this.eventDate = eventDate;
    this.backgroundCVColor = backgroundCVColor;
    this.docId = docId;
  }

  public String getBackgroundCVColor() {
    return backgroundCVColor;
  }

  public void setBackgroundCVColor(String backgroundCVColor) {
    this.backgroundCVColor = backgroundCVColor;
  }

  public String getEventTime() {
    return eventTime;
  }

  public void setEventTime(String eventTime) {
    this.eventTime = eventTime;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getEventDate() {
    return eventDate;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }

  public String getDocId() {
    return docId;
  }

  public void setDocId(String docId) {
    this.docId = docId;
  }
}
