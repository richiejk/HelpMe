package com.android.helpme;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.CalendarContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.helpme.R;
import com.android.helpme.data.HelpMeDBHandler;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by RjK on 8/19/13.
 */
public class SafeHoldActivity extends Activity {
    ImageButton holdButton;
    Button tapButton;
    GPSTracker gpsTracker;
    LinearLayout backgroundLayout;
    int pressed=0;
    String numberToBeCalled;
    String smsNumber;
    String smsNumber2="false";
    long seconds;
    String message;
    HelpMeDBHandler dbHandler;
    boolean released=true;
    double lat,lon;
    ConnectivityManager connectivityManager;



    private void _getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        LocationListener loc_listener = new LocationListener() {

            public void onLocationChanged(Location l) {}

            public void onProviderEnabled(String p) {}

            public void onProviderDisabled(String p) {}

            public void onStatusChanged(String p, int status, Bundle extras) {}
        };
        locationManager
                .requestLocationUpdates(bestProvider, 0, 0, loc_listener);
        location = locationManager.getLastKnownLocation(bestProvider);
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
        } catch (NullPointerException e) {
            e.printStackTrace();
            lat = -1.0;
            lon = -1.0;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_safehold);

    }
    CountDownTimer timer;
    boolean isSafe=false;
    @Override
    protected void onResume(){
        smsNumber2="false";
        pressed=0;
        super.onResume();
        isSafe=true;
        gpsTracker=new GPSTracker(this);
        dbHandler=new HelpMeDBHandler(this);
        numberToBeCalled=dbHandler.fetchContact(true,1).getPhone();
        smsNumber=dbHandler.fetchContact(false,1).getPhone();
        if(dbHandler.fetchContact(false,2)!=null){
            smsNumber2=dbHandler.fetchContact(false,2).getPhone();
        }

        seconds=5000;
        if(dbHandler.fetchSeconds()!=null&&dbHandler.fetchSeconds().trim().length()!=0){
            seconds=(Long.parseLong(dbHandler.fetchSeconds()))*1000;
        }

        message="I think I may be in trouble, Please Help! ";

        if(dbHandler.fetchMessage()!=null&&dbHandler.fetchMessage().trim().length()!=0){
            message=dbHandler.fetchMessage();
        }

        holdButton=(ImageButton)findViewById(R.id.holdButton);
        holdButton.setImageDrawable(getResources().getDrawable(R.drawable.hold_green_1));
        backgroundLayout=(LinearLayout)findViewById(R.id.ll_hold_base);
        tapButton=(Button)findViewById(R.id.tapButton);
        backgroundLayout.setBackgroundColor(getResources().getColor(R.color.clouds));
        tapButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    ++pressed;
                    if(pressed==2){
                        holdButton.setImageDrawable(getResources().getDrawable(R.drawable.hold_green_1));
                        backgroundLayout.setBackgroundColor(getResources().getColor(R.color.clouds));
                        released=true;
                        isSafe=true;
                    }
                }
                return true;
            }
        });

        holdButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pressed=0;
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    if(released){
                        backgroundLayout.setBackgroundColor(getResources().getColor(R.color.emerald));
                    }
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    isSafe=false;
                    released=false;
                    holdButton.setImageDrawable(getResources().getDrawable(R.drawable.hold_red_1));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.alizarin));
                    timer= new CountDownTimer(seconds, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            if(!isSafe){
                                isSafe=true;

                                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null)
                                {
                                    if(gpsTracker.canGetLocation()){
                                        lat=gpsTracker.getLatitude();
                                        lon=gpsTracker.getLongitude();
                                    }
                                    if(lat!=0){
                                        sendSMS(smsNumber,message+" I'm at http://maps.google.com/maps?q="+lat+","+lon);
                                        if(!smsNumber2.equals("false")){
                                            sendSMS(smsNumber,message+" I'm at http://maps.google.com/maps?q="+lat+","+lon);
                                        }
                                    }
                                    else{
                                        sendSMS(smsNumber,message);
                                        if(!smsNumber2.equals("false")){
                                            sendSMS(smsNumber,message);
                                        }
                                    }
                                    final String uri = "tel:"+numberToBeCalled;
                                    //Toast.makeText(SafeHoldActivity.this,uri,Toast.LENGTH_LONG).show();

                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                            intent.setData(Uri.parse(uri));
                                            startActivity(intent);
                                        }
                                    },5000);
                                }
                                else
                                {
                                    final String altUri = "tel:911";
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse(altUri));
                                    startActivity(intent);
                                }

                            }

                        }
                    }.start();
                }
                return true;
            }
        });
    }

    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                unregisterReceiver(this);
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
