package com.android.helpme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.helpme.common.Finals;
import com.android.helpme.data.HelpMeDBHandler;
import com.android.helpme.models.ContactModel;

import java.util.ArrayList;

/**
 * Created by RjK on 8/18/13.
 */
public class AddTextFirstActivity extends Activity {

    ArrayList<ContactModel> numbers;
    HelpMeApplication application;
    EditText primaryNumber,secondaryNumber;
    ImageButton primaryBtn,secondaryBtn;
    TextView title,note;
    HelpMeDBHandler handler;
    Button submit;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addmessage_1);
        application=(HelpMeApplication)getApplication();

        handler=new HelpMeDBHandler(this);

    }

    @Override
    protected void onResume(){
        super.onResume();
        initUI();
        fetchFromDB();
        secondaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddTextFirstActivity.this,AddTextActivity.class);
                intent.putExtra(Finals.INTENT_PRIMARY,"false");
                startActivity(intent);

            }
        });
        primaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddTextFirstActivity.this,AddTextActivity.class);
                intent.putExtra(Finals.INTENT_PRIMARY,"true");
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContactModel tempPrimary,tempSec;
                String primaryNo,secondaryNo;

                handler.deleteTextTable();
                if(primaryNumber.getText().toString().trim().length()!=0){
                    primaryNo=primaryNumber.getText().toString().trim();
                    handler.addContact(false,new ContactModel(primaryNo,"",1));
                }
                if(secondaryNumber.getText().toString().trim().length()!=0){
                    secondaryNo=secondaryNumber.getText().toString().trim();
                    handler.addContact(false,new ContactModel(secondaryNo,"",2));
                }
                finish();

            }
        });
    }

    void initUI(){
        Typeface tf=application.getTypeface();
        primaryBtn=(ImageButton)findViewById(R.id.imageButton_call_primary_number);
        secondaryBtn=(ImageButton)findViewById(R.id.imageButton_call_secondary_number);

        primaryNumber=(EditText)findViewById(R.id.editText_call_primary_number);
        primaryNumber.setTypeface(tf);
        secondaryNumber=(EditText)findViewById(R.id.editText_call_secondary_number);
        secondaryNumber.setTypeface(tf);
        title=(TextView)findViewById(R.id.txt_addtext_1_title);
        title.setTypeface(tf);
        note=(TextView)findViewById(R.id.txt_call_note);
        note.setTypeface(tf);
        submit=(Button)findViewById(R.id.button_call_1_submit);
        submit.setTypeface(tf);
    }

    void fetchFromDB(){
        numbers=new ArrayList<ContactModel>();
        numbers=handler.fetchContacts(false);
        if(numbers!=null&&numbers.size()!=0){
            if(numbers.size()==1){
                if(numbers.get(0).getPriority()==1){
                 primaryNumber.setText(numbers.get(0).getPhone());
                }else{
                    secondaryNumber.setText(numbers.get(0).getPhone());
                }
            }else if(numbers.size()==2){
                if(numbers.get(0).getPriority()==1){
                    primaryNumber.setText(numbers.get(0).getPhone());
                    secondaryNumber.setText(numbers.get(1).getPhone());
                }else{
                    primaryNumber.setText(numbers.get(1).getPhone());
                    secondaryNumber.setText(numbers.get(0).getPhone());
                }
            }
        }
    }

}

