package com.example.flannerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = "TAG";
    private Button saveEditButton, changeEmailButton;
    EditText editUsername, editEmail, editName, editAge;
    private String userID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        saveEditButton = findViewById(R.id.save_button);
        changeEmailButton = findViewById(R.id.change_email_button);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        editUsername = findViewById(R.id.username_edit);
        editEmail = findViewById(R.id.email_edit);
        editName = findViewById(R.id.fullname_edit);
        editAge = findViewById(R.id.age_edit);

        docRef = db.collection("Users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String username = task.getResult().getString("username");
                    String email = task.getResult().getString("email");
                    String fullName = task.getResult().getString("fullName");
                    String age = task.getResult().getString("age");

                    //fills in info from firestore
                    editUsername.setText(username);
                    editEmail.setText(email);
                    editName.setText(fullName);
                    editAge.setText(age);
                }else{
                    Toast.makeText(EditProfile.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editUsername.getText().toString().isEmpty() || editName.getText().toString().isEmpty() || editAge.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "One or more fields is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateProfile();
            }
        });

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editEmail.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "Email field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateEmail();
            }
        });
    }

    private void updateEmail(){
        final String newEmail = editEmail.getText().toString();
        final DocumentReference sDoc = db.collection("Users").document(userID);

        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(sDoc,"email", newEmail);
                return null;
            }
        });

        //authenticates new user email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    //when email has been authenticated, verification email will be sent
                    //info will be updated to firestore
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.sendEmailVerification();
                    Toast.makeText(EditProfile.this, "Email has been updated. Check email to verify.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateProfile() {
        final String newUsername = editUsername.getText().toString();
        final String newName = editName.getText().toString();
        final String newAge = editAge.getText().toString();

        final DocumentReference sDoc = db.collection("Users").document(userID);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                transaction.update(sDoc, "username", newUsername);
                transaction.update(sDoc, "fullName", newName);
                transaction.update(sDoc, "age", newAge);
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditProfile.this, "Profile has been updated", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Unable to update", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}