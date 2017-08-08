package com.simpleman383.testapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by simpl on 08.08.2017.
 */

public class QueueController {

    private RequestQueue appRequestQueue;
    private static Context appContext;
    private static QueueController queueController;

    private QueueController(Context context) {
        appContext = context;
        appRequestQueue = getAppRequestQueue();
    }

    public static synchronized QueueController getInstance(Context context){
        if (queueController == null) {
            queueController = new QueueController(context);
        }
        return queueController;
    }

    public RequestQueue getAppRequestQueue() {
        if (appRequestQueue == null) {
            appRequestQueue = Volley.newRequestQueue(appContext.getApplicationContext());
        }
        return appRequestQueue;
    }


}
