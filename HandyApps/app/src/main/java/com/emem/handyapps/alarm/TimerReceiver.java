package com.emem.handyapps.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class TimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Timer alarm", Toast.LENGTH_LONG).show();
        Uri timerUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (timerUri == null)
            timerUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(context, timerUri);
        ringtone.play();
    }
}
