package com.android.helpme.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.helpme.HelpMeApplication;
import com.android.helpme.R;

/**
 * Created by RjK on 8/18/13.
 */
public class CustomDialogDesc extends Dialog implements View.OnClickListener {

    public Dialog d;
    int layoutId;
    public Button yes;
    Context context;
    TextView content;
    TextView title;
    String titleStr;
    String contentStr;

    HelpMeApplication application;


    public CustomDialogDesc(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    public void setTitle(String title){
        this.titleStr=title;
    }

    public void setMessage(String message){
        this.contentStr=message;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.item_desc_dialog);
        yes = (Button) findViewById(R.id.desc_done);
        yes.setOnClickListener(this);
        application=(HelpMeApplication)((Activity)context).getApplication();
        title=(TextView)findViewById(R.id.desc_title);
        content=(TextView)findViewById(R.id.desc_content);
        title.setText(titleStr);
        content.setText(contentStr);
        this.setCustomFont((ViewGroup)findViewById(android.R.id.content));
    }



    protected void setCustomFont(ViewGroup root){
        Typeface typeface=application.getTypeface();
        for(int i=0; i< root.getChildCount(); i++){

            View v = root.getChildAt(i);

            if(v instanceof TextView){
                ((TextView)v).setTypeface(typeface);
            } else if(v instanceof ViewGroup){
                setCustomFont((ViewGroup)v);
            }

        } // --for

    }
}
