package com.android.helpme;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.helpme.adapters.ContactsAdapter;
import com.android.helpme.common.Finals;
import com.android.helpme.data.HelpMeDBHandler;
import com.android.helpme.models.ContactModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

/**
 * Created by RjK on 8/18/13.
 */
public class AddCallActivity extends Activity {

    ArrayList<String> names;
    Hashtable<String,String> numberForName;
    HelpMeApplication application;
    HelpMeDBHandler dbHandler;
    int priority;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addcall);
        application=(HelpMeApplication)getApplication();
        dbHandler=new HelpMeDBHandler(this);
        priority=(getIntent().getExtras().getString(Finals.INTENT_PRIMARY).equals("true"))?1:0;

    }

    @Override
    protected void onResume(){
        super.onResume();
        TextView title=(TextView)findViewById(R.id.txt_addcall_title);
        title.setTypeface(application.getTypeface());
        getContacts();
        fillList();
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
    void fillList(){
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
                    dbHandler.deleteCallTablePrimary();
                }else{
                    dbHandler.deleteCallTableSecondary();
                }
                dbHandler.addContact(true,temp);
                finish();
            }
        });
    }
}

/*


*/
/*

*/
