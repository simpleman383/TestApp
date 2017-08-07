package com.simpleman383.testapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class SMSMessageService extends IntentService {

    HttpURLConnection Connection;
    URL Url;
    String Parameters;

    public SMSMessageService(){
        super("SMSMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("SMSMessageService", "onHandleIntent started...");
        final Bundle bundle = intent.getExtras();

        MessageInfo mInfo = new MessageInfo(bundle);
        this.Parameters = MessageEncoder.urlEncodeParams(mInfo);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String urlInput = preferences.getString("ForwardTo","");

        if (setupConnection(urlInput)) {
            sendRequest();
        }

        stopSelf();
        Log.i("SMSMessageService", "service stopped...");
    }

    private boolean setupConnection(String url) {
        try {
            Url = new URL(url);
            Connection = (HttpURLConnection) Url.openConnection();
            Connection.setDoOutput(true);
            Connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Connection.setRequestMethod("POST");
        } catch (Exception ex) {
            Log.e("SMSMessageService", "Error while setting up connection to " + url + " : " + ex);
            return false;
        }
        Log.i("SMSMessageService", "Connection to " + url);
        return true;
    }

    private void sendRequest(){
        try {
            Log.i("SMSMessageService", "Start sending request...");
            OutputStreamWriter request = new OutputStreamWriter(Connection.getOutputStream());
            request.write(Parameters);
            request.flush();
            request.close();

            InputStream is = Connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Connection.disconnect();

            SMSLogger.createLogger().addRecord(getApplicationContext() ,"Request to "+ Url.toString() +" : " + Parameters);

            Log.i("SMSMessageService", "Request sent successfully...");
        } catch (Exception ex) {
            Log.e("SMSMessageService", "Error while sending request to " + Url + " : " + ex);
        }
    }

}
