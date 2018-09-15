package com.emem.handyapps.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.emem.handyapps.R;

import java.util.Calendar;
import java.util.Date;

public class TimerActivity extends AppCompatActivity {

    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    NumberPicker timerMin;
    NumberPicker timerSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        timerMin = findViewById(R.id.timerMin);
        timerMin.setMinValue(0);
        timerMin.setMaxValue(10);
        timerSec = findViewById(R.id.timerSec);
        timerSec.setMinValue(0);
        timerSec.setMaxValue(60);
    }

    public void onTimerToggleClicked(View view){
        long time;
        if (((ToggleButton) view).isChecked()){
            Toast.makeText(this, "Timer on", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            time = calendar.getTimeInMillis();
            time += 1000*timerSec.getValue() + 1000*60*timerMin.getValue();
            Intent intent = new Intent(this, TimerReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            if (Build.VERSION.SDK_INT>=19)
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(this, "Timer off", Toast.LENGTH_SHORT).show();
        }
    }
}
