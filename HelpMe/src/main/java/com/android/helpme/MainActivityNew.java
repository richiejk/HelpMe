package com.android.helpme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.helpme.adapters.CustomDialogDesc;
import com.android.helpme.adapters.CustomGridViewAdapter;
import com.android.helpme.data.HelpMeDBHandler;
import com.android.helpme.models.ContactModel;
import com.android.helpme.models.Item;

import java.util.ArrayList;

public class MainActivityNew extends Activity {
    HelpMeApplication application;
    Button startButton;
    HelpMeDBHandler dbHandler;
    CustomDialogDesc dialog;


    LinearLayout linearLayoutUserToCall,
                linearLayoutUserToText,
                linearLayoutSOS,
                linearLayoutSettings,
                linearLayoutHowTo,
                linearLayoutExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        application=(HelpMeApplication)getApplication();

    }

    TextView title;
    ArrayList<ContactModel> contactsForCall;
    ArrayList<ContactModel> contactsForText;
    @Override
    protected void onResume(){
        super.onResume();
        initUI();
        setListeners();
        dbHandler=new HelpMeDBHandler(this);
        contactsForCall=new ArrayList<ContactModel>();
        contactsForText=new ArrayList<ContactModel>();
        contactsForCall=dbHandler.fetchContacts(false);
        contactsForText=dbHandler.fetchContacts(false);
        GPSTracker gpsTracker=new GPSTracker(this);
        if(!gpsTracker.isGPSEnabled){
            gpsTracker.showSettingsAlert();
        }else{
            gpsTracker.getLocation();
        }
        startButton=(Button)findViewById(R.id.btnStart);
        startButton.setTypeface(application.getTypeface());

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contactsForCall.size()==0){
                    Toast.makeText(MainActivityNew.this,"Please select atleast one number to be called in case of emergency by clicking on Icon-1",Toast.LENGTH_SHORT).show();
                }
                else if(contactsForText.size()==0){
                    Toast.makeText(MainActivityNew.this,"Please select atleast one number to be texted in case of emergency by clicking on Icon-2",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(MainActivityNew.this, SafeHoldActivity.class);
                    startActivity(intent);
                }

            }
        });


        title=(TextView)findViewById(R.id.txt_dashboard_title);
        Typeface tf=application.getTypeface();
        title.setTypeface(tf);


    }

    void initUI(){
        linearLayoutUserToCall=(LinearLayout)findViewById(R.id.linearLayoutCall);
        linearLayoutUserToText=(LinearLayout)findViewById(R.id.linearLayoutText);
        linearLayoutSOS=(LinearLayout)findViewById(R.id.linearLayoutSOS);
        linearLayoutSettings=(LinearLayout)findViewById(R.id.linearLayoutSettings);
        linearLayoutHowTo=(LinearLayout)findViewById(R.id.linearLayoutHelp);
        linearLayoutExit=(LinearLayout)findViewById(R.id.linearLayoutExit);


    }

    void setListeners(){
        linearLayoutExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        linearLayoutHowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new CustomDialogDesc(MainActivityNew.this);
                dialog.setMessage(Html.fromHtml(getResources().getString(R.string.hello_world)).toString());
                dialog.setTitle("Readme");

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.FILL_PARENT;
                lp.height = WindowManager.LayoutParams.FILL_PARENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);
                dialog.show();
            }
        });
        linearLayoutSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityNew.this,SettingsActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityNew.this,SOSActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutUserToCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityNew.this,AddCallFirstActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutUserToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivityNew.this,AddTextFirstActivity.class);
                startActivity(intent);
            }
        });
    }


}
