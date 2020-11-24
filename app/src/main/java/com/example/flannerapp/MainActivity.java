package com.example.flannerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

  private Button signUpButton;
  private Button signInButton;
  private TextInputLayout emailMain;
  private TextInputLayout passwordMain;

  private String emailInput;
  private String passwordInput;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initializeLayout();

    signUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
      }
    });

    signInButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!validateEmptyEmail() | !validateEmptyPassword()) {
          Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
          return;
        }
        Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
        startActivity(intent);
      }
    });

  }

  private void initializeLayout() {
    emailMain = findViewById(R.id.emailMain);
    passwordMain = findViewById(R.id.passwordMain);
    signInButton = findViewById(R.id.SignInButton);
    signUpButton = findViewById(R.id.SignUpButton);
  }

  private boolean validateEmptyEmail() {
    emailInput = emailMain.getEditText().getText().toString().trim();
    if (emailInput.isEmpty()) {
      emailMain.setError("Email is required");
      return false;
    } else {
      emailMain.setError(null);
    }
    return true;
  }

  private boolean validateEmptyPassword() {
    passwordInput = passwordMain.getEditText().getText().toString().trim();
    if (passwordInput.isEmpty()) {
      passwordMain.setError("Password is required");
      return false;
    } else {
      passwordMain.setError(null);
    }
    return true;
  }


}