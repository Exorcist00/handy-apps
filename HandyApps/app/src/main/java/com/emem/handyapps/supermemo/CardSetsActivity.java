package com.emem.handyapps.supermemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.emem.handyapps.R;
import com.emem.handyapps.database.DatabaseHelper;
import com.emem.handyapps.database.MemoCardSet;

import java.util.ArrayList;
import java.util.List;

public class CardSetsActivity extends AppCompatActivity {
    List<MemoCardSet> memoCardSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_sets);
        ListView cardSetsList = findViewById(R.id.cardSetsList);
        DatabaseHelper dbHelper = new DatabaseHelper(getApplication());
        memoCardSets = MemoCardSet.getMemoCardSets(dbHelper);
        List<String> cardSets = new ArrayList<>();
        for (MemoCardSet s: memoCardSets) {
            cardSets.add(s.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cardSets);
        cardSetsList.setAdapter(adapter);
        cardSetsList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        goToCardPreview(position);
                    }
                }
        );
    }

    private void goToCardPreview(int pos){
        Intent intent = new Intent(this, CardPreviewActivity.class);
        intent.putExtra("id", memoCardSets.get(pos).getId());
        startActivity(intent);
    }
}
