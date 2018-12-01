package com.emem.handyapps.supermemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.emem.handyapps.R;
import com.emem.handyapps.database.DatabaseHelper;
import com.emem.handyapps.database.MemoCard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CardPreviewActivity extends AppCompatActivity {
    private List<MemoCard> cards;
    private int currentCardIndex;
    private MemoCard currentCard;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_preview);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            int setId = bundle.getInt("id");
            dbHelper = new DatabaseHelper(getApplication());
            cards = MemoCard.getMemoCards(dbHelper, setId);
        } else {
            Intent intent = new Intent(this, CardSetsActivity.class);
            startActivity(intent);
        }
        initializeRatingBar();
        initializeButtons();
        currentCardIndex = 0;
        fillWithCurrentCard();
    }

    private void initializeRatingBar(){
        RatingBar ratingBar = findViewById(R.id.cardRatingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                onRating((int)rating);
            }
        });
    }

    private void initializeButtons(){
        Button skipBtn = findViewById(R.id.cardSkipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });
    }

    private void skip(){
        currentCardIndex += 1;
        if (currentCardIndex == cards.size())
            currentCardIndex = 0;
        fillWithCurrentCard();
    }

    private void onRating(int rating){
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.add(Calendar.DATE, rating);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentCard.setDate(df.format(date.getTime()));
        MemoCard.updateMemoCardDate(dbHelper, currentCard);
        skip();
    }

    private void fillWithCurrentCard(){
        currentCard = cards.get(currentCardIndex);
        String type = currentCard.getType();
        switch (type){
            case MemoCard.TYPE_A:
                hideOrShowTexts(false, false);
                break;
            case MemoCard.TYPE_B:
                hideOrShowTexts(true, false);
                break;
            case MemoCard.TYPE_C:
                hideOrShowTexts(true, true);
                break;
        }
        fillTexts(currentCard.getTextA(), currentCard.getTextB(), currentCard.getTextC(), currentCard.getDate());
    }

    private void hideOrShowTexts(boolean showB, boolean showC){
        TextView textViewB = findViewById(R.id.cardPreviewB);
        TextView textViewC = findViewById(R.id.cardPreviewC);
        textViewB.setVisibility(showB ? View.VISIBLE: View.GONE);
        textViewC.setVisibility(showC ? View.VISIBLE: View.GONE);
    }

    private void fillTexts(String textA, String textB, String textC, String date){
        TextView textViewA = findViewById(R.id.cardPreviewA);
        TextView textViewB = findViewById(R.id.cardPreviewB);
        TextView textViewC = findViewById(R.id.cardPreviewC);
        TextView textViewDate = findViewById(R.id.cardPreviewDate);

        textViewA.setText(textA);
        textViewB.setText(textB);
        textViewC.setText(textC);
        textViewDate.setText("Planned review: " + date);
    }
}
