package com.example.flannerapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TrackerFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userPath = db.collection("Users");
    private ArrayList<UserTimer> tList = new ArrayList<>();
    private double[] barValues = new double[7];
    private Date currentDate = Calendar.getInstance().getTime();
    private String userID;
    private FirebaseUser user;
    BarChart mBarchart;
    public static final String FOCUS_TIMER_HISTORY = "Focus Timer History";
    private static final String[] DAYS = new String[7];
    SimpleDateFormat timerDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private String[] today = new String[3];
    private ListView lvTracker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_tracker, container, false);
        setData(root);
        lvTracker = root.findViewById(R.id.tList);
        userPath
                .document(userID)
                .collection(FOCUS_TIMER_HISTORY)
                .orderBy("timeInMilli")
                .startAt(currentDate.getTime() - 604800000)
                .endAt(currentDate.getTime())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    UserTimer usert = documentSnapshot.toObject(UserTimer.class);
                    tList.add(usert);
                }
                initializeLayout(root);
                TrackerListAdapter tAdapter = new TrackerListAdapter(getContext(), R.layout.adapter_tracker_view_layout, tList);
                lvTracker.setAdapter(tAdapter);
            }
        });

        return root;
    }

    private void configureChartAppearance() {
        String[] tempToday = new String[3];
        int j = 6, x = 7;
        mBarchart.getDescription().setEnabled(false);
        mBarchart.setDrawValueAboveBar(false);
        mBarchart.setNoDataText("No Data To Show");
        XAxis xAxis = mBarchart.getXAxis();
        xAxis.setTextSize(10f);
        today = timerDateFormat.format(currentDate).split("-");
        for (int i = 0; i < today.length; i++) tempToday[i] = today[i];

        for (int i = 0; i < x; i++) {
            if (Integer.parseInt(tempToday[2]) >= 7) {
                DAYS[j] = (Integer.parseInt(tempToday[1]) + "/" + (Integer.parseInt(tempToday[2]) - i));
                j--;
            } else {
                if (Integer.parseInt(tempToday[2]) - i == 0) {
                    if (Integer.parseInt(tempToday[1]) == 1) {
                        tempToday[2] = "31";
                        tempToday[1] = "12";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 2) {
                        tempToday[2] = "31";
                        tempToday[1] = "1";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 3) {
                        tempToday[2] = "28";
                        tempToday[1] = "2";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 4) {
                        tempToday[2] = "31";
                        tempToday[1] = "3";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 5) {
                        tempToday[2] = "30";
                        tempToday[1] = "4";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 6) {
                        tempToday[2] = "31";
                        tempToday[1] = "5";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 7) {
                        tempToday[2] = "30";
                        tempToday[1] = "6";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 8) {
                        tempToday[2] = "31";
                        tempToday[1] = "7";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 9) {
                        tempToday[2] = "31";
                        tempToday[1] = "8";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 10) {
                        tempToday[2] = "31";
                        tempToday[1] = "9";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 11) {
                        tempToday[2] = "31";
                        tempToday[1] = "10";
                        x -= i;
                        i = 0;
                    }
                    if (Integer.parseInt(tempToday[1]) == 12) {
                        tempToday[2] = "30";
                        tempToday[1] = "11";
                        x -= i;
                        i = 0;
                    }
                }
                DAYS[j] = (Integer.parseInt(tempToday[1]) + "/" + (Integer.parseInt(tempToday[2]) - i));
                j--;
            }
        }
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
        Legend legend = mBarchart.getLegend();
        legend.setEnabled(false);
    }

    private ArrayList<BarEntry> dataValues() {
        final ArrayList<BarEntry> dv = new ArrayList<>();
        String[] strArr = new String[7];
        String[] tempArr = new String[2];
        int j = 6;
        for (int i = 0; i < DAYS.length; i++) {
            tempArr = DAYS[i].split("/");
            strArr[i] = tempArr[1];
        }
        for (int i = 0; i < tList.size(); i++) {
            if (Integer.parseInt(tList.get(i).getDay()) == (Integer.parseInt(strArr[0]))) {
                barValues[0] += tList.get(i).convertChronoTime();
            } else if (Integer.parseInt(tList.get(i).getDay()) == (Integer.parseInt(strArr[1]))) {
                barValues[1] += tList.get(i).convertChronoTime();
            } else if (Integer.parseInt(tList.get(i).getDay()) == (Integer.parseInt(strArr[2]))) {
                barValues[2] += tList.get(i).convertChronoTime();
            } else if (Integer.parseInt(tList.get(i).getDay()) == (Integer.parseInt(strArr[3]))) {
                barValues[3] += tList.get(i).convertChronoTime();
            } else if (Integer.parseInt(tList.get(i).getDay()) == (Integer.parseInt(strArr[4]))) {
                barValues[4] += tList.get(i).convertChronoTime();
            } else if (Integer.parseInt(tList.get(i).getDay()) == (Integer.parseInt(strArr[5]))) {
                barValues[5] += tList.get(i).convertChronoTime();
            } else if (Integer.parseInt(tList.get(i).getDay()) == (Integer.parseInt(strArr[6]))) {
                barValues[6] += tList.get(i).convertChronoTime();
            }
        }

        for (int i = 0; i < 7; i++) {

            dv.add(new BarEntry(i, (float) barValues[i]));
        }
        return dv;
    }

    public void setData(View root) {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

    }

    private void initializeLayout(View root) {
        mBarchart = root.findViewById(R.id.tracker_chart);
        configureChartAppearance();
        BarDataSet bds0 = new BarDataSet(dataValues(), "");
        BarData bd0 = new BarData(bds0);
        mBarchart.setData(bd0);
        mBarchart.invalidate();
        mBarchart.refreshDrawableState();

    }

}
