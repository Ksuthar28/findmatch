package com.saadi.findmatch.utils;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kailash Suthar.
 */

public class Databases {
    //variable for database name...
    private static final String DATABASE_NAME = "MATCHES_DB";
    //variable for database version...
    private static final int DATABASE_VERSION = 1;

    //table name
    private static final String TABLE_MATCHES = "matches";

    //table fields
    private static final String ID = "_id";
    private static final String MOBILE_NUMBER = "mobile_number";
    private static final String TITLE = "title";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String AGE = "age";
    private static final String GENDER = "gender";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String PICTURE = "picture";
    private static final String STATUS = "status";

    public Cursor c;

    //for create table
    private static final String TABLE_CREATE_MATCHES = "CREATE TABLE IF NOT EXISTS " + TABLE_MATCHES + " ( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + MOBILE_NUMBER + " VARCHAR, " + TITLE + " VARCHAR, " + FIRST_NAME + " VARCHAR, " + LAST_NAME + " VARCHAR, "
            + AGE + " VARCHAR, " + GENDER + " VARCHAR, " + CITY + " VARCHAR, " + COUNTRY + " VARCHAR, " + PICTURE + " VARCHAR, " + STATUS + " VARCHAR);";


    //for call database
    private SQLiteDatabase db;

    private Context context;

    private DatabaseOpenHelper dbHelper;

    //set database...
    public Databases(Context context) {
        this.context = context;
        dbHelper = new DatabaseOpenHelper(context);
        db = dbHelper.getWritableDatabase();
    }


    public void insertMatchData(String mobile_number, String title, String first, String last,
                                String age, String gender, String city, String country,
                                String picture, String status) {
        ContentValues content = new ContentValues();
        content.put(MOBILE_NUMBER, mobile_number);
        content.put(TITLE, title);
        content.put(FIRST_NAME, first);
        content.put(LAST_NAME, last);
        content.put(AGE, age);
        content.put(GENDER, gender);
        content.put(CITY, city);
        content.put(COUNTRY, country);
        content.put(PICTURE, picture);
        content.put(STATUS, status);

        db.insert(TABLE_MATCHES, null, content);
    }

    public void updateStatus(String mobile_number, String status) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS, status);
        db.update(TABLE_MATCHES, values, MOBILE_NUMBER + " =? ", new String[]{mobile_number});
    }

    public boolean checkUniqueMatch(String mobile_number) {
        db = dbHelper.getWritableDatabase();
        String[] columns = new String[]{MOBILE_NUMBER};
        c = db.query(TABLE_MATCHES, columns, MOBILE_NUMBER + " =?", new String[]{mobile_number}, null, null, null, null);
        return !c.moveToFirst();
    }


    //get table data...
    public Cursor getMatchData() {
        String[] columns = new String[]{MOBILE_NUMBER, TITLE, FIRST_NAME, LAST_NAME, AGE, GENDER, CITY, COUNTRY, PICTURE, STATUS};
        return db.query(TABLE_MATCHES, columns, null, null, null, null, null, null);
    }

    //delete  table data...
    public void deleteMatchData() {
        db.delete(TABLE_MATCHES, null, null);
    }

    public Databases openDatabase() {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    //close database
    public void closeDatabase() {
        dbHelper.close();
    }


    public static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private DatabaseOpenHelper(Context aContext) {
            super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //create table
            db.execSQL(TABLE_CREATE_MATCHES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }


}

