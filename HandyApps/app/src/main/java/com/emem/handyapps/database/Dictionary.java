package com.emem.handyapps.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class Dictionary {
    public static final String TABLE_NAME = "dictionary";

    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ID = "id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT"
            + ")";

    private int id;
    private String name;

    public Dictionary(){
    }

    public Dictionary(int id, String name){
        this.id = id;
        this.name = name;
    }

    public static List<Dictionary> getDictionaries(DatabaseHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_NAME ;
        Cursor cursor = db.rawQuery(select, null);
        List<Dictionary> result = new ArrayList<>();
        if (cursor!=null) {
            if (cursor.moveToFirst())
                do {
                    Dictionary dictionary = new Dictionary();
                    dictionary.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    dictionary.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    result.add(dictionary);
                } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }

    public static long insertDictionary(DatabaseHelper dbHelper, String name){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int v){
        this.id = v;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String v){
        this.name = v;
    }

}
