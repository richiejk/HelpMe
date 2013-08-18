package com.android.helpme;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.helpme.adapters.CustomGridViewAdapter;
import com.android.helpme.adapters.SafeHoldActivity;
import com.android.helpme.models.Item;

import java.util.ArrayList;

public class MainActivity extends Activity {
    ArrayList<Item> gridArray = new ArrayList<Item>();
    GridView gridView;
    CustomGridViewAdapter customGridAdapter;
    HelpMeApplication application;
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_landing);
        application=(HelpMeApplication)getApplication();

    }

    TextView title;

    @Override
    protected void onResume(){
        super.onResume();
        addToGridArray();
        startButton=(Button)findViewById(R.id.start_button);
        startButton.setTypeface(application.getTypeface());
        gridView = (GridView) findViewById(R.id.gridView);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.item_dashboard_icon, gridArray);
        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch(i){
                    case 0:intent=new Intent(MainActivity.this,AddCallActivity.class);
                        startActivity(intent);
                            break;
                    case 1: intent=new Intent(MainActivity.this,AddTextActivity.class);
                        startActivity(intent);
                        break;
                    case 2:break;
                    case 3:break;
                    case 4:break;
                    case 5: finish();
                        break;
                }
                Toast.makeText(MainActivity.this,i+"",Toast.LENGTH_SHORT).show();
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SafeHoldActivity.class);
                startActivity(intent);
            }
        });
        title=(TextView)findViewById(R.id.txt_dashboard_title);
        Typeface tf=application.getTypeface();
        title.setTypeface(tf);


    }

    void addToGridArray(){
        gridArray=new ArrayList<Item>();
        Bitmap callIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.usercall);
        Bitmap textIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.text);
        Bitmap sosIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.helpsos);
        Bitmap settingsIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.settings);
        Bitmap helpIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
        Bitmap exitIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.exit);

        gridArray.add(new Item("add number to be called",callIcon));
        gridArray.add(new Item("add number to be texted",textIcon));
        gridArray.add(new Item("start an SOS alert",sosIcon));
        gridArray.add(new Item("manage settings",settingsIcon));
        gridArray.add(new Item("learn to use this app",helpIcon));
        gridArray.add(new Item("exit the application",exitIcon));

/*        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        }
        phones.close();*/

    }


}
