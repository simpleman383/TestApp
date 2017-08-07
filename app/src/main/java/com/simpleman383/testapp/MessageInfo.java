package com.simpleman383.testapp;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

public class MessageInfo {

    private String AddressFrom;
    private String MessageBody;
    private String MessageTime;
    private long Id;


    public String getAddressFrom() {
        return AddressFrom;
    }

    public String getMessageBody() {
        return MessageBody;
    }

    public String getMessageTime() {
        return MessageTime;
    }

    public long getId() {
        return Id;
    }

    public MessageInfo(Bundle bundle) {
        Log.i("MessageEncoder", "Message Params parsing started...");

        String tmp = bundle.getString("Message_From");
        this.AddressFrom = (tmp == null || tmp.isEmpty()) ? "" : tmp;

        tmp = bundle.getString("Message_Body");
        this.MessageBody = (tmp == null || tmp.isEmpty()) ? "" : tmp;
        this.Id = System.currentTimeMillis();

        try {
            long time = bundle.getLong("Message_Timestamp");
            this.MessageTime = getMessageReceiveTime(time);
        } catch (Exception ex) {
            Log.e("MessageEncoder", "Error while params parsing : " + ex);
            this.MessageTime = getMessageReceiveTime(System.currentTimeMillis());
        }
        Log.i("MessageEncoder", "Message Params parsing finished...");
    }

    private String getMessageReceiveTime(long timestamp){
        DateFormat df = new DateFormat();
        String formattedTime = df.format("EEE, d MMM yyyy HH:mm:ss", timestamp).toString();
        return formattedTime;
    }

}
