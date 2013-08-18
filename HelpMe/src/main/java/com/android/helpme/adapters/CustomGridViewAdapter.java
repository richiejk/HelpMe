package com.android.helpme.adapters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.helpme.HelpMeApplication;
import com.android.helpme.R;
import com.android.helpme.models.Item;

import java.util.ArrayList;

/**
 * Created by RjK on 8/18/13.
 */
public class CustomGridViewAdapter extends ArrayAdapter<Item> {

    Context context;
    int layoutResourceId;
    ArrayList<Item> data = new ArrayList<Item>();
    HelpMeApplication application;

    public CustomGridViewAdapter(Context context, int layoutResourceId,ArrayList<Item> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        application=(HelpMeApplication)((Activity)context).getApplication();
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.txt_dashboard_icon);
            holder.imageItem = (ImageView) row.findViewById(R.id.img_dashboard_icon);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        Typeface tf=application.getTypeface();

        Item item = data.get(position);
        holder.txtTitle.setText(item.desc);
        holder.txtTitle.setTypeface(tf ,1);
        holder.imageItem.setImageBitmap(item.img);
        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;

    }
}
