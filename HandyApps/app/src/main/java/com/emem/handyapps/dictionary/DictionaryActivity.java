package com.emem.handyapps.dictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.emem.handyapps.R;
import com.emem.handyapps.database.DatabaseHelper;
import com.emem.handyapps.database.Dictionary;
import com.emem.handyapps.database.DictionaryEntry;

import java.util.ArrayList;
import java.util.List;

public class DictionaryActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private List<Dictionary> dictionaries;
    private List<DictionaryEntry> entries;
    private ListView dictionaryEntries;
    private Dictionary selectedDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        dictionaryEntries = findViewById(R.id.dictionaryEntries);
        dbHelper = new DatabaseHelper(getApplication());
        dictionaries = Dictionary.getDictionaries(dbHelper);
        initializeDictionarySelectorSpinner();
        initializeSearchButton();
    }

    private void initializeDictionarySelectorSpinner(){
        Spinner dictionarySpinner = findViewById(R.id.dictionary);
        dictionarySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        onDictionarySelect(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        List<String> dictList = new ArrayList<>();
        for (Dictionary d: dictionaries) {
            dictList.add(d.getName());
        }

        ArrayAdapter<String> dictAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dictList);
        dictionarySpinner.setAdapter(dictAdapter);
    }

    private void initializeSearchButton(){
        Button btn = findViewById(R.id.dictSearchBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearch();
            }
        });
    }

    private void onDictionarySelect(int position){
        selectedDictionary = dictionaries.get(position);
        entries = DictionaryEntry.getDictionaryEntries(dbHelper, selectedDictionary.getId());

        dictionaryEntries.setAdapter(new DictionaryEntryAdapter(this, entries));
    }

    private void onSearch(){
        if (selectedDictionary == null)
            return;
        EditText editText = findViewById(R.id.dictionarySearch);
        String text = editText.getText().toString();
        if (!text.isEmpty()) {
            entries = DictionaryEntry.searchDictionaryEntries(dbHelper, selectedDictionary.getId(), text);
        } else {
            entries = DictionaryEntry.getDictionaryEntries(dbHelper, selectedDictionary.getId());
        }
        dictionaryEntries.setAdapter(new DictionaryEntryAdapter(this, entries));
    }
}
