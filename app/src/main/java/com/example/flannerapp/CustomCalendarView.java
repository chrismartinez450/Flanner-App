package com.example.flannerapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flannerapp.Adapter.CalendarEventRecycleViewAdapter;
import com.example.flannerapp.Adapter.MyGridAdapter;
import com.example.flannerapp.DatabaseUser.Events;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import yuku.ambilwarna.AmbilWarnaDialog;

public class CustomCalendarView extends LinearLayout {

  public static final String DATE = "date";
  public static final String CALENDAR_EVENTS = "Calendar Events";
  public static final String TEST = "test"; // Test instance for log
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
  private final SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
  private final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
  private final SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
  private static final int MAX_CALENDAR_DAYS = 42;
  private final CollectionReference userPath = FirebaseFirestore.getInstance().collection("Users");
  private final Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
  private int radioButtonChoice; // =1 for all events, = 2 for upcoming events only
  private int cardViewBackgroundColor;
  private List<Date> dates;
  private List<Events> eventsList;
  private ImageButton nextButton, previousButton;
  private TextView currentDate;
  private GridView gridView;
  private Context context;
  private MyGridAdapter myGridAdapter;
  private AlertDialog alertDialog;
  private String userID;
  private RadioGroup radioGroup;
  private RecyclerView mRecyclerView;
  private CalendarEventRecycleViewAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

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

    radioButtonActivity();

