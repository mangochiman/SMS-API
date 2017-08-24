package com.webtechmw.sms_api;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SMSServiceStartOnBoot extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // here you can add whatever you want this service to do
        ArrayList<String> listItems = new ArrayList<String>();
        try {
            URL smsConnection = new URL("http://localhost:3000/get_messages");
            URLConnection smsCon = smsConnection.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(smsCon.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                JSONArray ja = new JSONArray(line);
                Log.e("JSON", ja.toString());
                //for (int i = 0; i < ja.length(); i++) {
                    //JSONObject jo = (JSONObject) ja.get(i);
                    //listItems.add(jo.getString("text"));
                //}
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
