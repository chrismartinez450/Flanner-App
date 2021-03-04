package com.example.flannerapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class TrackerFragment extends Fragment {
    private static final int MAX_X_VALUE = 7;
    BarChart mBarchart; //bar chart
    private static final String[] DAYS = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_tracker, container, false);
    initializeLayout(root);

    return root;
  }

  private void configureChartAppearance() {
      mBarchart.getDescription().setEnabled(false);
      mBarchart.setDrawValueAboveBar(false);

      XAxis xAxis = mBarchart.getXAxis();
      xAxis.setValueFormatter(new ValueFormatter() {
          @Override
          public String getFormattedValue(float value) {
                return DAYS[(int) value];
          }
      });

      YAxis axisLeft = mBarchart.getAxisLeft();
      axisLeft.setGranularity(10f);
      axisLeft.setAxisMinimum(0);

      YAxis axisRight = mBarchart.getAxisRight();
      axisRight.setGranularity(10f);
      axisRight.setAxisMinimum(0);
  }
    //Add data values for bar chart
  private ArrayList<BarEntry> dataValues(){
    ArrayList<BarEntry> dv = new ArrayList<>();

    for (int i=0; i < MAX_X_VALUE; i++) {
        dv.add(new BarEntry(i, 0));
    }

    return dv;
  }
    //Create Bar chart
  private void initializeLayout(View root) {
      mBarchart = root.findViewById(R.id.tracker_chart);
      configureChartAppearance();
      BarDataSet bds0 = new BarDataSet(dataValues(), "");

      BarData bd0 = new BarData(bds0);
      mBarchart.setData(bd0);
  }
}
