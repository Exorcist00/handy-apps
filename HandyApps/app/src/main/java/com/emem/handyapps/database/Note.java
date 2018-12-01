package com.emem.handyapps.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.emem.handyapps.NoteType;

import java.util.ArrayList;
import java.util.List;


public class Note {
    public static final String TABLE_NAME = "note";

    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ORDINAL_NO = "ordinal_no";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_SET_ID = "set_id";
    private static final String COLUMN_NOTE_ID = "note_id";
    private static final String COLUMN_ID = "id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TYPE + " INTEGER,"
            + COLUMN_ORDINAL_NO + " INTEGER,"
            + COLUMN_TEXT + " TEXT,"
            + COLUMN_VALUE + " REAL,"
            + COLUMN_NOTE_ID + " INTEGER,"
            + COLUMN_SET_ID + " INTEGER"
            + ")";

    private int id;
    private NoteType type;
    private int ordinal;
    private String text;
    private Double value;
    private int noteId;
    private int setId;

    public Note(){
    }

    public Note(int id, NoteType type, int ordinal, String text, Double value, int noteId, int setId){
        this.id = id;
        this.type = type;
        this.ordinal = ordinal;
        this.text = text;
        this.value = value;
        this.noteId = noteId;
        this.setId = setId;
    }

    public static List<Note> getNotes(DatabaseHelper dbHelper, int setId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_TYPE, COLUMN_ORDINAL_NO, COLUMN_TEXT, COLUMN_VALUE, COLUMN_NOTE_ID, COLUMN_SET_ID},
                COLUMN_SET_ID + "=?",
                new String[]{String.valueOf(setId)}, null, null, null);
        List<Note> result = new ArrayList<>();
        if (cursor!=null) {
            if (cursor.moveToFirst())
                do {
                    Note note = new Note();
                    note.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    note.setType(NoteType.getNoteType(cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE))));
                    note.setOrdinal(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDINAL_NO)));
                    note.setText(cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)));
                    note.setValue(cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE)));
                    note.setNoteId(cursor.getInt(cursor.getColumnIndex(COLUMN_NOTE_ID)));
                    note.setSetId(cursor.getInt(cursor.getColumnIndex(COLUMN_SET_ID)));
                    result.add(note);
                } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }

    public static long insertNote(DatabaseHelper dbHelper, NoteType type, int ordinal, String text, Double value, int noteId, int setId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type.getType());
        values.put(COLUMN_ORDINAL_NO, ordinal);
        values.put(COLUMN_TEXT, text);
        values.put(COLUMN_VALUE, value);
        values.put(COLUMN_NOTE_ID, noteId);
        values.put(COLUMN_SET_ID, setId);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /*public static int updateMemoCardDate(DatabaseHelper dbHelper, Note card) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, card.getDate());

        // updating row
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(card.getId())});
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NoteType getType() {
        return type;
    }

    public void setType(NoteType type) {
        this.type = type;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }
}
