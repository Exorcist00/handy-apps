package com.emem.handyapps.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class Unit {
    public static final String TABLE_NAME = "unit";

    public static final String CURRENCY = "currency";
    public static final String LENGTH = "length";
    public static final String AREA = "area";
    public static final String VOLUME = "volume";
    public static final String WEIGHT = "weight";

    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_ID = "id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE + " TEXT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_VALUE + " DOUBLE(10,5)"
            + ")";

    private int id;
    private String name;
    private String type;
    private Double value;

    public Unit(){
    }

    public Unit(int id, String name, Double value){
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public static List<Unit> getUnits(DatabaseHelper dbHelper, String type){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_TYPE, COLUMN_NAME, COLUMN_VALUE},
                COLUMN_TYPE + "=?",
                new String[]{type}, null, null, null);
        List<Unit> result = new ArrayList<>();
        if (cursor!=null) {
            if (cursor.moveToFirst())
                do {
                    Unit unit = new Unit();
                    unit.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    unit.setType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                    unit.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    unit.setValue(cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE)));
                    result.add(unit);
                } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }

    public static long insertUnit(DatabaseHelper dbHelper, String name, String type, Double value){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_VALUE, value);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }
    /*public int updateNote(Note note) {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(Note.COLUMN_NOTE, note.getNote());

    // updating row
    return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
            new String[]{String.valueOf(note.getId())});
}*/

    /*public void deleteNote(Note note) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
            new String[]{String.valueOf(note.getId())});
    db.close();
}*/

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

    public String getType(){
        return this.type;
    }

    public void setType(String v){
        this.type = v;
    }

    public Double getValue(){
        return this.value;
    }

    public void setValue(Double v){
        this.value = v;
    }
}
