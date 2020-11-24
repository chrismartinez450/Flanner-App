package com.example.flannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
  private TextView displayEmail;
  private TextView displayFullName;
  private EditText displayAge;
  private EditText displayGoal;
  private Button confirmButton;
  private Button signOutButton;
  private Button deleteAccountButton;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_profiles, container, false);
    initializeLayout(root);
    displayData();
    confirmButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
      }
    });
    signOutButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), MainActivity.class));
        Toast.makeText(getContext(), "Sign out Successful", Toast.LENGTH_SHORT).show();
      }
    });
    deleteAccountButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), MainActivity.class));
        Toast.makeText(getContext(), "Delete Account Successful", Toast.LENGTH_SHORT).show();
      }
    });
    return root;
  }

  private void initializeLayout(View root) {
    displayEmail = root.findViewById(R.id.displayEmail);
    displayFullName = root.findViewById(R.id.displayFullName);
    displayAge = root.findViewById(R.id.displayAge);
    displayGoal = root.findViewById(R.id.displayGoal);
    confirmButton = root.findViewById(R.id.confirm_button);
    signOutButton = root.findViewById(R.id.signOut_button);
    deleteAccountButton = root.findViewById(R.id.deleteAccount_button);
  }

  private void displayData() {
    displayEmail.setText("Email");
    displayFullName.setText("Name");
    // displayAge.setText(0);
    displayGoal.setText("Goal");
  }
}
