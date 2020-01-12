package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView timerTextView;
    ListView timerListView;
    FloatingActionButton startTimerFloatingActionButton;
    FloatingActionButton stopTimerFloatingActionButton;
    FloatingActionButton lapFloatingActionButton;
    FloatingActionButton resetTimerFloatingActionButton;

    Handler handler = new Handler();

    long startTime = 0L, timeInMilisecounds = 0L, timeSwapBuff = 0L, updateTime = 0L;
    int secs, mins, milisecounds;

    String[] ListElements = new String[]{ };
    List<String> ListElementsArrayList;
    ArrayAdapter<String> adapter;

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilisecounds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilisecounds;
            secs = (int) (updateTime / 1000);
            mins = secs /60;
            secs %= 60;
            milisecounds = (int) (updateTime%1000);
            timerTextView.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milisecounds));
            handler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        timerListView = findViewById(R.id.timerListView);
        startTimerFloatingActionButton = findViewById(R.id.startTimerFloatingActionButton);
        stopTimerFloatingActionButton = findViewById(R.id.stopTimerFloatingActionButton);
        lapFloatingActionButton = findViewById(R.id.lapFloatingActionButton);
        resetTimerFloatingActionButton = findViewById(R.id.resetTimerFloatingActionButton);

        ListElementsArrayList = new ArrayList<String>(Arrays.asList(ListElements));

        adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                ListElementsArrayList
        );

        timerListView.setAdapter(adapter);

        startTimerFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Started!", Snackbar.LENGTH_LONG).setAction("A", null).show();
                startTimerFloatingActionButton.setVisibility(View.INVISIBLE);
                stopTimerFloatingActionButton.setVisibility(View.VISIBLE);
                startTime = SystemClock.uptimeMillis();
                handler.postDelayed(updateTimerThread,0);
            }
        });

        stopTimerFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Stopped!", Snackbar.LENGTH_LONG).setAction("A", null).show();
                stopTimerFloatingActionButton.setVisibility(View.INVISIBLE);
                startTimerFloatingActionButton.setVisibility(View.VISIBLE);
                timeSwapBuff += timeInMilisecounds;
                handler.removeCallbacks(updateTimerThread);
            }
        });

        lapFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Lap!", Snackbar.LENGTH_SHORT).setAction("A", null).show();
                ListElementsArrayList.add(timerTextView.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });

        resetTimerFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Reset!", Snackbar.LENGTH_SHORT).setAction("A", null).show();
                startTime = 0L;
                timeInMilisecounds = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                secs = 0;
                mins = 0;
                milisecounds = 0;
                timerTextView.setText("00:00:000");
                ListElementsArrayList.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }
}
