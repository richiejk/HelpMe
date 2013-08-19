package com.android.helpme.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.helpme.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by RjK on 8/19/13.
 */
public class SafeHoldActivity extends Activity {
    ImageButton holdButton;
    Button tapButton;
    LinearLayout backgroundLayout;
    int pressed=0;
    boolean released=true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_safehold);

    }
    CountDownTimer timer;
    boolean isSafe=false;
    @Override
    protected void onResume(){
        pressed=0;
        super.onResume();
        holdButton=(ImageButton)findViewById(R.id.holdButton);
        holdButton.setImageDrawable(getResources().getDrawable(R.drawable.hold_green_1));
        backgroundLayout=(LinearLayout)findViewById(R.id.ll_hold_base);
        tapButton=(Button)findViewById(R.id.tapButton);
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
                    timer= new CountDownTimer(5000, 1000) {

                        public void onTick(long millisUntilFinished) {

                        }

                        public void onFinish() {
                            if(!isSafe){
                                String uri = "tel:108";
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(uri));
                                startActivity(intent);
                            }

                        }
                    }.start();
                }
                return true;
            }
        });
    }
}
