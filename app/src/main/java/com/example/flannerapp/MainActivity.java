package com.example.flannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flannerapp.DatabaseUser.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private TextView registerTextView, forgotPasswordTextView;
  private EditText editTextEmail;
  private TextInputLayout editTextPassword;
  private Button signInButton;
  private FirebaseAuth mAuth;
  private SignInButton googleSignInButton;
  private GoogleSignInClient mGoogleSignInClient;
  private String TAG = "MainActivity";
  private int RC_SIGN_IN = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initializeAttributes();
    performButtons();
  }

  @Override
  public void onClick(View v) {
    switch(v.getId()) {
      case R.id.google_sign_in:
        googleLogin();
        break;
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

  private void initializeAttributes() {
    registerTextView = findViewById(R.id.tv_register_main);
    signInButton = findViewById(R.id.btn_signIn_main);
    editTextEmail = findViewById(R.id.et_email_main);
    editTextPassword = findViewById(R.id.et_password_main);
    mAuth = FirebaseAuth.getInstance();
    forgotPasswordTextView = findViewById(R.id.tv_forgotPassword_main);

    /**Google Sign in**/
    googleSignInButton = findViewById(R.id.google_sign_in);
    googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

  }

  private void performButtons() {
    registerTextView.setOnClickListener(this);
    signInButton.setOnClickListener(this);
    forgotPasswordTextView.setOnClickListener(this);
    googleSignInButton.setOnClickListener(this);
  }

  private void userLogin() {
    String email = editTextEmail.getText().toString().trim();
    String password = editTextPassword.getEditText().getText().toString().trim();
    if (checkInvalidEmailAndPassword(email, password)) return;
    authenticationFirebase(email, password);
  }

  private void googleLogin() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    if (requestCode == RC_SIGN_IN) {
      // The Task returned from this call is always completed, no need to attach
      // a listener.
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = task.getResult(ApiException.class);
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        firebaseAuthWithGoogle(account.getIdToken());
      } catch (ApiException e) {
        // Google Sign In failed, update UI appropriately
        Log.w(TAG, "Google sign in failed", e);
      }
    }
  }

  private void firebaseAuthWithGoogle(String idToken) {
    AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                  // Sign in success, update UI with the signed-in user's information
                  Log.d(TAG, "signInWithCredential:success");
                  FirebaseUser user = mAuth.getCurrentUser();
                  User newUser = new User(user.getDisplayName(), "0", user.getEmail());
                  FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).set(newUser);
                  startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                  startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                } else {
                  // If sign in fails, display a message to the user.
                  Log.w(TAG, "signInWithCredential:failure", task.getException());
                  Toast.makeText(MainActivity.this, "Authentication failed.",
                          Toast.LENGTH_SHORT).show();
                }
              }
            });
  }

  private boolean checkInvalidEmailAndPassword(String email, String password) {
    if (!checkEmptyEmail(email) | !checkEmailPatterns(email) | !checkEmptyPassword(password) | !checkPasswordLength(password)) {
      Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
      return true;
    }
    return false;
  }

  private void authenticationFirebase(String email, String password) {
    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
      @Override
      public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
          FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
          assert user != null;
          if (user.isEmailVerified()) {
            startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
          } else {
            Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
          }
        } else {
          Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
        }
      }
    });
  }

  private boolean checkPasswordLength(String password) {
    if (password.length() < 6) {
      editTextPassword.setError("Password must be at least 6 characters!");
      return false;
    }
    return true;
  }

  private boolean checkEmptyPassword(String password) {
    if (password.isEmpty()) {
      editTextPassword.setError("Password is required!");
      return false;
    }
    return true;
  }

  private boolean checkEmptyEmail(String email) {
    if (email.isEmpty()) {
      editTextEmail.setError("Email is required!");
      return false;
    }
    return true;
  }

  private boolean checkEmailPatterns(String email) {
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
      editTextEmail.setError("Please provide valid email!");
      return false;
    }
    return true;
  }
}