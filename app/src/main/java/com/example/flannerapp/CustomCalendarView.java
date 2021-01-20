package com.example.flannerapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {

  public static final String DATE = "date";
  ImageButton nextButton, previousButton;
  TextView currentDate;
  GridView gridView;
  private static final int MAX_CALENDAR_DAYS = 42;
  Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
  Context context;
  SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
  SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
  SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
  SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

  MyGridAdapter myGridAdapter;
  ListView lvEvents;

  AlertDialog alertDialog;
  List<Date> dates = new ArrayList();
  final List<Events> eventsList = new ArrayList();

  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private CollectionReference userEventInfo = db.collection("events");

  public CustomCalendarView(Context context) {
    super(context);
  }

  public CustomCalendarView(final Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    initializeLayout();
    setUpCalendar();

    previousButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        calendar.add(Calendar.MONTH, -1);
        setUpCalendar();
      }
    });

    nextButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        calendar.add(Calendar.MONTH, 1);
        setUpCalendar();
      }
    });

    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        final View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout, null);
        final EditText eventName = addView.findViewById(R.id.event_name);
        final TextView eventTime = addView.findViewById(R.id.cv_eventTime);
        ImageButton setTime = addView.findViewById(R.id.set_event_time);
        final Button addEvent = addView.findViewById(R.id.btn_add_event);
        setTime.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), R.style.Theme_AppCompat_Dialog, new TimePickerDialog.OnTimeSetListener() {
              @Override
              public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                c.setTimeZone(TimeZone.getDefault());
                SimpleDateFormat hourFormat = new SimpleDateFormat("K:mm a", Locale.ENGLISH);
                String event_Time = hourFormat.format(c.getTime());
                eventTime.setText(event_Time);
              }
            }, hours, minutes, false);
            timePickerDialog.show();
          }
        });
        final String date = eventDateFormat.format(dates.get(position));
        final String month = monthFormat.format(dates.get(position));
        final String year = yearFormat.format(dates.get(position));

        addEvent.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            Toast.makeText(context, "event is: " + eventName.getText().toString()
              + " \nthe time is: " + eventTime.getText().toString() + " \ndate : " + date + "\nmonth : " + month
            + " \nyear: " + year, Toast.LENGTH_LONG).show();
            Events newEvent = new Events(eventName.getText().toString(), eventTime.getText().toString(), date, month, year);
            CollectionReference c = db.collection("events");
            // c.document(date + " " + eventTime.getText().toString()).set(newEvent);
            c.add(newEvent);
            setUpCalendar();
            alertDialog.dismiss();
          }
        });

        builder.setView(addView);
        alertDialog = builder.create();
        alertDialog.show();
      }
    });

  }

  public interface OnTaskedCompleted {
    void onSuccess(List<Events> events);
    void onFail();
  }

  public void updateEventsListAdapter(final OnTaskedCompleted listener) {
    eventsList.clear();
    db.collection("events")
      .orderBy(DATE, Query.Direction.ASCENDING)
      .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
          Events currentEvent = documentSnapshot.toObject(Events.class);
          eventsList.add(currentEvent);
        }
        CustomCalendarListAdapter ccAdapter = new CustomCalendarListAdapter(getContext(), R.layout.adapter_event_view_layout, eventsList);
        lvEvents.setAdapter(ccAdapter);
        listener.onSuccess(eventsList);
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        listener.onFail();
      }
    });
  }

  public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  private void initializeLayout() {
    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.calendar_layout, this);
    nextButton = view.findViewById(R.id.nextBtn);
    previousButton = view.findViewById(R.id.previousBtn);
    currentDate = view.findViewById(R.id.current_date);
    gridView = view.findViewById(R.id.gridView);
    lvEvents = view.findViewById(R.id.lv_events);
  }

  private void setUpCalendar() {
    String currentDate = dateFormat.format(calendar.getTime());
    this.currentDate.setText(currentDate);
    dates.clear();

    final Calendar monthCalendar = (Calendar)calendar.clone();
    monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
    int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
    monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);
    while (dates.size() < MAX_CALENDAR_DAYS) {
      dates.add(monthCalendar.getTime());
      monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
    }
    updateEventsListAdapter(new OnTaskedCompleted() {
      @Override
      public void onSuccess(List<Events> events) {

        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);
      }

      @Override
      public void onFail() {
      }
    });

  }

}
