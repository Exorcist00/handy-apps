package com.emem.handyapps.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class DictionaryEntry {
    public static final String TABLE_NAME = "dictionary_entry";

    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_MEANING = "value";
    private static final String COLUMN_DICT_ID = "dict_id";
    private static final String COLUMN_ID = "id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_WORD + " TEXT,"
            + COLUMN_MEANING + " TEXT,"
            + COLUMN_DICT_ID + " INTEGER"
            + ")";

    private int id;
    private String word;
    private String meaning;
    private int dictId;

    public DictionaryEntry(){
    }

    public DictionaryEntry(int id, String word, String meaning, int dictId){
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.dictId = dictId;
    }

    public static List<DictionaryEntry> getDictionaryEntries(DatabaseHelper dbHelper, int dictId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_WORD, COLUMN_MEANING, COLUMN_DICT_ID},
                COLUMN_DICT_ID + "=?",
                new String[]{String.valueOf(dictId)}, null, null, COLUMN_WORD + "ASC");
        List<DictionaryEntry> result = new ArrayList<>();
        if (cursor!=null) {
            if (cursor.moveToFirst())
                do {
                    DictionaryEntry dictionaryEntry = new DictionaryEntry();
                    dictionaryEntry.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    dictionaryEntry.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
                    dictionaryEntry.setMeaning(cursor.getString(cursor.getColumnIndex(COLUMN_MEANING)));
                    dictionaryEntry.setDictId(cursor.getInt(cursor.getColumnIndex(COLUMN_DICT_ID)));
                    result.add(dictionaryEntry);
                } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }

    public static List<DictionaryEntry> searchDictionaryEntries(DatabaseHelper dbHelper, int dictId, String text){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_WORD, COLUMN_MEANING, COLUMN_DICT_ID},
                COLUMN_DICT_ID + "=? AND " + COLUMN_WORD + " LIKE ?",
                new String[]{String.valueOf(dictId), "%"+text+"%"}, null, null, COLUMN_WORD + "ASC");
        List<DictionaryEntry> result = new ArrayList<>();
        if (cursor!=null) {
            if (cursor.moveToFirst())
                do {
                    DictionaryEntry dictionaryEntry = new DictionaryEntry();
                    dictionaryEntry.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    dictionaryEntry.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
                    dictionaryEntry.setMeaning(cursor.getString(cursor.getColumnIndex(COLUMN_MEANING)));
                    dictionaryEntry.setDictId(cursor.getInt(cursor.getColumnIndex(COLUMN_DICT_ID)));
                    result.add(dictionaryEntry);
                } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }

    public static long insertDictionaryEntry(DatabaseHelper dbHelper, String word, String meaning, int dictId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, word);
        values.put(COLUMN_MEANING, meaning);
        values.put(COLUMN_DICT_ID, dictId);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getDictId() {
        return dictId;
    }

    public void setDictId(int dictId) {
        this.dictId = dictId;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int v){
        this.id = v;
    }
}
