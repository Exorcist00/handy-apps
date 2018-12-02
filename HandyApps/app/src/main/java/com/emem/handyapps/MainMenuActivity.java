package com.emem.handyapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.emem.handyapps.alarm.AlarmClockActivity;
import com.emem.handyapps.alarm.StopwatchActivity;
import com.emem.handyapps.alarm.TimerActivity;
import com.emem.handyapps.browser.BrowserTabListActivity;
import com.emem.handyapps.dictionary.DictionaryActivity;
import com.emem.handyapps.gallery.GalleryActivity;
import com.emem.handyapps.notes.NotesListActivity;
import com.emem.handyapps.player.MusicPlayerActivity;
import com.emem.handyapps.supermemo.CardSetsActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button notesButton = findViewById(R.id.notesButton);
        Button superMemoButton = findViewById(R.id.superMemoButton);
        superMemoButton.setWidth(notesButton.getWidth());
    }

    public void goToAlarm(View view){
        Intent intent = new Intent(this, AlarmClockActivity.class);
        startActivity(intent);
    }

    public void goToTimer(View view){
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
    }

    public void goToStopwatch(View view){
        Intent intent = new Intent(this, StopwatchActivity.class);
        startActivity(intent);
    }

    public void goToBrowser(View view){
        Intent intent = new Intent(this, BrowserTabListActivity.class);
        startActivity(intent);
    }

    public void goToDictionary(View view){
        Intent intent = new Intent(this, DictionaryActivity.class);
        startActivity(intent);
    }

    public void goToGallery(View view){
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    public void goToNotes(View view){
        Intent intent = new Intent(this, NotesListActivity.class);
        startActivity(intent);
    }

    public void goToPlayer(View view){
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        startActivity(intent);
    }

    public void goToSupermemo(View view){
        Intent intent = new Intent(this, CardSetsActivity.class);
        startActivity(intent);
    }

    public void goToConverter(View view){
        Intent intent = new Intent(this, ConverterActivity.class);
        startActivity(intent);
    }

    public void goToFileManager(View view){
        Intent intent = new Intent(this, FileManagerActivity.class);
        startActivity(intent);
    }

    public void goToReader(View view){
        Intent intent = new Intent(this, ReaderActivity.class);
        startActivity(intent);
    }
}
