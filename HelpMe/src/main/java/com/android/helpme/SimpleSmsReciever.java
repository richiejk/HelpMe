package com.android.helpme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.android.helpme.data.HelpMeDBHandler;

/**
 * Created by RjK on 8/20/13.
 */
public class SimpleSmsReciever extends BroadcastReceiver {

    private static final String TAG = "Message recieved";
    GPSTracker gpsTracker;
    double lat,lon;
    String smsNumber;
    String smsNumber2="false";
    HelpMeDBHandler dbHandler;
    String message;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        final Context newContext=context;
        SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]);
        dbHandler=new HelpMeDBHandler(newContext);
        String keyword=dbHandler.fetchKeyword();
        if(messages.getMessageBody().length()!=0&&messages.getMessageBody().contains(keyword.trim())){
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gpsTracker=new GPSTracker(newContext);
                    message="I think I may be in trouble, Please Help! ";

                    if(dbHandler.fetchMessage()!=null&&dbHandler.fetchMessage().trim().length()!=0){
                        message=dbHandler.fetchMessage();
                    }


                    if(dbHandler.fetchContact(false,1)!=null){
                        smsNumber=dbHandler.fetchContact(false,1).getPhone();
                    }
                    if(dbHandler.fetchContact(false,2)!=null){
                        smsNumber2=dbHandler.fetchContact(false,2).getPhone();
                    }

                    if(gpsTracker.canGetLocation()){
                        lat=gpsTracker.getLatitude();
                        lon=gpsTracker.getLongitude();
                    }
                    if(lat!=0){
                        if(!smsNumber.equals("false")){
                            sendSMS(smsNumber,message+" I'm at http://maps.google.com/maps?q="+lat+","+lon);
                        }
                        if(!smsNumber2.equals("false")){
                            sendSMS(smsNumber2,message+" I'm at http://maps.google.com/maps?q="+lat+","+lon);
                        }
                    }
                    else{
                        if(!smsNumber.equals("false")){
                            sendSMS(smsNumber,message);
                        }
                        if(!smsNumber2.equals("false")){
                            sendSMS(smsNumber2,message);
                        }
                    }
                }
            },10000);

        }
    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

}
