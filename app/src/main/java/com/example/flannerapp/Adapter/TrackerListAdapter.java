package com.example.flannerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.flannerapp.R;
import com.example.flannerapp.DatabaseUser.UserTimer;

import java.util.ArrayList;


public class TrackerListAdapter extends ArrayAdapter<UserTimer> {

    private Context mContext;
    private int mResource;

    public TrackerListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<UserTimer> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the persons information

        String date = getItem(position).getDate();
        String subject = getItem(position).getSubject();
        String chronoTime = getItem(position).getChronoTime();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        displayDataToScreen(convertView, subject, chronoTime, date);

        return convertView;
    }

    public void displayDataToScreen(View convertView, String subject, String chronoTime, String date) {
        TextView tvSubject = convertView.findViewById(R.id.tv_subject);
        TextView tvChronoTime = convertView.findViewById(R.id.tv_chronoTime);
        TextView tvDate = convertView.findViewById(R.id.tv_date);
        tvSubject.setText(subject);
        tvChronoTime.setText(chronoTime);
        tvDate.setText(date);
    }


}