    // The implementation of the gridView (calendar + events)
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
        final Button colorSelectButton = addView.findViewById(R.id.btn_color_select_calendar);
        colorSelectButton.setBackgroundColor(cardViewBackgroundColor);
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
        colorSelectButton.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), cardViewBackgroundColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
              @Override
              public void onCancel(AmbilWarnaDialog dialog) {
              }
              @Override
              public void onOk(AmbilWarnaDialog dialog, int color) {
                cardViewBackgroundColor = color;
                colorSelectButton.setBackgroundColor(color);
              }
            });
            colorPicker.show();
          }
        });
        final String date = eventDateFormat.format(dates.get(position));
        final String month = monthFormat.format(dates.get(position));
        final String year = yearFormat.format(dates.get(position));
        addEvent.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            Events newEvent = new Events(eventName.getText().toString(), eventTime.getText().toString(), date, month, year, String.valueOf(cardViewBackgroundColor));
            userPath.document(userID).collection(CALENDAR_EVENTS).add(newEvent);
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

  /**
   * Initialize all of the variables needed
   */
  private void initializeLayout() {
    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View view = inflater.inflate(R.layout.calendar_layout, this);
    nextButton = view.findViewById(R.id.nextBtn);
    previousButton = view.findViewById(R.id.previousBtn);
    currentDate = view.findViewById(R.id.current_date);
    gridView = view.findViewById(R.id.gridView);
    radioGroup = view.findViewById(R.id.radioButton_calendar);
    userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    mRecyclerView = findViewById(R.id.recyclerView);
    mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getContext());
    mAdapter = new CalendarEventRecycleViewAdapter(new ArrayList<EventCalendarCardView>());
    radioButtonChoice = 1; // =1 for all events, = 2 for upcoming events only
    cardViewBackgroundColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
    dates = new ArrayList();
    eventsList = new ArrayList();
  }

  public interface OnTaskedCompleted {
    void onSuccess(List<Events> events);
    void onFail();
  }

  /**
   * This function will get all of the events, and pass into the MyGridAdapter.java to show the events on calendar correctly
   */
  public void updateEventsListAdapter(final OnTaskedCompleted listener) {
    eventsList.clear();
    listViewDataTable(radioButtonChoice);

    // Add the data to display on the calendar
    userPath.document(userID).collection(CALENDAR_EVENTS)
      .orderBy(DATE, Query.Direction.ASCENDING)
      .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
          Events currentEvent = documentSnapshot.toObject(Events.class);
          eventsList.add(currentEvent);
        }
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


  /**
   * This function will reload the calendar with all of the new datas
   */
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

  public Date ConvertStringToDate(String eventDate) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date date = null;
    try {
      date = format.parse(eventDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  /**
   * radioButtonChoice = 1 -> Show all events
   * radioButtonChoice = 2 -> Show current events and later
   */
  public void radioButtonActivity() {
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (radioGroup.getCheckedRadioButtonId()) {
          case R.id.radioButton_allEvents_calendar:
            radioButtonChoice = 1;
            listViewDataTable(radioButtonChoice);
            break;
          case R.id.radioButton_upcomingEvents_calendar:
            radioButtonChoice = 2;
            listViewDataTable(radioButtonChoice);
            break;
          default:
            break;
        }
      }
    });
  }

  /**
   * This function will display the events list by using cardView
   */
  private void listViewDataTable(final int radioButtonChoice) {
    final ArrayList<EventCalendarCardView> allEventsList = new ArrayList<>();
    final List<CustomEvents> showEventsList = new ArrayList();
    final Date date = new Date();
    // Adding the data for the list view, nothing to do with the calendar data
    userPath.document(userID).collection(CALENDAR_EVENTS)
      .orderBy(DATE, Query.Direction.ASCENDING)
      .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
      @Override
      public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
          Events currentEvent = documentSnapshot.toObject(Events.class);
          String id = documentSnapshot.getId();
          Date currentDate = ConvertStringToDate(currentEvent.getDATE());
          if (radioButtonChoice == 1) {
            showEventsList.add(new CustomEvents(currentEvent.getEVENT(), currentEvent.getTIME(), currentEvent.getDATE(), currentEvent.getCARDVIEWCOLOR(), id));
          } else { // radioButtonChoice == 2
            if (currentDate.compareTo(date) > 0) {
              showEventsList.add(new CustomEvents(currentEvent.getEVENT(), currentEvent.getTIME(), currentEvent.getDATE(), currentEvent.getCARDVIEWCOLOR(), id));
            }
          }
        }
        for (CustomEvents showEventsItem : showEventsList) {
          allEventsList.add(new EventCalendarCardView(showEventsItem.getTime(),
            showEventsItem.getEvent(),
            showEventsItem.getDate(),
            showEventsItem.getCardViewColor(),
            showEventsItem.getDocId()));
        }
        buildRecyclerView(allEventsList, showEventsList);
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Log.d("test", "no data");
      }
    });
  }

  /**
   * This function will build the recycler view
   * @param allEventsList : All of the events
   * @param showEventsList: Only the events that will show on calendar. Just in case the user select on showing only current events
   */
  private void buildRecyclerView(final ArrayList<EventCalendarCardView> allEventsList, final List<CustomEvents> showEventsList) {
    mRecyclerView.setHasFixedSize(true);
    mAdapter = new CalendarEventRecycleViewAdapter(allEventsList);
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mAdapter);

    mAdapter.setOnItemClickListener(new CalendarEventRecycleViewAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(int position) {

      }
      @Override
      public void onDeleteClick(int position) {
        removeItem(position, allEventsList, showEventsList);
      }
    });
  }

  /**
   * This function will handle the remove icon on event cardView
   * @param position: Position on events arraylist
   * @param allEventsList: All of the events
   * @param showEventsList: Only the events that will show on calendar. Just in case the user select on showing only current events
   */
  private void removeItem(int position, ArrayList<EventCalendarCardView> allEventsList, List<CustomEvents> showEventsList) {
    EventCalendarCardView removedCardView = allEventsList.remove(position);
    showEventsList.remove(position);
    try {
      userPath.document(userID).collection(CALENDAR_EVENTS).document(removedCardView.getDocId())
        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
          Log.d(TEST, "onSuccess: We have deleted it successfully");
        }
      })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Log.e(TEST, "onFailure: " + e);
          }
        });
    } catch (Exception e) {
      Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
    setUpCalendar();
    mAdapter.notifyItemChanged(position);
    mAdapter.notifyDataSetChanged();
  }
}