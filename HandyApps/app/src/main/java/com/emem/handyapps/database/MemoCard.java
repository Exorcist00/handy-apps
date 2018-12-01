package com.emem.handyapps.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class MemoCard {
    public static final String TABLE_NAME = "memo_card";
    public static final String TYPE_A = "A";//A
    public static final String TYPE_B = "B";//A,B
    public static final String TYPE_C = "C";//A,B,C

    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_A = "text_A";
    private static final String COLUMN_B = "text_B";
    private static final String COLUMN_C = "text_C";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_SET_ID = "set_id";
    private static final String COLUMN_ID = "id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_A + " TEXT,"
            + COLUMN_B + " TEXT,"
            + COLUMN_C + " TEXT,"
            + COLUMN_SET_ID + " INTEGER"
            + ")";

    private int id;
    private String type;
    private String textA;
    private String textB;
    private String textC;
    private String date;//2018-09-14
    private int setId;

    public MemoCard(){
    }

    public MemoCard(int id, String type, String textA, String textB, String textC, String date, int setId){
        this.id = id;
        this.type = type;
        this.textA = textA;
        this.textB = textB;
        this.textC = textC;
        this.date = date;
        this.setId = setId;
    }

    public static List<MemoCard> getMemoCards(DatabaseHelper dbHelper, int setId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_TYPE, COLUMN_A, COLUMN_B, COLUMN_C, COLUMN_DATE, COLUMN_SET_ID},
                COLUMN_SET_ID + "=?",
                new String[]{String.valueOf(setId)}, null, null, COLUMN_DATE + "ASC");
        List<MemoCard> result = new ArrayList<>();
        if (cursor!=null) {
            if (cursor.moveToFirst())
                do {
                    MemoCard memoCard = new MemoCard();
                    memoCard.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    memoCard.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                    memoCard.setTextA(cursor.getString(cursor.getColumnIndex(COLUMN_A)));
                    memoCard.setTextB(cursor.getString(cursor.getColumnIndex(COLUMN_B)));
                    memoCard.setTextC(cursor.getString(cursor.getColumnIndex(COLUMN_C)));
                    memoCard.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                    memoCard.setSetId(cursor.getInt(cursor.getColumnIndex(COLUMN_SET_ID)));
                    result.add(memoCard);
                } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }

    public static long insertMemoCard(DatabaseHelper dbHelper, String type, String textA, String textB, String textC, String date, int setId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_A, textA);
        values.put(COLUMN_B, textB);
        values.put(COLUMN_C, textC);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_SET_ID, setId);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public static int updateMemoCardDate(DatabaseHelper dbHelper, MemoCard card) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, card.getDate());

        // updating row
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(card.getId())});
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTextA() {
        return textA;
    }

    public void setTextA(String textA) {
        this.textA = textA;
    }

    public String getTextB() {
        return textB;
    }

    public void setTextB(String textB) {
        this.textB = textB;
    }

    public String getTextC() {
        return textC;
    }

    public void setTextC(String textC) {
        this.textC = textC;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
