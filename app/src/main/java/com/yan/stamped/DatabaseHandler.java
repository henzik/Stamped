package com.yan.stamped;

/**
 * Created by Henrik on 27/01/2015.
 */
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_SCHEMES = "schemes";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "Username";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_UID = "UserID";

    //Schemes Table Column Names
    private static final String SCHEME_ID = "SchemeID";
    private static final String SCHEME_NAME = "SchemeName";
    private static final String SCHEME_STAMPS = "StampsCurrent";
    private static final String SCHEME_TOTAL_STAMPS = "StampsForever";
    private static final String SCHEME_LEVEL = "Level";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_UID + " TEXT"+")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_SCHEMES_TABLE = "CREATE TABLE " + TABLE_SCHEMES + "("
                + SCHEME_ID + " INTEGER PRIMARY KEY,"
                + SCHEME_NAME + " TEXT,"
                + SCHEME_STAMPS + " INTEGER,"
                + SCHEME_TOTAL_STAMPS + " INTEGER,"
                + SCHEME_LEVEL + " STRING"+")";

        db.execSQL(CREATE_SCHEMES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email

        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("Username", cursor.getString(1));
            user.put("Email", cursor.getString(2));
            user.put("UserID", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }

}