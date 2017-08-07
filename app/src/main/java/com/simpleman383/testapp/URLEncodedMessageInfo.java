package com.simpleman383.testapp;

import android.os.Bundle;
import android.util.Log;
import java.net.URLEncoder;

public class URLEncodedMessageInfo extends MessageInfo {

    @Override
    public String getAddressFrom() {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(super.getAddressFrom(), "UTF-8");
        } catch (Exception ex) {
            Log.e("URLEncodedMessageInfo", "getMessageBody() : " + ex);
        } finally {
            return encoded;
        }
    }

    @Override
    public String getMessageBody() {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(super.getMessageBody(), "UTF-8");
        } catch (Exception ex) {
            Log.e("URLEncodedMessageInfo", "getMessageBody() : " + ex);
        } finally {
            return encoded;
        }
    }

    @Override
    public String getMessageTime() {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(super.getMessageTime(), "UTF-8");
        } catch (Exception ex) {
            Log.e("URLEncodedMessageInfo", "getMessageBody() : " + ex);
        } finally {
            return encoded;
        }
    }

    @Override
    public String getId() {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(super.getId(), "UTF-8");
        } catch (Exception ex) {
            Log.e("URLEncodedMessageInfo", "getMessageBody() : " + ex);
        } finally {
            return encoded;
        }
    }

    public URLEncodedMessageInfo(Bundle bundle) {
        super(bundle);
    }

    public String getRawParams(){
        return String.format("id=%s&body=%s&sender=%s&send_at=%s", this.getId(), this.getMessageBody(), this.getAddressFrom(), this.getMessageTime());
    }
}
