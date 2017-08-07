package com.simpleman383.testapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Logger;

public class SMSMonitor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (    intent != null &&
                    bundle != null &&
                    intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") ) {

                Object[] pduArray = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pduArray.length];

                for (int i = 0; i < pduArray.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                }

                String addressFrom = messages[0].getDisplayOriginatingAddress();
                long timestamp = messages[0].getTimestampMillis();

                StringBuilder bodyText = new StringBuilder();

                for (int i = 0; i < messages.length; i++) {
                    bodyText.append(messages[i].getMessageBody());
                }

                String body = bodyText.toString();
                Log.i("SMSMonitor", "Received New Message from: " + addressFrom + "; body: " + body + "; timestamp: " + String.valueOf(timestamp));
                SMSLogger.createLogger().addRecord(context, "Received New Message from: " + addressFrom + "; body: " + body + "; timestamp: " + String.valueOf(timestamp));


                Intent serviceIntent = new Intent(context, SMSMessageService.class);
                serviceIntent.putExtra("Message_From", addressFrom);
                serviceIntent.putExtra("Message_Timestamp", timestamp);
                serviceIntent.putExtra("Message_Body", body);
                context.startService(serviceIntent);
            }
        } catch (Exception ex) {
            Log.e("SMSMonitor", "Exception while receiving : " + ex);
        } finally {
            abortBroadcast();
        }
    }

}
