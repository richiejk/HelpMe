package com.richiejose.helpme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.richiejose.helpme.data.HelpMeDBHandler;

/**
 * Created by RjK on 8/20/13.
 */
public class SimpleSmsReciever extends BroadcastReceiver {

    private static final String TAG = "Message recieved";
    GPSTracker gpsTracker;
    double lat,lon;
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
        if(keyword==null||keyword.equals("")){
            keyword="HELPME_START";
        }
      //  Toast.makeText(context,keyword,Toast.LENGTH_LONG).show();
        final String phoneNo=messages.getOriginatingAddress();
        if(keyword!=null&&messages.getMessageBody().length()!=0&&messages.getMessageBody().contains(keyword.trim())){
            Handler handler=new Handler();
            Toast.makeText(context,"in body",Toast.LENGTH_LONG).show();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gpsTracker=new GPSTracker(newContext);
                    message="I think I may be in trouble, Please Help! ";

                    if(gpsTracker.canGetLocation()){
                        lat=gpsTracker.getLatitude();
                        lon=gpsTracker.getLongitude();
                    }
                    if(lat!=0){
                            sendSMS(phoneNo,message+" I'm at http://maps.google.com/maps?q="+lat+","+lon);
                    }
                    else{
                            sendSMS(phoneNo,message);
                    }
                }
            },10000);

        }
       // context.unregisterReceiver(this);
    }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

}
