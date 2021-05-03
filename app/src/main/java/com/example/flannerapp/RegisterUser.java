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
import com.example.flannerapp.DatabaseUser.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
  private TextView banner, registerUserButton;
  private EditText editTextFullName, editTextAge, editTextEmail;
  private TextInputLayout editTextPassword, editTextConfirmPassword;
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
    editTextConfirmPassword = findViewById(R.id.et_confirmPassword_register);
  }

  private void performButtons() {
    banner.setOnClickListener(this);
    registerUserButton.setOnClickListener(this);
  }

  private void registerUser() {
    final String email = editTextEmail.getText().toString().trim();
    final String age = editTextAge.getText().toString().trim();
    final String fullName = editTextFullName.getText().toString().trim();
    String password = editTextPassword.getEditText().getText().toString().trim();
    String confirmPassword = editTextConfirmPassword.getEditText().getText().toString().trim();
    if (!checkEmptyFullName(fullName) | !checkEmptyAge(age)
      | !checkEmailPatterns(email) | !checkBothPasswordsEmpty(confirmPassword, password)
      | !checkPasswordLength(password) | !checkIfEmpty(confirmPassword)
      | !checkMatching(confirmPassword, password)) {
      return;
    }
    createUserInFirebase(email, age, fullName, password);
  }

  private void createUserInFirebase(final String email, final String age, final String fullName, String password) {
    mAuth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
          if (task.isSuccessful()) {
            User user = new User(fullName, age, email);
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
  private boolean checkPasswordLength(String password) {
    if(password.length() < 6) {
      editTextPassword.setError("Password must be at least 6 characters!");
      editTextPassword.requestFocus();
      return false;
    }
    return true;
  }
  private boolean checkIfEmpty(String confirmpassword){
    if(confirmpassword.isEmpty()){
      editTextConfirmPassword.setError("Cannot be empty!");
      editTextConfirmPassword.requestFocus();
      return false;
    }
    return true;
  }
  private boolean checkBothPasswordsEmpty( String confirmpassword, String password){
    if(password.isEmpty() && confirmpassword.isEmpty()){
      editTextConfirmPassword.setError("Cannot be empty!");
      editTextConfirmPassword.requestFocus();
      editTextPassword.setError("Cannot be empty!");
      editTextPassword.requestFocus();
      return false;
    }
    return true;
  }
private boolean checkMatching(String confirmpassword, String password)
{
  if(!confirmpassword.equals(password)){
    editTextConfirmPassword.setError("Passwords do not match!");
    editTextConfirmPassword.requestFocus();
    return false;
  }
  return true;
}

  private boolean checkEmailPatterns(String email) {
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      editTextEmail.setError("Please provide valid email!");
      editTextEmail.requestFocus();
      return false;
    }
    return true;
  }

  private boolean checkEmptyAge(String age) {
    if (age.isEmpty()) {
      editTextAge.setError("Age is required!");
      editTextAge.requestFocus();
      return false;
    }
    return true;
  }

  private boolean checkEmptyFullName(String fullName) {
    if (fullName.isEmpty()) {
      editTextFullName.setError("Full name is required!");
      editTextFullName.requestFocus();
      return false;
    }
    return true;
  }


}