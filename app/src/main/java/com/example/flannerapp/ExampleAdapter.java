package com.example.flannerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
  private ArrayList<EventCalendarCardView> eventCalendarList;

  public static class ExampleViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImageDeleteIcon;
    public TextView eventTimeTextView;
    public TextView eventNameTextView;
    public TextView eventDateTextView;

    public ExampleViewHolder(@NonNull View itemView) {
      super(itemView);
      mImageDeleteIcon = itemView.findViewById(R.id.image_delete);
      eventTimeTextView = itemView.findViewById(R.id.events_time_calendar_cardview);
      eventNameTextView = itemView.findViewById(R.id.events_name_calendar_cardview);
      eventDateTextView = itemView.findViewById(R.id.events_date_calendar_cardview);
    }
  }

  public ExampleAdapter(ArrayList<EventCalendarCardView> eventsList) {
    eventCalendarList = eventsList;
  }

  @NonNull
  @Override
  public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_calendar_cardview, parent, false);
    ExampleViewHolder evh = new ExampleViewHolder(v);
    return evh;
  }

  @Override
  public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
    // first item position = 0, second item position = 1, and so on
    EventCalendarCardView currentItem = eventCalendarList.get(position);
    holder.eventTimeTextView.setText(currentItem.getEventTime());
    holder.eventNameTextView.setText(currentItem.getEventName());
    holder.eventDateTextView.setText(currentItem.getEventDate());

  }

  @Override
  public int getItemCount() {
    // how many items in the list
    return eventCalendarList.size();
  }
}
