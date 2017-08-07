package com.simpleman383.testapp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import android.util.Log;

public class MessageEncoder {

    public static String urlEncodeParams(MessageInfo message) {

        String Parameters = "";
        String EncodedTime = "";
        String EncodedBody = "";
        String EncodedSender = "";
        String Id = "";

        try {
            EncodedSender = URLEncoder.encode(message.getAddressFrom(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Log.e("MessageEncoder", "Error While encoding Sender : " + ex);
            EncodedSender = "";
        } finally {
            Parameters += "sender=" + EncodedSender + "&";
        }

        try {
            EncodedBody = URLEncoder.encode(message.getMessageBody(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Log.e("MessageEncoder", "Error While encoding Body : " + ex);
            EncodedBody = "";
        } finally {
            Parameters += "body=" + EncodedBody + "&";
        }

        try {
            EncodedTime = URLEncoder.encode(message.getMessageTime(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Log.e("MessageEncoder", "Error While encoding Message Time : " + ex);
            EncodedTime = "";
        } finally {
            Parameters += "send_at=" + EncodedTime + "&";
        }

        try {
            Id = URLEncoder.encode(String.valueOf(message.getId()), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Log.e("MessageEncoder", "Error While encoding Message Id : " + ex);
            Id = "";
        } finally {
            Parameters += "id=" + Id + "&";
        }
        return Parameters;
    }
}
