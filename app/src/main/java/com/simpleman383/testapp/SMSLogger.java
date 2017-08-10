package com.simpleman383.testapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SMSLogger {

    private static SMSLogger logger;
    private final static String LOG_NAME = "SMSLog";
    private final static long LOG_MAX_SIZE = 4096;

    private SMSLogger(){
    }

    public static SMSLogger createLogger(){
        if (logger == null) {
            logger = new SMSLogger();
        }
        return logger;
    }

    public void addRecord(Context ctx, String info) {
        try {
            File file = new File(ctx.getFilesDir(), LOG_NAME);
            if (file.length() > LOG_MAX_SIZE)
                this.clearLogger(ctx);

            FileOutputStream fileOutputStream = ctx.openFileOutput(LOG_NAME, Context.MODE_APPEND);
            fileOutputStream.write((getCurrentTime() + " : " + info + "\n\n").getBytes());
            fileOutputStream.close();
        } catch (Exception ex) {
            Log.e("SMSLogger", "Error while logging SMS : " + ex);
        }
    }

    private String getCurrentTime(){
        DateFormat df = new DateFormat();
        String formattedTime = df.format("d MMM yyyy HH:mm:ss", System.currentTimeMillis()).toString();
        return formattedTime;
    }

    public void clearLogger(Context ctx) {
        try {
            FileOutputStream fileOutputStream = ctx.openFileOutput(LOG_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write("".getBytes());
            fileOutputStream.close();
        } catch (Exception ex) {}
    }

    public String readAll(Context context){
        try {
            InputStream inputStream = context.openFileInput(LOG_NAME);
            String data = "";

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    data += line + "\n\n";
                }
            }

            inputStream.close();
            return data;
        } catch (Exception ex) {
            Log.e("SMSLogger", "Error while read info : " + ex);
            return "error while reading log file...";
        }
    }
}
