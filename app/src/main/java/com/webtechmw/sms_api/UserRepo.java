package com.webtechmw.sms_api;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mangochiman on 9/2/17.
 */

public class UserRepo {
    private DBHelper dbHelper;

    public UserRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(User user) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.KEY_username, user.username);
        values.put(User.KEY_first_name, user.first_name);
        values.put(User.KEY_last_name, user.last_name);
        values.put(User.KEY_phone_number, user.phone_number);
        values.put(User.KEY_email, user.email);
        values.put(User.KEY_api_key, user.api_key);
        values.put(User.KEY_api_key_status, user.api_key_status);
        values.put(User.KEY_api_key_expiry_date, user.api_key_expiry_date);
        values.put(User.KEY_created_at, user.created_at);

        // Inserting Row
        long user_Id = db.insert(User.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) user_Id;
    }

    public void delete(int user_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // It's a good practice to use parameter ?, instead of concatenate string
        db.delete(User.TABLE, User.KEY_ID + "= ?", new String[] { String.valueOf(user_Id) });
        db.close(); // Closing database connection
    }

    public void update(User user) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(User.KEY_username, user.username);
        values.put(User.KEY_first_name, user.first_name);
        values.put(User.KEY_last_name, user.last_name);
        values.put(User.KEY_phone_number, user.phone_number);
        values.put(User.KEY_email, user.email);
        values.put(User.KEY_api_key, user.api_key);
        values.put(User.KEY_api_key_status, user.api_key_status);
        values.put(User.KEY_api_key_expiry_date, user.api_key_expiry_date);
        values.put(User.KEY_created_at, user.created_at);
        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(User.TABLE, values, User.KEY_username + "= ?", new String[] { String.valueOf(user.username) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getUserList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                User.KEY_ID + "," +
                User.KEY_username + "," +
                User.KEY_first_name + "," +
                User.KEY_last_name + "," +
                User.KEY_phone_number + "," +
                User.KEY_email + "," +
                User.KEY_api_key_status + "," +
                User.KEY_api_key_expiry_date + "," +
                User.KEY_created_at + "," +
                User.KEY_api_key +
                " FROM " + User.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> userList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> user = new HashMap<String, String>();
                user.put("id", cursor.getString(cursor.getColumnIndex(User.KEY_ID)));
                user.put("username", cursor.getString(cursor.getColumnIndex(User.KEY_username)));
                user.put("first_name", cursor.getString(cursor.getColumnIndex(User.KEY_first_name)));
                user.put("last_name", cursor.getString(cursor.getColumnIndex(User.KEY_last_name)));
                user.put("phone_number", cursor.getString(cursor.getColumnIndex(User.KEY_phone_number)));
                user.put("email", cursor.getString(cursor.getColumnIndex(User.KEY_email)));
                user.put("api_key", cursor.getString(cursor.getColumnIndex(User.KEY_api_key)));
                userList.add(user);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;

    }

    public User getUserById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                User.KEY_ID + "," +
                User.KEY_username + "," +
                User.KEY_first_name + "," +
                User.KEY_last_name + "," +
                User.KEY_phone_number + "," +
                User.KEY_email + "," +
                User.KEY_api_key_status + "," +
                User.KEY_api_key_expiry_date + "," +
                User.KEY_created_at + "," +
                User.KEY_api_key +
                " FROM " + User.TABLE
                + " WHERE " +
                User.KEY_ID + "=?";// It's a good practice to use parameter ?, instead of concatenate string

        int iCount =0;
        User user = new User();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                user.user_ID =cursor.getInt(cursor.getColumnIndex(User.KEY_ID));
                user.username =cursor.getString(cursor.getColumnIndex(User.KEY_username));
                user.first_name  =cursor.getString(cursor.getColumnIndex(User.KEY_first_name));
                user.last_name  =cursor.getString(cursor.getColumnIndex(User.KEY_last_name));
                user.phone_number  =cursor.getString(cursor.getColumnIndex(User.KEY_phone_number));
                user.email  =cursor.getString(cursor.getColumnIndex(User.KEY_email));
                user.api_key  =cursor.getString(cursor.getColumnIndex(User.KEY_api_key));
                user.api_key_status  =cursor.getString(cursor.getColumnIndex(User.KEY_api_key_status));
                user.api_key_expiry_date  =cursor.getString(cursor.getColumnIndex(User.KEY_api_key_expiry_date));
                user.created_at  = cursor.getString(cursor.getColumnIndex(User.KEY_created_at));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return user;
    }

    public int getUserCount() {
        String countQuery = "SELECT  * FROM User";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }

    public User getUser(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                User.KEY_ID + "," +
                User.KEY_username + "," +
                User.KEY_first_name + "," +
                User.KEY_last_name + "," +
                User.KEY_phone_number + "," +
                User.KEY_email + "," +
                User.KEY_api_key_status + "," +
                User.KEY_api_key_expiry_date + "," +
                User.KEY_created_at + "," +
                User.KEY_api_key +
                " FROM  " + User.TABLE;
        Log.e("UQUERY", selectQuery);
        User user = new User();

        Cursor cursor = db.rawQuery(selectQuery, null );

        if (cursor.moveToFirst()) {
            do {
                user.user_ID = cursor.getInt(cursor.getColumnIndex(User.KEY_ID));
                user.username = cursor.getString(cursor.getColumnIndex(User.KEY_username));
                user.first_name  = cursor.getString(cursor.getColumnIndex(User.KEY_first_name));
                user.last_name  = cursor.getString(cursor.getColumnIndex(User.KEY_last_name));
                user.phone_number  = cursor.getString(cursor.getColumnIndex(User.KEY_phone_number));
                user.email  = cursor.getString(cursor.getColumnIndex(User.KEY_email));
                user.api_key  = cursor.getString(cursor.getColumnIndex(User.KEY_api_key));
                user.api_key_status  = cursor.getString(cursor.getColumnIndex(User.KEY_api_key_status));
                user.api_key_expiry_date  = cursor.getString(cursor.getColumnIndex(User.KEY_api_key_expiry_date));
                user.created_at  = cursor.getString(cursor.getColumnIndex(User.KEY_created_at));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return user;
    }
}
