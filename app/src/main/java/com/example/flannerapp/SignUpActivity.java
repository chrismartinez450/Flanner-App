package com.example.flannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
  private static final Pattern PASSWORD_PATTERN =
    Pattern.compile("^" +
      //"(?=.*[0-9])" +       //at least 1 digit
      //"(?=.*[a-z])" +       //at least 1 lower case letter
      //"(?=.*[A-Z])" +       //at least 1 upper case letter
      "(?=.*[a-zA-Z])" +      //any letter
      //"(?=.*[@#$%^&+=])" +  //at least 1 special character
      "(?=\\S+$)" +           //no white spaces
      ".{4,}" +               //at least 6 characters
      "$");
  private TextInputLayout emailTextInput;
  private TextInputLayout fullNameTextInput;
  private TextInputLayout passwordTextInput;
  private TextInputLayout ageTextInput;
  private TextInputLayout goalTextInput;
  private Button confirmButton;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);
    initializeLayout();
    confirmButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!validateEmail()  | !validateName() | !validatePassword() | !validateAge() | !validateGoal()) {
          return;
        }
        Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
      }
    });
  }

  public void initializeLayout() {
    emailTextInput = findViewById(R.id.emailTextInput);
    fullNameTextInput = findViewById(R.id.fullNameTextInput);
    passwordTextInput = findViewById(R.id.passwordTextInput);
    ageTextInput = findViewById(R.id.ageTextInput);
    goalTextInput = findViewById(R.id.goalTextInput);
    confirmButton = findViewById(R.id.confirmButton);
  }

  public boolean validateEmail() {
    String emailInput = emailTextInput.getEditText().getText().toString().trim();
    if (emailInput.isEmpty()) {
      emailTextInput.setError("Field can't be empty");
      return false;
    } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
      emailTextInput.setError("Please enter a valid email address");
      return false;
    } else {
      emailTextInput.setError(null);
      return true;
    }
  }

  public boolean validateName() {
    String fullNameInput = fullNameTextInput.getEditText().getText().toString().trim();
    if (fullNameInput.isEmpty()) {
      fullNameTextInput.setError("Field can't be empty");
      return false;
    } else {
      fullNameTextInput.setError(null);
      return true;
    }
  }

  public boolean validatePassword() {
    String passwordInput = passwordTextInput.getEditText().getText().toString().trim();
    if (passwordInput.isEmpty()) {
      passwordTextInput.setError("Field can't be empty");
      return false;
    } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
      passwordTextInput.setError("Password must contain at least one alphabet letter and at least 6 characters");
      return false;
    } else if (passwordInput.length() > 20) {
      passwordTextInput.setError("Password cannot be more than 20 characters");
      return false;
    } else {
      passwordTextInput.setError(null);
      return true;
    }
  }

  public boolean validateAge() {
    String ageInput = ageTextInput.getEditText().getText().toString().trim();
    if (ageInput.isEmpty()) {
      ageTextInput.setError("Field can't be empty");
      return false;
    } else if (Integer.parseInt(ageInput) < 0) {
      ageTextInput.setError("The age cannot be less than zero");
      return false;
    } else {
      ageTextInput.setError(null);
      return true;
    }
  }

  public boolean validateGoal() {
    String goalInput = goalTextInput.getEditText().getText().toString().trim();
    if (goalInput.isEmpty()) {
      goalTextInput.setError("Field can't be empty");
      return false;
    } else {
      goalTextInput.setError(null);
      return true;
    }
  }
}
