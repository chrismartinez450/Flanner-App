package com.example.flannerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.flannerapp.DatabaseUser.Events;
import com.example.flannerapp.R;

import java.util.List;

public class CustomCalendarListAdapter extends ArrayAdapter<Events> {

  private Context mContext;
  private int mResource;

  public CustomCalendarListAdapter(@NonNull Context context, int resource, @NonNull List<Events> objects) {
    super(context, resource, objects);
    mContext = context;
    mResource = resource;
  }

  @NonNull
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    //get the persons information

    String time = getItem(position).getTIME();
    String event = getItem(position).getEVENT();
    String date = getItem(position).getDATE();

    LayoutInflater inflater = LayoutInflater.from(mContext);
    convertView = inflater.inflate(mResource, parent, false);

    displayDataToScreen(convertView, time, event, date);

    return convertView;
  }

  public void displayDataToScreen(View convertView, String time, String event, String date){
    TextView tvTime = convertView.findViewById(R.id.tv_eventTime);
    TextView tvEvents = convertView.findViewById(R.id.tv_eventName);
    TextView tvDate = convertView.findViewById(R.id.tv_eventDate);
    tvTime.setText(time);
    tvEvents.setText(event);
    tvDate.setText(date);
  }


}
