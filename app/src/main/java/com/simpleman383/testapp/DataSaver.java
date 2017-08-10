package com.simpleman383.testapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by simpl on 07.08.2017.
 */


public class DataSaver {
    private static DataSaver dataSaver;
    private final static String URL_MEMO = "Url_Memo";
    private final static String SWITCH_MEMO = "Switch_Memo";

    public enum STATE {
        URL(URL_MEMO), SWITCH(SWITCH_MEMO);

        private String filename;

        STATE(String filename){
            this.filename = filename;
        }

        public String getFilename(){
            return filename;
        }
    }


    private DataSaver(){}

    public static DataSaver getInstance(){
        if (dataSaver == null) {
            dataSaver = new DataSaver();
        }
        return dataSaver;
    }

    public void SaveState(Context context, String info, STATE state){
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(state.getFilename(), Context.MODE_PRIVATE);
            fileOutputStream.write((info).getBytes());
            fileOutputStream.close();
        } catch (Exception ex) {
            Log.e("DataSaver", "Error while logging last URL : " + ex);
        }
    }


    public String getLastState(Context context, STATE state) {
        try {
            InputStream inputStream = context.openFileInput(state.getFilename());
            String data = "";

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                data = reader.readLine();
                inputStream.close();
            }
            return data;
        } catch (Exception ex) {
            Log.e("DataSaver", "Error while reading last URL : " + ex);
            return "";
        }
    }
}
