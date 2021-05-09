package com.flanner.flannerapp;

public class CustomEvents {
  String event, time, date, cardViewColor, docId;

  public CustomEvents() {
  }

  public CustomEvents(String event, String time, String date, String cardViewColor, String docId) {
    this.event = event;
    this.time = time;
    this.date = date;
    this.cardViewColor = cardViewColor;
    this.docId = docId;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getCardViewColor() {
    return cardViewColor;
  }

  public void setCardViewColor(String cardViewColor) {
    this.cardViewColor = cardViewColor;
  }

  public String getDocId() {
    return docId;
  }

  public void setDocId(String docId) {
    this.docId = docId;
  }
}
