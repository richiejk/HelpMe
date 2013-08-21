package com.richiejose.helpme;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.richiejose.helpme.data.HelpMeDBHandler;

/**
 * Created by RjK on 8/18/13.
 */
public class SettingsActivity extends Activity {

    Button save;
    EditText seconds,message,keywords;
    TextView secondsTitle,messageTitle,mainTitle,note,keywordsTitle;
    HelpMeDBHandler dbHandler;
    HelpMeApplication application;
    Typeface tf;
    String secondsStr,messageStr,keywordStr;
    int sx=0;
    int mg=0;
    int ky=0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sx=0;
        mg=0;
        application=(HelpMeApplication)getApplication();
        dbHandler=new HelpMeDBHandler(this);
        tf=application.getTypeface();

        save=(Button)findViewById(R.id.button_settings_save);
        save.setTypeface(tf);

        seconds=(EditText)findViewById(R.id.editText_settings_seconds_value);
        seconds.setTypeface(tf);
        message=(EditText)findViewById(R.id.editText_settings_message_value);
        message.setTypeface(tf);
        keywords=(EditText)findViewById(R.id.editText_settings_keywords_value);
        keywords.setTypeface(tf);

        secondsTitle=(TextView)findViewById(R.id.txt_settings_seconds_title);
        messageTitle=(TextView)findViewById(R.id.txt_settings_message_title);
        mainTitle=(TextView)findViewById(R.id.txt_settings_title);
        note=(TextView)findViewById(R.id.txt_settings_note);
        keywordsTitle=(TextView)findViewById(R.id.txt_settings_keywords_title);

        keywordsTitle.setTypeface(tf);
        secondsTitle.setTypeface(tf);
        messageTitle.setTypeface(tf);
        mainTitle.setTypeface(tf);
        note.setTypeface(tf);

        secondsStr=dbHandler.fetchSeconds();
        messageStr=dbHandler.fetchMessage();
        keywordStr=dbHandler.fetchKeyword();

        if(secondsStr!=null&&secondsStr.trim().length()!=0){
            seconds.setText(secondsStr);
        }else if(messageStr!=null&&messageStr.trim().length()!=0){
            message.setText(messageStr);
        }
        if(keywordStr!=null&&keywordStr.trim().length()!=0){
            keywords.setText(keywordStr);
        }

        if(secondsStr.trim().length()==0){
            seconds.setText("5");
        }
        if(messageStr.trim().length()==0){
            message.setText("I think I may be in trouble, Please Help! ");
        }
        if(keywordStr.trim().length()==0){
            keywords.setText("HELPME_START");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(seconds.length()!=0){
                    sx=1;
                }
                if(message.length()!=0){
                    mg=1;
                }
                if(keywords.length()!=0){
                    ky=1;
                }
                String finalKeyword="";
                if(ky!=1){
                    finalKeyword="HELPME_START";

                }else{
                    finalKeyword=keywords.getText().toString();
                }

                if(sx!=1){
                    finalSeconds="5";

                }else{
                    finalSeconds=seconds.getText().toString();
                }

                if(mg!=1){
                    finalMessage="I think I may be in trouble, Please Help! ";
                }else{
                    finalMessage=message.getText().toString();
                }

                dbHandler.addSettings(finalSeconds,finalMessage,finalKeyword);
                finish();
            }
        });



    }

    String finalSeconds;
    String finalMessage;

}