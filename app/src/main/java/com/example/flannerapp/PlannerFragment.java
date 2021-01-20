package com.example.flannerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlannerFragment extends Fragment {

  CustomCalendarView customCalendarView;
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_planner, container, false);
    initializeLayout(root);
    return root;
  }

  private void initializeLayout(View root) {
    customCalendarView = root.findViewById(R.id.custom_calendar_view);
  }
}
