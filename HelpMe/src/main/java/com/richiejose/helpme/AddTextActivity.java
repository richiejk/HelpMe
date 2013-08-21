package com.richiejose.helpme;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.richiejose.helpme.adapters.ContactsAdapter;
import com.richiejose.helpme.common.Finals;
import com.richiejose.helpme.data.HelpMeDBHandler;
import com.richiejose.helpme.models.ContactModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

/**
 * Created by RjK on 8/18/13.
 */
public class AddTextActivity extends Activity {

    ArrayList<String> names;
    Hashtable<String,String> numberForName;
    HelpMeApplication application;
    ArrayList<String> filteredNames;
    EditText searchKeyword;
    HelpMeDBHandler dbHandler;
    int priority;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addtext);
        application=(HelpMeApplication)getApplication();
        priority=(getIntent().getExtras().getString(Finals.INTENT_PRIMARY).equals("true"))?1:0;

    }

    @Override
    protected void onResume(){
        super.onResume();
        dbHandler=new HelpMeDBHandler(this);
        TextView title=(TextView)findViewById(R.id.txt_addcall_title);
        title.setTypeface(application.getTypeface());
        searchKeyword=(EditText)findViewById(R.id.editText_searchKey);
        searchKeyword.setTypeface(application.getTypeface());
        getContacts();
        fillList(names);
        searchKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence editable, int i, int i2, int i3) {
                if(editable.toString().trim().length()!=0){
                    filteredNames=new ArrayList<String>();
                    for(String temp:names){
                        if(temp.toUpperCase().contains(editable.toString().toUpperCase())){
                            filteredNames.add(temp);
                        }
                    }
                    fillList(filteredNames);
                }else{
                    fillList(names);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    void getContacts(){
        names=new ArrayList<String>();
        numberForName=new Hashtable<String, String>();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            names.add(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            Collections.sort(names);
            numberForName.put(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }
        phones.close();
    }
    ListView listView;
    ContactsAdapter adapter;


    void fillList(final ArrayList<String> names){
        listView=(ListView)findViewById(R.id.listView_contacts);
        if(names!=null&&names.size()!=0){
            adapter=new ContactsAdapter(this,R.id.txt_contacts_list_name,names,numberForName,application.getTypeface());
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selName=names.get(i);
                String selNumber=numberForName.get(selName);
                ContactModel temp=new ContactModel(selNumber,selName,priority);
                if(priority==1){
                    dbHandler.deleteTextTablePrimary();
                }else{
                    dbHandler.deleteTextTableSecondary();
                }
                dbHandler.addContact(false,temp);
                finish();
            }
        });
    }
}