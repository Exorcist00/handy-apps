package com.emem.handyapps.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String alarm = extras.getString("chosenAlarm");
        boolean sound = extras.getBoolean("sound");
        String text = extras.getString("text");
        new AlarmClockActivity().repeatAlarm(alarm, sound, text);

        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        if (sound) {
            Uri timerUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (timerUri == null)
                timerUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(context, timerUri);
            ringtone.play();
        }
    }
}
