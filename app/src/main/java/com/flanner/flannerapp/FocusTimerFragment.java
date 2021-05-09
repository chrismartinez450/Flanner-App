package com.flanner.flannerapp;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flanner.flannerapp.DatabaseUser.UserTimer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FocusTimerFragment extends Fragment {

  public static final String FOCUS_TIMER_HISTORY = "Focus Timer History";
  Button btnStart, btnStop, btnPause, btnResume;
  ImageView icAnchor;
  Animation roundingAnim;
  Chronometer chrono;
  long timeWhenStopped = 0;
  String subject = "";
  private Date currentTime = Calendar.getInstance().getTime();
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  private CollectionReference userPath = db.collection("Users");
  private FirebaseUser user;
  private String userID;
  SimpleDateFormat timerDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);




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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("What is the subject of which you are studying?");

        final EditText input = new EditText(getActivity());
        input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            subject = input.getText().toString();
            icAnchor.startAnimation(roundingAnim);
            btnStop.animate().alpha(1).setDuration(300).start();
            btnPause.animate().alpha(1).setDuration(300).start();
            btnStart.animate().alpha(0).setDuration(300).start();
            chrono.setBase(SystemClock.elapsedRealtime());
            chrono.start();
          }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
          }
        });

        builder.show();

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
            btnResume.setAlpha(0);
            Toast.makeText(getActivity(),"Studied for: " + chrono.getText().toString(), Toast.LENGTH_SHORT ).show();
            setUserTimerHistory(currentTime, chrono.getText().toString(), subject);
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

  public void setUserTimerHistory(Date date, String time, String subject) {

      UserTimer currentUserTimer = new UserTimer(timerDateFormat.format(date), time, subject, currentTime.getTime()
      );
      currentUserTimer.setSubject(subject);
      user = FirebaseAuth.getInstance().getCurrentUser();
      userID = user.getUid();
      userPath.document(userID).collection(FOCUS_TIMER_HISTORY).add(currentUserTimer);

  }

  private void initializeLayout(View root) {

  }
}
