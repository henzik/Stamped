package com.yan.stamped;

/**
 * Created by Henrik on 27/01/2015.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_SCHEMES = "schemes";
    private static final String TABLE_REWARDS = "rewards";

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

    //Rewards Table Column Names
    private static final String REWARD_SCHEME = "SchemeID";
    private static final String REWARD_ID = "RewardID";
    private static final String REWARD_NAME = "Name";
    private static final String REWARD_DESC = "Description";
    private static final String REWARD_COST = "Cost";
    private static final String REWARD_REQUIREMENT = "Requirement";

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
                + SCHEME_TOTAL_STAMPS + " INTEGER"+")";
        db.execSQL(CREATE_SCHEMES_TABLE);

        String CREATE_REWARDS_TABLE = "CREATE TABLE " + TABLE_REWARDS + "("
                + REWARD_ID + " INTEGER PRIMARY KEY,"
                + REWARD_SCHEME + " INTEGER,"
                + REWARD_NAME + " TEXT,"
                + REWARD_DESC + " TEXT,"
                + REWARD_COST + " INTEGER,"
                + REWARD_REQUIREMENT + " INTEGER"+")";
        db.execSQL(CREATE_REWARDS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }


    public void addReward(String rid, String rscheme, String rname,
                          String rdesc, String rcost, String rreq) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(REWARD_ID, rid);
        values.put(REWARD_SCHEME, rscheme);
        values.put(REWARD_NAME, rname);
        values.put(REWARD_DESC, rdesc);
        values.put(REWARD_COST, rcost);
        values.put(REWARD_REQUIREMENT, rreq);
        // Inserting Row
        if(existReward(rid) != true) {
            db.insert(TABLE_REWARDS, null,values);
        } else {
            db.update(TABLE_REWARDS, values, REWARD_ID + " = " + rid, null);
        }

    }
    /**
     * Storing user details in database
     * */

    public void addScheme(String sid, String sname, String sstamps, String stotal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SCHEME_ID, sid); // Name
        values.put(SCHEME_NAME, sname); // Email
        values.put(SCHEME_STAMPS, sstamps); // Email
        values.put(SCHEME_TOTAL_STAMPS, stotal); // Email

        // Inserting Row
        if(existScheme(sid) != true) {
            db.insert(TABLE_SCHEMES, null,values);
        } else {
            db.update(TABLE_SCHEMES, values, SCHEME_ID + " = " + sid, null);
        }

        db.close(); // Closing database connection
    }

    private boolean existScheme(String sid) {
        String selectQuery = "SELECT SchemeID FROM " + TABLE_SCHEMES+ " WHERE SchemeID = " + sid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            return true;
        } else {
            return false;
        }
    }

    private boolean existReward(String sid) {
        String selectQuery = "SELECT RewardID FROM " + TABLE_REWARDS+ " WHERE RewardID = " + sid;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            return true;
        } else {
            return false;
        }
    }

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

    public HashMap<String, String> getScheme(int id){
        HashMap<String,String> user = new HashMap<String,String>();
        String selectQuery = "SELECT SchemeID, SchemeName, StampsCurrent, StampsForever FROM " + TABLE_SCHEMES + " WHERE SchemeID =" +id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("SchemeID", cursor.getString(1));
            user.put("SchemeName", cursor.getString(2));
            user.put("StampsCurrent", cursor.getString(3));
            user.put("StampsForever", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }

    public ArrayList<String> getSchemeNames(){
        ArrayList<String> schemeArray = new ArrayList<String>();
        String selectQuery = "SELECT SchemeName FROM " + TABLE_SCHEMES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        int i = getSchemeCount();
        for(int x=0;x<i;x++) {
            schemeArray.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return user
        String[] stringArray = schemeArray.toArray(new String[schemeArray.size()]);
        return schemeArray;
    }

    public List<Map<String , String>> getRewards(){
        String selectQuery = "SELECT Cost, SchemeID, Name, Description FROM " + TABLE_REWARDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        HashMap<String,String> user = new HashMap<String,String>();
        List<Map<String , String>> myMap  = new ArrayList<Map<String,String>>();

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            int i = getRewardCount();
            for(int x=0;x<i;x++) {
                user.put("Cost", cursor.getString(0));
                user.put("SchemeID", cursor.getString(1));
                user.put("Name", cursor.getString(2));
                user.put("Description", cursor.getString(3));
                myMap.add(x,user);
                cursor.moveToNext();
                user = new HashMap<String,String>();
            }
        }
        cursor.close();
        db.close();
        // return user
        Log.e("DEBUG",myMap.size() + "");
        return myMap;
    }


    public List<Map<String , String>> getRewardsBySchemeID(int id){
        String selectQuery = "SELECT Cost, SchemeID, Name, Description FROM " + TABLE_REWARDS + " WHERE SchemeID = "+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        HashMap<String,String> user = new HashMap<String,String>();
        List<Map<String , String>> myMap  = new ArrayList<Map<String,String>>();

        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            int i = getRewardCount();
            for(int x=0;x<i;x++) {
                user.put("Cost", cursor.getString(0));
                user.put("SchemeID", cursor.getString(1));
                user.put("Name", cursor.getString(2));
                user.put("Description", cursor.getString(3));
                myMap.add(x,user);
            }
        }
        cursor.close();
        db.close();
        // return user
        return myMap;
    }


    public ArrayList<String> getAvailableRewards(){
        ArrayList<String> schemeArray = new ArrayList<String>();
        String selectQuery = "SELECT Cost, SchemeID, Name, Description FROM " + TABLE_REWARDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        int i = getRewardCount();
        for(int x=0;x<i;x++) {
            int price = cursor.getInt(0);
            int c = cursor.getInt(1);
            int cost = Integer.parseInt(getScheme(c).get("SchemeName"));
            Log.e("AVAILABLE", cost+ " " + getScheme(c).get("SchemeName"));
            if(price <= cost) {
                schemeArray.add(cursor.getString(2)+ ": " + price +" stamps");
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return user
        String[] stringArray = schemeArray.toArray(new String[schemeArray.size()]);
        return schemeArray;
    }

    public ArrayList<Integer> getRewardsCosts(){
        ArrayList<Integer> schemeArray = new ArrayList<Integer>();
        String selectQuery = "SELECT Cost, SchemeID, Name FROM " + TABLE_REWARDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        int i = getRewardCount();
        for(int x=0;x<i;x++) {
            int actualPrice = cursor.getInt(0);
            int c = cursor.getInt(1);
            int cost = Integer.parseInt(getScheme(c).get("SchemeName"));
            Log.e("AVAILABLE", cost+ " " + getScheme(c).get("SchemeName"));
            if(actualPrice <= cost) {
                schemeArray.add(actualPrice);
            }
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return user
        //String[] stringArray = schemeArray.toArray(new String[schemeArray.size()]);
        return schemeArray;
    }

    public ArrayList<Integer> getSchemeCurrentStamps(){
        ArrayList<Integer> schemeArray = new ArrayList<Integer>();
        String selectQuery = "SELECT StampsCurrent FROM " + TABLE_SCHEMES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        int i = getSchemeCount();
        for(int x=0;x<i;x++) {
            schemeArray.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return user
        Integer[] intArray = schemeArray.toArray(new Integer[schemeArray.size()]);
        return schemeArray;
    }

    public Integer getStampScore() {
        int counting = 0;
        String selectQuery = "SELECT StampsForever FROM " + TABLE_SCHEMES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        int i = getSchemeCount();
        for(int x=0;x<i;x++) {
            counting = counting +(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        // return user
        return counting*100;
    }

    public String[] testme() {
        String[] blah = {"1","2"};
        return blah;
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

    public int getSchemeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SCHEMES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    public int getRewardCount() {
        String countQuery = "SELECT  * FROM " + TABLE_REWARDS;
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

    public void resetSchemes(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_SCHEMES, null, null);
        db.close();
    }

}