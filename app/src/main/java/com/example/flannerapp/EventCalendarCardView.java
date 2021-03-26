package com.example.flannerapp;

public class EventCalendarCardView {
  private String eventTime;
  private String eventName;
  private String eventDate;

  public EventCalendarCardView(String eventTime, String eventName, String eventDate) {
    this.eventTime = eventTime;
    this.eventName = eventName;
    this.eventDate = eventDate;
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

}
