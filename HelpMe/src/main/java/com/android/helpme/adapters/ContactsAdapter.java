package com.android.helpme.adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.helpme.HelpMeApplication;
import com.android.helpme.R;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by RjK on 8/18/13.
 */
public class ContactsAdapter extends ArrayAdapter<String> {

    Context context;
    Hashtable<String,String> contactsMapper;
    ArrayList<String> contactsName;
    Typeface typeface;

    public ContactsAdapter(Context context, int textViewResourceId, List<String> objects,Hashtable<String,String> contactsMapper,Typeface typeface) {
        super(context, textViewResourceId, objects);
        this.context=context;
        this.contactsName=(ArrayList<String>)objects;
        this.contactsMapper=contactsMapper;
        this.typeface=typeface;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(R.layout.item_contacts_list, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.txt_contacts_list_name);
        TextView number=(TextView)mView.findViewById(R.id.txt_contacts_list_number);

        if(contactsName.get(position) != null )
        {
            text.setText(contactsName.get(position));
            text.setTypeface(typeface);
            number.setText(contactsMapper.get(contactsName.get(position)));
            number.setTypeface(typeface);

        }
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_up_left_alt);
        animation.setDuration(800);

        mView.startAnimation(animation);

        animation = null;

        return mView;
    }
}
