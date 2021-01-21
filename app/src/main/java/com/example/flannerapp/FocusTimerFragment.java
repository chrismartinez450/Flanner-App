package com.example.flannerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FocusTimerFragment extends Fragment {

  Button btnStart, btnStop, btnPause, btnResume;
  ImageView icAnchor;
  Animation roundingAnim;
  Chronometer chrono;
  long timeWhenStopped = 0;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_focustimer, container, false);
    initializeLayout(root);

    icAnchor = root.findViewById(R.id.icAnchor);
    btnStart = root.findViewById(R.id.btnStart);
    btnStop = root.findViewById(R.id.btnStop);
    btnPause = root.findViewById(R.id.btnPause);
    chrono = root.findViewById(R.id.chrono);
    btnResume = root.findViewById(R.id.btnResume);

    btnStop.setAlpha(0);
    btnPause.setAlpha(0);
    btnResume.setAlpha(0);

    roundingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rounding);
    Typeface MMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MMedium.ttf");
    btnStart.setTypeface(MMedium);
    btnStop.setTypeface(MMedium);
    btnPause.setTypeface(MMedium);

    btnStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        icAnchor.startAnimation(roundingAnim);
        btnStop.animate().alpha(1).setDuration(300).start();
        btnPause.animate().alpha(1).setDuration(300).start();
        btnStart.animate().alpha(0).setDuration(300).start();
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
      }
    });

    btnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        icAnchor.clearAnimation();
        timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
        chrono.stop();

        AlertDialog.Builder finishAlert = new AlertDialog.Builder(getActivity());
        finishAlert.setTitle("Finished Studying");
        finishAlert.setMessage("Are you sure you're finished?");
        finishAlert.setPositiveButton("Finished", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            btnStart.animate().alpha(1).setDuration(300).start();
            btnStop.animate().alpha(0).setDuration(300).start();
            btnPause.setAlpha(0);
            Toast.makeText(getActivity(),"Studied for: " + chrono.getText().toString(), Toast.LENGTH_SHORT ).show();
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.stop();
          }
        });
        finishAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
            chrono.start();
            icAnchor.startAnimation(roundingAnim);
          }
        });
        finishAlert.show();
      }
    });

    btnPause.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
        chrono.stop();
        btnResume.animate().alpha(1).setDuration(300).start();
        btnPause.animate().alpha(0).setDuration(300).start();
        icAnchor.clearAnimation();
      }
    });

    btnResume.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        chrono.start();
        icAnchor.startAnimation(roundingAnim);
        btnPause.animate().alpha(1).setDuration(300).start();
        btnResume.animate().alpha(0).setDuration(300).start();
      }
    });

    return root;
  }

  private void initializeLayout(View root) {

  }
}
