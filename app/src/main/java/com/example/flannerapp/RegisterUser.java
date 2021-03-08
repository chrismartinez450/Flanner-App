package com.example.flannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
  private TextView banner, registerUserButton;
  private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword, editTextUsername;
  private FirebaseAuth mAuth;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register_user);
    initializeAttributes();
    performButtons();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.banner:
        startActivity(new Intent(this, MainActivity.class));
        break;
      case R.id.btn_registerUser_register:
        registerUser();
        break;
    }
  }

  private void initializeAttributes() {
    mAuth = FirebaseAuth.getInstance();
    banner = findViewById(R.id.banner);
    registerUserButton = findViewById(R.id.btn_registerUser_register);
    editTextFullName = findViewById(R.id.et_fullName_register);
    editTextAge = findViewById(R.id.et_age_register);
    editTextEmail = findViewById(R.id.et_email_register);
    editTextPassword = findViewById(R.id.et_password_register);
    editTextUsername = findViewById(R.id.et_username_register);
  }

  private void performButtons() {
    banner.setOnClickListener(this);
    registerUserButton.setOnClickListener(this);
  }

  private void registerUser() {
    final String email = editTextEmail.getText().toString().trim();
    final String age = editTextAge.getText().toString().trim();
    final String fullName = editTextFullName.getText().toString().trim();
    final String username = editTextUsername.getText().toString().trim();
    String password = editTextPassword.getText().toString().trim();
    checkValidAttributes(email, age, fullName, password, username);
    createUserInFirebase(email, age, fullName, password, username);
  }

  private void createUserInFirebase(final String email, final String age, final String fullName, String password, final String username) {
    mAuth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            User user = new User(fullName, age, email, username);
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
              .set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                sendEmailVerification();
                startActivity(new Intent(RegisterUser.this, MainActivity.class));
              }
            })
              .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                  Toast.makeText(RegisterUser.this, "Fail!", Toast.LENGTH_LONG).show();
                }
              });
          } else {
            Toast.makeText(RegisterUser.this, "Fail!", Toast.LENGTH_LONG).show();
          }
        }
      });
  }

  private void sendEmailVerification() {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    assert user != null;
    user.sendEmailVerification();
    Toast.makeText(RegisterUser.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
  }

  private void checkValidAttributes(String email, String age, String fullName, String password, String username) {
    checkEmptyFullName(fullName);
    checkEmptyAge(age);
    checkEmptyPassword(password);
    checkEmailPatterns(email);
    checkPasswordLength(password);
    checkUsernameLength(username);
  }

  private void checkUsernameLength(String username) {
    if(username.length() < 6)
    {
      editTextUsername.setError("Username must be at least 6 characters long!");
      editTextUsername.requestFocus();
    }
  }

  private void checkPasswordLength(String password) {
    if (password.length() < 6) {
      editTextPassword.setError("Password must be at least 6 characters!");
      editTextPassword.requestFocus();
    }
  }

  private void checkEmailPatterns(String email) {
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      editTextEmail.setError("Please provide valid email!");
      editTextEmail.requestFocus();
    }
  }

  private void checkEmptyPassword(String password) {
    if (password.isEmpty()) {
      editTextPassword.setError("Password is required!");
      editTextPassword.requestFocus();
    }
  }

  private void checkEmptyAge(String age) {
    if (age.isEmpty()) {
      editTextAge.setError("Age is required!");
      editTextAge.requestFocus();
    }
  }

  private void checkEmptyFullName(String fullName) {
    if (fullName.isEmpty()) {
      editTextFullName.setError("Full name is required!");
      editTextFullName.requestFocus();
    }
  }


}