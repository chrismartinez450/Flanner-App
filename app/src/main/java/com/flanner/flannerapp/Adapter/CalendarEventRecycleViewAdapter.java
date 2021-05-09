package com.flanner.flannerapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.flanner.flannerapp.EventCalendarCardView;
import com.flanner.flannerapp.R;
import java.util.ArrayList;

public class CalendarEventRecycleViewAdapter extends RecyclerView.Adapter<CalendarEventRecycleViewAdapter.ExampleViewHolder> {
  private ArrayList<EventCalendarCardView> eventCalendarList;
  private OnItemClickListener mListener;

  public interface OnItemClickListener {
    void onItemClick(int position);
    void onDeleteClick(int position);
  }
  public void setOnItemClickListener(OnItemClickListener listener) {
    mListener = listener;
  }

  public static class ExampleViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImageDeleteIcon;
    public TextView eventTimeTextView;
    public TextView eventNameTextView;
    public TextView eventDateTextView;
    public CardView cardView;

    public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
      super(itemView);
      mImageDeleteIcon = itemView.findViewById(R.id.image_delete);
      eventTimeTextView = itemView.findViewById(R.id.events_time_calendar_cardview);
      eventNameTextView = itemView.findViewById(R.id.events_name_calendar_cardview);
      eventDateTextView = itemView.findViewById(R.id.events_date_calendar_cardview);
      mImageDeleteIcon = itemView.findViewById(R.id.image_delete);
      cardView = itemView.findViewById(R.id.calendar_cardview);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (listener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
              listener.onItemClick(position);
            }
          }
        }
      });

      mImageDeleteIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (listener != null) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
              listener.onDeleteClick(position);
            }
          }
        }
      });
    }
  }

  public CalendarEventRecycleViewAdapter(ArrayList<EventCalendarCardView> eventsList) {
    eventCalendarList = eventsList;
  }

  @NonNull
  @Override
  public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_calendar_cardview, parent, false);
    ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
    return evh;
  }

  @Override
  public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
    // first item position = 0, second item position = 1, and so on
    EventCalendarCardView currentItem = eventCalendarList.get(position);
    int backgroundColor = Integer.parseInt(currentItem.getBackgroundCVColor());
    holder.eventTimeTextView.setText(currentItem.getEventTime());
    holder.eventNameTextView.setText(currentItem.getEventName());
    holder.eventDateTextView.setText(currentItem.getEventDate());
    holder.cardView.setCardBackgroundColor(backgroundColor);
  }

  @Override
  public int getItemCount() {
    // how many items in the list
    return eventCalendarList.size();
  }
}
