package com.android.helpme;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import com.android.helpme.adapters.CustomGridViewAdapter;
import com.android.helpme.models.Item;

import java.util.ArrayList;

public class MainActivity extends Activity {
    ArrayList<Item> gridArray = new ArrayList<Item>();
    GridView gridView;
    CustomGridViewAdapter customGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_landing);

    }

    TextView title;

    @Override
    protected void onResume(){
        super.onResume();
        addToGridArray();
        gridView = (GridView) findViewById(R.id.gridView);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.item_dashboard_icon, gridArray);
        gridView.setAdapter(customGridAdapter);
        title=(TextView)findViewById(R.id.txt_dashboard_title);
        Typeface tf=Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Light.ttf");
        title.setTypeface(tf);


    }

    void addToGridArray(){
        Bitmap callIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.usercall);
        Bitmap textIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.text);
        Bitmap sosIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.helpsos);
        Bitmap settingsIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.settings);
        Bitmap helpIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.help);
        Bitmap exitIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.exit);

        gridArray.add(new Item("add number to be called",callIcon));
        gridArray.add(new Item("add number to be texted",textIcon));
        gridArray.add(new Item("start an SOS Alert",sosIcon));
        gridArray.add(new Item("manage Settings",settingsIcon));
        gridArray.add(new Item("learn to use this app",helpIcon));
        gridArray.add(new Item("exit the application",exitIcon));

    }


}
