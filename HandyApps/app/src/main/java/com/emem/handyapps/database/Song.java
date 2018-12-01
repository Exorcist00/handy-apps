package com.emem.handyapps.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class Song {
    public static final String TABLE_NAME = "song";

    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PLAYLIST_ID = "playlist_id";
    private static final String COLUMN_ID = "id";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_PLAYLIST_ID + " INTEGER"
            + ")";

    private int id;
    private String name;
    private int playlistId;

    public Song(){
    }

    public Song(int id, String name, int playlistId){
        this.id = id;
        this.name = name;
        this.playlistId = playlistId;
    }

    public static List<Song> getSongs(DatabaseHelper dbHelper, int playlistId){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PLAYLIST_ID},
                COLUMN_PLAYLIST_ID + "=?",
                new String[]{String.valueOf(playlistId)}, null, null, null);
        List<Song> result = new ArrayList<>();
        if (cursor!=null) {
            if (cursor.moveToFirst())
                do {
                    Song song = new Song();
                    song.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    song.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                    song.setPlaylistId(cursor.getInt(cursor.getColumnIndex(COLUMN_PLAYLIST_ID)));
                    result.add(song);
                } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return result;
    }

    public static long insertSong(DatabaseHelper dbHelper, String name, int playlistId){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PLAYLIST_ID, playlistId);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }
}
