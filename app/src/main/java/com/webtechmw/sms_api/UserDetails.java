package com.webtechmw.sms_api;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class UserDetails extends AppCompatActivity implements android.view.View.OnClickListener {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    Button btnActivateButton;

    TextView textViewUsername;
    TextView textViewEmail;
    TextView textViewFirstName;
    TextView textViewLastName;
    TextView textViewPhoneNumber;
    TextView textViewDateCreated;
    TextView textViewAPIStatus;
    TextView textViewExpiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        Intent intent = getIntent();

        textViewUsername = (TextView) findViewById(R.id.username);
        textViewEmail = (TextView) findViewById(R.id.email);
        textViewFirstName = (TextView) findViewById(R.id.firstName);
        textViewLastName = (TextView) findViewById(R.id.lastName);
        textViewPhoneNumber = (TextView) findViewById(R.id.phoneNumber);
        textViewDateCreated = (TextView) findViewById(R.id.dateCreated);
        textViewAPIStatus = (TextView) findViewById(R.id.apiStatus);
        textViewExpiryDate = (TextView) findViewById(R.id.expiryDate);

        String username = intent.getStringExtra("username");
        String email = intent.getStringExtra("email");
        String first_name = intent.getStringExtra("first_name");
        String last_name = intent.getStringExtra("last_name");
        String phone_number = intent.getStringExtra("phone_number");
        String api_key_status = intent.getStringExtra("api_key_status");
        String api_key_expiry_date = intent.getStringExtra("api_key_expiry_date");
        String created_at = intent.getStringExtra("created_at");
        final String api_key = intent.getStringExtra("api_key");

        textViewUsername.setText(username);
        textViewEmail.setText(email);
        textViewFirstName.setText(first_name);
        textViewLastName.setText(last_name);
        textViewPhoneNumber.setText(phone_number);
        textViewDateCreated.setText(created_at);
        textViewAPIStatus.setText(api_key_status);
        textViewExpiryDate.setText(api_key_expiry_date);
        btnActivateButton = (Button) findViewById(R.id.activateButton);

        if (api_key_status != null){
            if (api_key_status.trim().equalsIgnoreCase("ACTIVE")){
                btnActivateButton.setEnabled(false);
                // send SMS here
            } else {
                btnActivateButton.setOnClickListener(this);
            }
        } else {
            //something was wrong here
            btnActivateButton.setOnClickListener(this);
        }

        final Handler handler = new Handler();
        final Handler SMShandler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {
                new checkAPIKeyStatus().execute(api_key); //check if the APi key is still valid
                handler.postDelayed(this, 2000);
            }
        }, 2000); //Every 120000 ms (2 minutes)

        SMShandler.postDelayed(new Runnable() {
            public void run() {
                new AsyncFetch().execute(api_key); //send SMS
                SMShandler.postDelayed(this, 5000);
            }
        }, 5000); //Every 120000 ms (2 minutes)


    }

    public void onClick(View view) {
        if (view== findViewById(R.id.activateButton)){
            Intent intent = new Intent(getApplicationContext(), activate_token.class);
            startActivity(intent);

        }
    }

    private class checkAPIKeyStatus extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String apiToken = params[0];

                url = new URL("http://www.smsapi.ninja/check_api_key_status/" + apiToken);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
        protected void onPostExecute(String result)  {
            try {
                JSONObject json = new JSONObject(result);
                String api_key_status = json.optString("api_key_status");
                btnActivateButton = (Button) findViewById(R.id.activateButton);
                textViewUsername = (TextView) findViewById(R.id.username);
                textViewEmail = (TextView) findViewById(R.id.email);
                textViewFirstName = (TextView) findViewById(R.id.firstName);
                textViewLastName = (TextView) findViewById(R.id.lastName);
                textViewPhoneNumber = (TextView) findViewById(R.id.phoneNumber);
                textViewDateCreated = (TextView) findViewById(R.id.dateCreated);
                textViewAPIStatus = (TextView) findViewById(R.id.apiStatus);
                textViewExpiryDate = (TextView) findViewById(R.id.expiryDate);

                if (api_key_status != null){
                    if (api_key_status.trim().equalsIgnoreCase("ACTIVE")){
                        btnActivateButton.setEnabled(false);
                        // send SMS here
                    } else {
                        btnActivateButton.setEnabled(true);
                        btnActivateButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), activate_token.class);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    btnActivateButton.setEnabled(true);
                    btnActivateButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), activate_token.class);
                            startActivity(intent);
                        }
                    });
                }

                String username = json.optString("username");
                String first_name = json.optString("first_name");
                String last_name = json.optString("last_name");
                String phone_number = json.optString("phone_number");
                String email = json.optString("email");
                String created_at = json.optString("created_at");
                String api_key = json.optString("api_key");
                String api_expiry_date = json.optString("api_expiry_date");

                UserRepo repo = new UserRepo(getApplicationContext());

                User user = new User();
                user.username = username;
                user.first_name = first_name;
                user.last_name = last_name;
                user.phone_number = phone_number;
                user.email = email;
                user.api_key = api_key;
                user.api_key_status = api_key_status;
                user.api_key_expiry_date = api_expiry_date;
                user.created_at = created_at;

                repo.update(user);

                textViewUsername.setText(username);
                textViewEmail.setText(email);
                textViewFirstName.setText(first_name);
                textViewLastName.setText(last_name);
                textViewPhoneNumber.setText(phone_number);
                textViewDateCreated.setText(created_at);
                textViewAPIStatus.setText(api_key_status);
                textViewExpiryDate.setText(api_expiry_date);

                if (api_key_status != null) {
                    if (api_key_status.trim().equalsIgnoreCase("ACTIVE")) {
                        textViewAPIStatus.setTextColor(Color.parseColor("#71C671"));
                    } else {
                        textViewAPIStatus.setTextColor(Color.parseColor("#8B0000"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                String apiToken = params[0];
                url = new URL("http://www.smsapi.ninja/get_messages/"  + apiToken);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
                e.printStackTrace();
            }

        }

    }
}
