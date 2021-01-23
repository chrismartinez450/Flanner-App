package com.example.flannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


  private TextView registerTextView, forgotPasswordTextView;
  private EditText editTextEmail, editTextPassword;
  private Button signInButton;

  private FirebaseAuth mAuth;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    registerTextView = findViewById(R.id.tv_register_main);
    registerTextView.setOnClickListener(this);

    signInButton = findViewById(R.id.btn_signIn_main);
    signInButton.setOnClickListener(this);

    editTextEmail = findViewById(R.id.et_email_main);
    editTextPassword = findViewById(R.id.et_password_main);
    mAuth = FirebaseAuth.getInstance();

    forgotPasswordTextView = findViewById(R.id.tv_forgotPassword_main);
    forgotPasswordTextView.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.tv_register_main:
        startActivity(new Intent(this, RegisterUser.class));
        break;
      case R.id.btn_signIn_main:
        userLogin();
        break;
      case R.id.tv_forgotPassword_main:
        startActivity(new Intent(this, ForgotPassword.class));
        break;
    }
  }

  private void userLogin() {
    String email = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();

    if (email.isEmpty()) {
      editTextEmail.setError("Email is required!");
      editTextEmail.requestFocus();
    }

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      editTextEmail.setError("Please provide valid email!");
      editTextEmail.requestFocus();
    }

    if (password.isEmpty()) {
      editTextPassword.setError("Password is required!");
      editTextPassword.requestFocus();
    }

    if (password.length() < 6) {
      editTextPassword.setError("Password must be at least 6 characters!");
      editTextPassword.requestFocus();
    }


    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          //redirect to user profile
          FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
          assert user != null;
          if (user.isEmailVerified()) {
            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
          } else {
            user.sendEmailVerification();
            Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
          }

        } else {
          Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
        }
      }
    });

  }
}