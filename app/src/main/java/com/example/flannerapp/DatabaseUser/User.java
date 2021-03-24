package com.example.flannerapp.DatabaseUser;

public class User {
  public String fullName, age, email, username;

  public User() {
  }

  public User(String fullName, String age, String email, String username) {
    this.fullName = fullName;
    this.age = age;
    this.email = email;
    this.username = username;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
