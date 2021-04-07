package com.example.flannerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
  private Button logOutButton;
  private FirebaseUser user;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private DocumentReference docRef;
  private String userID;

  private Button editProfileButton;
  private Button deleteProfileButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_profiles, container, false);
    logOutButton = root.findViewById(R.id.btn_signOut_profile);
    editProfileButton = root.findViewById(R.id.editButton);
    deleteProfileButton = root.findViewById(R.id.deleteProfileButton);

    logOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
      }
    });

    editProfileButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), EditProfile.class));
      }
    });

    //deletes account on firebase
    deleteProfileButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Deleting your account will remove all your information, and you will no longer be able to login to the app with this account. Do you wish to continue?");
        dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                  Toast.makeText(getActivity(), "Account has been deleted.", Toast.LENGTH_LONG).show();
                  FirebaseAuth.getInstance().signOut();
                  startActivity(new Intent(getActivity(), MainActivity.class));
                }else{
                  Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
              }
            });
          }
        });
        dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
      }
    });

    user = FirebaseAuth.getInstance().getCurrentUser();
    userID = user.getUid();
    docRef = FirebaseFirestore.getInstance().collection("Users").document(userID);

    final TextView greetingTextView = root.findViewById(R.id.greeting);
    final TextView fullNameTextView = root.findViewById(R.id.tv_fullName_profile);
    final TextView emailTextView = root.findViewById(R.id.tv_emailAddress_profile);
    final TextView ageTextView = root.findViewById(R.id.tv_age_profile);
    final TextView usernameTextView = root.findViewById(R.id.tv_username_profile);

    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
      @Override
      public void onSuccess(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
          String fullName = documentSnapshot.getString("fullName");
          String age = documentSnapshot.getString("age");
          String email = documentSnapshot.getString("email");
          String username = documentSnapshot.getString("username");

          greetingTextView.setTextColor(Color.WHITE);
          fullNameTextView.setTextColor(Color.WHITE);
          emailTextView.setTextColor(Color.WHITE);
          ageTextView.setTextColor(Color.WHITE);
          usernameTextView.setTextColor(Color.WHITE);

          usernameTextView.setText(username);
          fullNameTextView.setText(fullName);
          emailTextView.setText(email);
          ageTextView.setText(age);
          greetingTextView.setText("Hello " + fullName);

        } else {
          Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_LONG).show();
        }
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {

      }
    });

    return root;
  }

}
