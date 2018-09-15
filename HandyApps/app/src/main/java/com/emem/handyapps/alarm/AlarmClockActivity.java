package com.emem.handyapps.alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.emem.handyapps.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AlarmClockActivity extends AppCompatActivity {
    private static final String ALARM_1 = "alarm_1";
    private static final String ALARM_2 = "alarm_2";
    private static final String ALARM_3 = "alarm_3";
    private static final String ALARM_4 = "alarm_4";
    private static final String ALARM_5 = "alarm_5";
    private static final int ALARM_1_CODE = 1;
    private static final int ALARM_2_CODE = 2;
    private static final int ALARM_3_CODE = 3;
    private static final int ALARM_4_CODE = 4;
    private static final int ALARM_5_CODE = 5;
    private static final String HOUR = "hour";
    private static final String MIN = "min";
    private static final String TEXT = "text";
    private static final String WEEKDAYS = "weekdays";
    private static final String SOUND = "sound";
    private int chosenHour;
    private int chosenMin;
    private boolean isAlarmActive;
    private CheckBox mondayCheckbox;
    private CheckBox tuesdayCheckbox;
    private CheckBox wednesdayCheckbox;
    private CheckBox thursdayCheckbox;
    private CheckBox fridayCheckbox;
    private CheckBox saturdayCheckbox;
    private CheckBox sundayCheckbox;
    private EditText alarmText;
    private Switch soundSwitch;
    private String chosenAlarm;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        initializeButtons();
        fillWithChosenAlarm(ALARM_1);
    }

    public void chooseTime(View view){
        DialogFragment timePicker = new AlarmTimePickerFragment();
        timePicker.show(getFragmentManager(), "AlarmTimePicker");
    }

    private void fillWithChosenAlarm(String alarm){
        chosenAlarm = alarm;
        pref = getSharedPreferences(chosenAlarm, Activity.MODE_PRIVATE);
        chosenHour = pref.getInt(HOUR, 0);
        chosenMin = pref.getInt(MIN, 0);

        mondayCheckbox = findViewById(R.id.mondayCheckbox);
        tuesdayCheckbox = findViewById(R.id.tuesdayCheckbox);
        wednesdayCheckbox = findViewById(R.id.wednesdayCheckbox);
        thursdayCheckbox = findViewById(R.id.thursdayCheckbox);
        fridayCheckbox = findViewById(R.id.fridayCheckbox);
        saturdayCheckbox = findViewById(R.id.saturdayCheckbox);
        sundayCheckbox = findViewById(R.id.sundayCheckbox);

        alarmText = findViewById(R.id.alarmText);
        alarmText.setText(pref.getString(TEXT, ""));
        soundSwitch = findViewById(R.id.soundSwitch);
        soundSwitch.setChecked(pref.getBoolean(SOUND, true));

        List<String> weekdays = getWeekdays();
        mondayCheckbox.setChecked(weekdays.contains("1"));
        tuesdayCheckbox.setChecked(weekdays.contains("2"));
        wednesdayCheckbox.setChecked(weekdays.contains("3"));
        thursdayCheckbox.setChecked(weekdays.contains("4"));
        fridayCheckbox.setChecked(weekdays.contains("5"));
        saturdayCheckbox.setChecked(weekdays.contains("6"));
        sundayCheckbox.setChecked(weekdays.contains("7"));
    }

    public void onSave(View view){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(HOUR, chosenHour);
        editor.putInt(MIN, chosenMin);

        editor.putString(TEXT, alarmText.getText().toString());
        editor.putBoolean(SOUND, soundSwitch.isChecked());

        List<String> weekdays = new ArrayList<>();
        if (mondayCheckbox.isChecked())
            weekdays.add("1");
        if (tuesdayCheckbox.isChecked())
            weekdays.add("2");
        if (wednesdayCheckbox.isChecked())
            weekdays.add("3");
        if (thursdayCheckbox.isChecked())
            weekdays.add("4");
        if (fridayCheckbox.isChecked())
            weekdays.add("5");
        if (saturdayCheckbox.isChecked())
            weekdays.add("6");
        if (sundayCheckbox.isChecked())
            weekdays.add("7");
        editor.putString(WEEKDAYS, TextUtils.join(";", weekdays));
        isAlarmActive = !weekdays.isEmpty();
        editor.commit();
        if (setAlarm())
            Toast.makeText(this, "Alarm on", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Alarm off", Toast.LENGTH_SHORT).show();
    }

    private int getAlarmCode(String alarm){
        int code = ALARM_1_CODE;
        switch (alarm) {
            case ALARM_1:
                code = ALARM_1_CODE;
                break;
            case ALARM_2:
                code = ALARM_2_CODE;
                break;
            case ALARM_3:
                code = ALARM_3_CODE;
                break;
            case ALARM_4:
                code = ALARM_4_CODE;
                break;
            case ALARM_5:
                code = ALARM_5_CODE;
                break;
        }
        return code;
    }

    private boolean setAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        int code = getAlarmCode(chosenAlarm);

        intent.putExtra("chosenAlarm", chosenAlarm);
        intent.putExtra("sound", soundSwitch.isChecked());
        intent.putExtra("text", alarmText.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, code, intent, 0);
        if (!isAlarmActive) {
            alarmManager.cancel(pendingIntent);
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 1;//sunday = 0, mon = 1...
        if (today == 0)
            today = 7;
        calendar.set(Calendar.HOUR_OF_DAY, chosenHour);
        calendar.set(Calendar.MINUTE, chosenMin);
        long time = calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000);//full minute
        boolean canRingToday = true;
        if (System.currentTimeMillis()>time)
            canRingToday = false;
        List<String> weekdays = getWeekdays();
        int noOfDays;
        if (canRingToday && weekdays.contains(String.valueOf(today))){
            noOfDays = 0;
        } else {
            noOfDays = getNumberOfDays(today, weekdays);
            if (noOfDays == 0)//!canRingToday
                noOfDays +=7;
        }
        time = time + noOfDays*AlarmManager.INTERVAL_DAY;

        if (Build.VERSION.SDK_INT>=19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        return true;
    }

    public void repeatAlarm(String chosenAlarm, boolean sound, String text){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        int code = getAlarmCode(chosenAlarm);
        pref = getSharedPreferences(chosenAlarm, Activity.MODE_PRIVATE);

        intent.putExtra("chosenAlarm", chosenAlarm);
        intent.putExtra("sound", sound);
        intent.putExtra("text", text);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, code, intent, 0);
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 1;//sunday = 0, mon = 1...
        if (today == 0)
            today = 7;
        int chosenHour = pref.getInt(HOUR, 0);
        int chosenMin = pref.getInt(MIN, 0);
        calendar.set(Calendar.HOUR_OF_DAY, chosenHour);
        calendar.set(Calendar.MINUTE, chosenMin);
        long time = calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000);//full minute
        boolean canRingToday = true;
        if (System.currentTimeMillis()>time)
            canRingToday = false;
        List<String> weekdays = getWeekdays();
        int noOfDays;
        if (canRingToday && weekdays.contains(String.valueOf(today))){
            noOfDays = 0;
        } else {
            noOfDays = getNumberOfDays(today, weekdays);
            if (noOfDays == 0)//!canRingToday
                noOfDays +=7;
        }
        time = time + noOfDays*AlarmManager.INTERVAL_DAY;

        if (Build.VERSION.SDK_INT>=19)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    public int getNumberOfDays(int today, List<String> weekdays){
        int day = today-1;//0-6
        for (int i = 0; i<=6; i++){
            int checkedDay = (day + i) % 7;//0-6
            if (weekdays.contains(String.valueOf(checkedDay+1)))
                return i;
        }
        return -1;//can't happen if everything is correct
    }

    private void initializeButtons(){
        Button alarm1 = findViewById(R.id.alarm1);
        Button alarm2 = findViewById(R.id.alarm2);
        Button alarm3 = findViewById(R.id.alarm3);
        Button alarm4 = findViewById(R.id.alarm4);
        Button alarm5 = findViewById(R.id.alarm5);

        alarm1.setText(getButtonText(ALARM_1));
        alarm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillWithChosenAlarm(ALARM_1);
            }
        });

        alarm2.setText(getButtonText(ALARM_2));
        alarm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillWithChosenAlarm(ALARM_2);
            }
        });

        alarm3.setText(getButtonText(ALARM_3));
        alarm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillWithChosenAlarm(ALARM_3);
            }
        });

        alarm4.setText(getButtonText(ALARM_4));
        alarm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillWithChosenAlarm(ALARM_4);
            }
        });

        alarm5.setText(getButtonText(ALARM_5));
        alarm5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillWithChosenAlarm(ALARM_5);
            }
        });
    }

    private String getButtonText(String alarm){
        boolean isActive;
        pref = getSharedPreferences(alarm, Activity.MODE_PRIVATE);
        int hour = pref.getInt(HOUR, 0);
        int min = pref.getInt(MIN, 0);
        String text = pref.getString(TEXT, "");
        String weekdays = pref.getString(WEEKDAYS, "");//1-7 with ; between
        isActive = !weekdays.equals("");
        StringBuilder bld = new StringBuilder();
        bld.append(String.format("%02d", hour));
        bld.append(":");
        bld.append(String.format("%02d", min));
        bld.append(" ");
        if (isActive)
            bld.append("on: ");
        else
            bld.append("off: ");
        bld.append(text);
        bld.append("\n");
        bld.append(weekdays);
        return bld.toString();
    }

    private List<String> getWeekdays(){
        String weekdays = pref.getString(WEEKDAYS, "");
        if (weekdays.equals("")){
            return new ArrayList<>();
        }else {
            return new ArrayList<>(Arrays.asList(weekdays.split(";")));
        }
    }

    public void setChosenHour(int hour){
        this.chosenHour = hour;
    }

    public void setChosenMin(int min){
        this.chosenMin = min;
    }
}
