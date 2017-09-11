package com.webtechmw.sms_api;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserDetails extends AppCompatActivity {

    Button btnActivateButton;
    Button btnClose;
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
        String api_key = intent.getStringExtra("api_key");

        textViewUsername.setText(username);
        textViewEmail.setText(email);
        textViewFirstName.setText(first_name);
        textViewLastName.setText(last_name);
        textViewDateCreated.setText(created_at);
        textViewAPIStatus.setText(api_key_status);
        textViewExpiryDate.setText(api_key_expiry_date);

    }
}
