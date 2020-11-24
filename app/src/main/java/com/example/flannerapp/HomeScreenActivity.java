package com.example.flannerapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeScreenActivity extends AppCompatActivity {
  private Bundle bundle = new Bundle();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_homescreen);
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
          case R.id.nav_planner:
            selectedFragment = new PlannerFragment();
            break;
          case R.id.nav_focusTimer:
            selectedFragment = new FocusTimerFragment();
            break;
          case R.id.nav_tracker:
            selectedFragment = new TrackerFragment();
            break;
          case R.id.nav_profiles:
            selectedFragment = new ProfileFragment();
            break;
        }
        selectedFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
      }
    });

    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlannerFragment()).commit();
  }
}
