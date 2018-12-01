package com.emem.handyapps.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "handy_apps_db";
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Unit.CREATE_TABLE);
        db.execSQL(Dictionary.CREATE_TABLE);
        db.execSQL(DictionaryEntry.CREATE_TABLE);
        db.execSQL(MemoCardSet.CREATE_TABLE);
        db.execSQL(MemoCard.CREATE_TABLE);
        initializeUnits();
        long dictId = Dictionary.insertDictionary(this, "eng-pl");
        initializeDictionaryEntries(dictId);
        long setId = MemoCardSet.insertMemoCardSet(this, "test");
        initializeMemoCards(setId);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_IF_EXISTS + Unit.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + Dictionary.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + DictionaryEntry.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + MemoCardSet.TABLE_NAME);
        db.execSQL(DROP_TABLE_IF_EXISTS + MemoCard.TABLE_NAME);
        onCreate(db);
    }

    private void initializeUnits(){
        Unit.insertUnit(this,"PLN", Unit.CURRENCY, 1.0);
        Unit.insertUnit(this,"EUR", Unit.CURRENCY, 4.31);
        Unit.insertUnit(this,"USD", Unit.CURRENCY, 3.72);
        Unit.insertUnit(this,"JPY", Unit.CURRENCY, 0.0335);

        Unit.insertUnit(this,"M", Unit.LENGTH, 1.0);
        Unit.insertUnit(this,"CM", Unit.LENGTH, 0.01);
        Unit.insertUnit(this,"INCH", Unit.LENGTH, 0.0254);
        Unit.insertUnit(this,"FEET", Unit.LENGTH, 0.3048);
        Unit.insertUnit(this,"KM", Unit.LENGTH, 1000.0);

        Unit.insertUnit(this,"SQ M", Unit.AREA, 1.0);
        Unit.insertUnit(this,"SQ FEET", Unit.AREA, 0.0929);

        Unit.insertUnit(this,"L", Unit.VOLUME, 1.0);
        Unit.insertUnit(this,"GALLON", Unit.VOLUME, 3.7854);
        Unit.insertUnit(this,"CUBIC M", Unit.VOLUME, 1000.0);
        Unit.insertUnit(this,"PINT", Unit.VOLUME, 0.473);

        Unit.insertUnit(this,"KG", Unit.WEIGHT, 1.0);
        Unit.insertUnit(this,"OUNCE", Unit.WEIGHT, 0.0283);
        Unit.insertUnit(this,"POUND", Unit.WEIGHT, 0.454);
    }

    private void initializeDictionaryEntries(long id){
        DictionaryEntry.insertDictionaryEntry( this, "home", "dom", (int)id);
        DictionaryEntry.insertDictionaryEntry( this, "family", "rodzina", (int)id);
        DictionaryEntry.insertDictionaryEntry( this, "job", "praca, zadanie", (int)id);
        DictionaryEntry.insertDictionaryEntry( this, "raven", "kruk", (int)id);
        DictionaryEntry.insertDictionaryEntry( this, "resin", "żywica", (int)id);
        DictionaryEntry.insertDictionaryEntry( this, "tawny", "śniady, płowy, żółtobrązowy", (int)id);
        DictionaryEntry.insertDictionaryEntry( this, "spyglass", "lornetka, mała luneta", (int)id);
        DictionaryEntry.insertDictionaryEntry( this, "parsnip", "pasternak", (int)id);
    }

    private void initializeMemoCards(long id){
        MemoCard.insertMemoCard(this, MemoCard.TYPE_A, "\"I know not with what weapons World War III will be fought, but World War IV will be fought with sticks and stones.\"― Albert Einstein", "?", "?", "2018-09-27", (int)id);
        MemoCard.insertMemoCard(this, MemoCard.TYPE_B, "A", "B", "?", "2018-09-25", (int)id);
        MemoCard.insertMemoCard(this, MemoCard.TYPE_C, "X", "Y", "Z", "2018-09-26", (int)id);
    }
}
