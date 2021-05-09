package com.flanner.flannerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeAttributes();
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();

            }
        });
    }

    private void initializeAttributes() {
        emailEditText = findViewById(R.id.et_email_forgot);
        resetPasswordButton = findViewById(R.id.btn_resetPassword_forgot);
        auth = FirebaseAuth.getInstance();
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();
        if (!checkEmptyEmail(email) || !checkInvalidEmail(email)) {
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
                Toast.makeText(ForgotPassword.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
            } else {
                emailEditText.setError("Try again! Please check your email!");
            }
            }
        });
    }

    private boolean checkInvalidEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please provide valid email!");
            emailEditText.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkEmptyEmail(String email) {
        if (email.isEmpty() || email == null) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return false;
        }
        return true;
    }
}