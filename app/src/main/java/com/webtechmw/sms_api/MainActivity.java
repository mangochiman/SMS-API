package com.webtechmw.sms_api;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    Button verify_code;
    EditText txtAPIToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verify_code = (Button) findViewById(R.id.verifyCode);
        verify_code.setOnClickListener(this);
        txtAPIToken = (EditText) findViewById(R.id.apiToken);
        //Intent serviceIntent = new Intent(this, SMSServiceStartOnBoot.class);
        //serviceIntent.startService(serviceIntent);
        //new AsyncFetch().execute();
        /*new Handler().postDelayed(new Runnable() {
            public void run() {
                //new AsyncFetch().execute();
            }
        }, 100); //Run every 1 minute => 60000*/

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                new AsyncFetch().execute();
                handler.postDelayed(this, 1000); //now is every 2 minutes
            }
        }, 1000); //Every 120000 ms (2 minutes)
    }


    public void onClick(View view) {
        if (view== findViewById(R.id.verifyCode)){
            String txtToken = txtAPIToken.getText().toString();
            Log.e("API SIZE", txtToken.length() + " Hererererrererer");
            if (txtToken.length() > 0){
                new VerifyAPIAuthenticity().execute(txtToken);
            }else{
                Toast.makeText(this, " API Token can not be blank", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("START", "This is the start");
            //this method will be running on UI thread

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://www.smsapi.ninja/get_messages");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("ERROR", e.toString());
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                Log.e("ERROR", "Failed to connect");
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            Log.e("AUTOMATED RUN", "RUN IN PROGRESS");
            try {
                //JSONObject json= (JSONObject) new JSONTokener(result);
                JSONObject json = new JSONObject(result);
                Iterator<String> iter = json.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        JSONObject row = json.getJSONObject(key);
                        String phoneNumber = row.optString("phone_number");
                        String message = row.optString("message");



                        Log.i("Send SMS", "");
                        SmsManager sms = SmsManager.getDefault();

                        //PendingIntent sentPI;
                        //String SENT = "SMS_SENT";

                        //sentPI = PendingIntent.getBroadcast(activity, 0, new Intent(SENT), 0);

                        sms.sendTextMessage(phoneNumber, null, message, null, null);

                        //Log.e("Sending Message ::: ", message);
                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }


            } catch (JSONException e) {
                Log.e("ERROR", e.toString());
                e.printStackTrace();
            }

        }

    }

    private class VerifyAPIAuthenticity extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        private ProgressDialog  dialog = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait. Verification in progress");
            this.dialog.show();
            //this.dialog.setMessage("Please wait");
            Log.e("START", "This is the start");
            //this method will be running on UI thread

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String apiToken = apiToken = params[0];

                url = new URL("http://10.0.2.2:3000/verify_api_aunthenticity/" + apiToken);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("ERROR", e.toString());
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                Log.e("ERROR", "Failed to connect");
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            Log.e("VERIFYING API TOKEN", "RUN IN PROGRESS");
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            try {
                //JSONObject json= (JSONObject) new JSONTokener(result);
                JSONObject json = new JSONObject(result);
                String username = json.optString("username");
                String first_name = json.optString("first_name");
                String last_name = json.optString("last_name");
                String phone_number = json.optString("phone_number");
                String email = json.optString("email");
                String created_at = json.optString("created_at");
                String api_key_status = json.optString("api_key_status");
                String api_expiry_date = json.optString("api_expiry_date");

                UserRepo repo = new UserRepo(getApplicationContext());

                User user = new User();
                user.username = username;
                user.first_name = first_name;
                user.last_name = last_name;
                user.phone_number = phone_number;
                user.email = email;
                user.api_key = "";
                repo.insert(user);
                Toast.makeText(MainActivity.this, "Successfully saved user", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), UserDetails.class);
                //intent.putExtra("student_Id",0);
                startActivity(intent);
                //Toast.makeText(this, "No student!",Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                Log.e("FAILED TO SAVE USER", e.toString());
                e.printStackTrace();
            }

        }

    }
}
