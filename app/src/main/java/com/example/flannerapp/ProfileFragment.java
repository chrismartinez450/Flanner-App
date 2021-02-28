package com.example.flannerapp;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_profiles, container, false);
    logOutButton = root.findViewById(R.id.btn_signOut_profile);

    logOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
      }
    });

    user = FirebaseAuth.getInstance().getCurrentUser();
    userID = user.getUid();
    docRef = FirebaseFirestore.getInstance().collection("Users").document(userID);

    final TextView greetingTextView = root.findViewById(R.id.greeting);
    final TextView fullNameTextView = root.findViewById(R.id.tv_fullName_profile);
    final TextView emailTextView = root.findViewById(R.id.tv_emailAddress_profile);
    final TextView ageTextView = root.findViewById(R.id.tv_age_profile);

    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
      @Override
      public void onSuccess(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot.exists()) {
          String fullName = documentSnapshot.getString("fullName");
          String age = documentSnapshot.getString("age");
          String email = documentSnapshot.getString("email");
          greetingTextView.setTextColor(Color.WHITE);
          fullNameTextView.setTextColor(Color.WHITE);
          emailTextView.setTextColor(Color.WHITE);
          ageTextView.setTextColor(Color.WHITE);

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
