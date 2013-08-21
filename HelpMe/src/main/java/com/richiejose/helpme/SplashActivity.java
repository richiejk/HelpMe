package com.richiejose.helpme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by RjK on 8/20/13.
 */
public class SplashActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        //* set time to splash out
        final int welcomeScreenDisplay = 1000;
        // create a thread to show splash up to splash time
        Thread welcomeThread = new Thread() {

            int wait = 0;
            @Override
            public void run() {
                try {
                    super.run();

					/*
					  use while to get the splash time. Use sleep() to increase
					 the wait variable for every 100L.*/

                    while (wait < welcomeScreenDisplay) {
                        sleep(100);
                        wait += 100;
                    }
                } catch (Exception e) {

                } finally {
					/*
					  Called after splash times up.
					  Here we moved to another main activity class*/

                    Intent intentSearch = new Intent(SplashActivity.this, MainActivityNew.class);
                    intentSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentSearch);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // do nothing
        }
        return true;
    }
}
