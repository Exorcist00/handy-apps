package com.emem.handyapps.alarm;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.emem.handyapps.R;

public class StopwatchActivity extends AppCompatActivity {

    private TextView stopwatchTimer;
    private Handler handler = new Handler();
    private long startTime;
    private Runnable stopwatchTimerRunner = new Runnable() {
        @Override
        public void run() {
            long time = SystemClock.uptimeMillis() - startTime;
            int mili = (int) time % 1000;
            int sec = (int) time / 1000;
            int min = sec / 60;
            sec = sec % 60;
            stopwatchTimer.setText(
                    "" + String.format("%02d", min)
                    + ":" + String.format("%02d", sec)
                    + ":" + String.format("%03d", mili)
            );
            handler.post(this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        stopwatchTimer = findViewById(R.id.stopwatchTimer);
        stopwatchTimer.setText("00:00:000");
    }

    public void startStopwatch(View view){
        startTime = SystemClock.uptimeMillis();
        handler.post(stopwatchTimerRunner);
    }

    public void stopStopwatch(View view){
        handler.removeCallbacks(stopwatchTimerRunner);
    }
}
