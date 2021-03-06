package com.example.mrg20.menuing_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

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
    private static final String COL10 = "idrecipe";
    private static final String COL11 = "yourRating";




    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 +" TEXT, " +
                COL2 +" TEXT, " +
                COL3 +" TEXT, " +
                COL4 +" TEXT, " +
                COL5 +" TEXT, " +
                COL6 +" TEXT, " +
                COL7 +" TEXT, " +
                COL8 +" TEXT, " +
                COL9 +" TEXT, " +
                COL10 +" TEXT, " +
                COL11 +" TEXT)";

        db.execSQL(createTable);
        System.out.println("DATABASE CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        System.out.println("DATABASE UPGRADE");
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        System.out.println("DATABASE DROPED");
        onCreate(db);
    }

    public void deleteFirstRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            String rowId = cursor.getString(0);
            db.delete(TABLE_NAME, "ID" + "=?",  new String[]{rowId});
        }
    }

    public long getNumOfRecipes(){
        SQLiteDatabase db = this.getWritableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return count;
    }

    public boolean addData(JSONObject item) {
        SQLiteDatabase db = this.getWritableDatabase();

        if(getNumOfRecipes() >= 20) {
            deleteFirstRow();
            System.out.println("DELETING FIRST ROW");
        }

        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(COL1, item.getString(COL1));
            contentValues.put(COL2, item.getString(COL2));
            contentValues.put(COL3, item.getString(COL3));
            contentValues.put(COL4, item.getString(COL4));
            contentValues.put(COL5, item.getString(COL5));
            contentValues.put(COL6, item.getString(COL6));
            contentValues.put(COL7, item.getString(COL7));
            contentValues.put(COL8, item.getString(COL8));
            contentValues.put(COL9, item.getString(COL9));
            contentValues.put(COL10, item.getString("id"));
        }catch (Exception e){
            System.out.println("FAILED ADDING TO DB");
            return false;
        }

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            System.out.println("OK ADDING");
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
     * ID IS THE POSITION IN THE LIST FROM 0 TO 19
     * @param id recipe id
     * @return
     */
    public Cursor getRecipeByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id;
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

    public void updateRate(String rate, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(COL11, rate);
        }catch (Exception e){
            System.out.println("FAILED UPDATING");
            return;
        }

        db.update(TABLE_NAME, contentValues, "ID="+id, null);
    }

}
