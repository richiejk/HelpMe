package com.richiejose.helpme;

import android.app.Application;
import android.graphics.Typeface;

/**
 * Created by RjK on 8/18/13.
 */
public class HelpMeApplication extends Application {
    public Typeface getTypeface() {

        if(typeface==null){
            typeface=Typeface.createFromAsset(getAssets(), "fonts/SegoeWP-Light.ttf");
        }
        return typeface;

    }

    public Typeface typeface;
}
