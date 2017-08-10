package com.simpleman383.testapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class SMSMessageService extends IntentService {

    public SMSMessageService() {
        super("SMSMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("SMSMessageService", "onHandleIntent started...");
        final Bundle bundle = intent.getExtras();

        final URLEncodedMessageInfo mInfo = new URLEncodedMessageInfo(bundle);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String urlInput = preferences.getString("ForwardTo", "");

        RequestQueue queue = QueueController.getInstance(getApplicationContext()).getAppRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, urlInput, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SMSLogger.createLogger().addRecord(getApplicationContext(), "Successful Request to " + urlInput + " : " +
                        mInfo.getRawParams() + " Response : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SMSLogger.createLogger().addRecord(getApplicationContext(), "Failed Request to " + urlInput + " : " +
                        mInfo.getRawParams() + " Error : " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("body", mInfo.getMessageBody());
                params.put("id", mInfo.getId());
                params.put("send_at", mInfo.getMessageTime());
                params.put("sender", mInfo.getAddressFrom());
                return params;
            }
        };

        queue.add(request);

        stopSelf();
        Log.i("SMSMessageService", "service stopped...");
    }
}
