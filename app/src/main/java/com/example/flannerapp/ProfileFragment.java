package com.example.flannerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileFragment extends Fragment {
  private Button logOutButton;
  private FirebaseUser user;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private DocumentReference docRef;
  private String userID;

  private Button editProfileButton, deleteProfileButton, switchModes;
  private TextView greetingTextView;
  private TextView fullNameTextView;
  private TextView emailTextView;
  private TextView ageTextView;
  private GoogleSignInClient mGoogleSignInClient;

  private SharedPreferences sharedPreferences = null;
  private boolean isDarkModeOn;
  private Callback callback;

  public interface Callback{
    void onButtonClicked();
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_profiles, container, false);
    logOutButton = root.findViewById(R.id.btn_signOut_profile);
    editProfileButton = root.findViewById(R.id.editButton);
    deleteProfileButton = root.findViewById(R.id.deleteProfileButton);
    switchModes = root.findViewById(R.id.switch_mode);
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

    logOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mGoogleSignInClient.signOut();
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

    //toggles between light and dark mode
    sharedPreferences = this.getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
    final SharedPreferences.Editor editor = sharedPreferences.edit();
    isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

    if(isDarkModeOn){
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
      switchModes.setText("Disable Dark Mode");
    }else{
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
      switchModes.setText("Enable Dark Mode");
    }

    switchModes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(isDarkModeOn){
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
          editor.putBoolean("isDarkModeOn", false);
          editor.apply();
          switchModes.setText("Enable Dark Mode");
        }else{
          AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
          editor.putBoolean("isDarkModeOn", true);
          editor.apply();
          switchModes.setText("Disable Dark Mode");
        }
        callback.onButtonClicked();
      }
    });

    user = FirebaseAuth.getInstance().getCurrentUser();
    userID = user.getUid();
    docRef = FirebaseFirestore.getInstance().collection("Users").document(userID);

    greetingTextView = root.findViewById(R.id.greeting);
    fullNameTextView = root.findViewById(R.id.tv_fullName_profile);
    emailTextView = root.findViewById(R.id.tv_emailAddress_profile);
    ageTextView = root.findViewById(R.id.tv_age_profile);

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

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
      callback = (Callback) context;
      setRetainInstance(true);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    callback = null;
  }

  //updates fragment data in real time
  @Override
  public void onStart() {
    super.onStart();
    user = FirebaseAuth.getInstance().getCurrentUser();
    userID = user.getUid();
    docRef = FirebaseFirestore.getInstance().collection("Users").document(userID);

    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
      @Override
      public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
          if(error != null){
            Toast.makeText(getActivity(), "Error while loading", Toast.LENGTH_LONG).show();
            return;
          }
          if(value.exists()){
            String fullName = value.getString("fullName");
            String age = value.getString("age");
            String email = value.getString("email");

            fullNameTextView.setText(fullName);
            emailTextView.setText(email);
            ageTextView.setText(age);
            greetingTextView.setText("Hello " + fullName);
          }
      }
    });
  }//end onStart
}