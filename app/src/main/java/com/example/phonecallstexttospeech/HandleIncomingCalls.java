package com.example.phonecallstexttospeech;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.widget.*;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class HandleIncomingCalls extends BroadcastReceiver {
    String call_msg = "";
    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context,"Ringing..! - " + incomingNumber,Toast.LENGTH_LONG).show();
                call_msg = " is calling you..!";
            }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                Toast.makeText(context,"Ended/Received..! - " + incomingNumber,Toast.LENGTH_LONG).show();
                call_msg = " OFFHOOK State";
            }
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context,"Idle..! - " + incomingNumber,Toast.LENGTH_LONG).show();
                call_msg = "'s call is ended..!";
            }

            Intent speechIntent = new Intent();
            speechIntent.setClass(context, PhoneCallSpeech.class);
            String callerName = getContactName(context,incomingNumber);
            System.out.println(callerName);
            if (callerName == null){
                speechIntent.putExtra("MESSAGE",  "Unknown Person "+ incomingNumber + " " + call_msg);
            }else{
                speechIntent.putExtra("MESSAGE", callerName + "" + call_msg);
            }

            speechIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            context.startActivity(speechIntent);
            context.startService(speechIntent);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    context.startService(speechIntent);
                }
            }, 2000);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }
}
