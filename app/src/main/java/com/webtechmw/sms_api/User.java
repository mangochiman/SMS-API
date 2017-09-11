package com.webtechmw.sms_api;

/**
 * Created by mangochiman on 9/2/17.
 */

public class User {
    // Labels table name
    public static final String TABLE = "User";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_username = "username";
    public static final String KEY_first_name = "first_name";
    public static final String KEY_last_name = "last_name";
    public static final String KEY_phone_number = "phone_number";
    public static final String KEY_email = "email";
    public static final String KEY_api_key = "api_key";
    public static final String KEY_api_key_status = "api_key_status";
    public static final String KEY_api_key_expiry_date = "api_key_expiry_date";
    public static final String KEY_created_at = "created_at";

    // property help us to keep data
    public int user_ID;
    public String username;
    public String first_name;
    public String last_name;
    public String phone_number;
    public String email;
    public String api_key;
    public String api_key_status;
    public String api_key_expiry_date;
    public String created_at;

}
