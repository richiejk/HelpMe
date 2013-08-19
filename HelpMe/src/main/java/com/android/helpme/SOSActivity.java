package com.android.helpme;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by RjK on 8/19/13.
 */
public class SOSActivity extends Activity {
    MediaPlayer meidaPlayer = new MediaPlayer();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sos_1);
        play("sounds/sound.mp3");

    }
    HelpMeApplication application;
    Button stop;
    protected void onResume(){
        super.onResume();

        stop=(Button)findViewById(R.id.stopBtn);
        application=(HelpMeApplication)getApplication();
        stop.setTypeface(application.getTypeface());
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meidaPlayer.stop();
                finish();
            }
        });
    }

    private void play(String file) {
        try {
            AssetFileDescriptor afd = getAssets().openFd(file);
            meidaPlayer.setDataSource(
                    afd.getFileDescriptor(),
                    afd.getStartOffset(),
                    afd.getLength()
            );
            afd.close();
            meidaPlayer.prepare();
            AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);
            meidaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}