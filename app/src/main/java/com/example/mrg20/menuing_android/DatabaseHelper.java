package com.example.mrg20.menuing_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "recipes_user";
    private static final String COL1 = "name";
    private static final String COL2 = "instructions";
    private static final String COL3 = "proportions";
    private static final String COL4 = "calories";
    private static final String COL5 = "sodium";
    private static final String COL6 = "fat";
    private static final String COL7 = "protein";
    private static final String COL8 = "urlPhoto";
    private static final String COL9 = "averagePuntuation";




    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT, " +
                COL3 +" TEXT, " +
                COL4 +" TEXT, " +
                COL5 +" TEXT, " +
                COL6 +" TEXT, " +
                COL7 +" TEXT, " +
                COL8 +" TEXT, " +
                COL9 +" TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Returns all the data from database
     * @return
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Delete from database
     * @param id
     */
    public void deleteName(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + "ID = '" + id + "'";

        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting from database.");
        db.execSQL(query);
    }

}
