package com.Veiled.Components.ScreenDetails;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * Created by Marius on 3/14/2015.
 */

public class ScreenDetails implements IScreenDetails {


    private static ScreenDetails instance = null;
    private Point details;
    private static Activity m_activity;

    private ScreenDetails(int width, int height){
        details = new Point(width,height);
    }

    public static ScreenDetails getInstance(Activity activity){
        if (instance == null) {
            instance = new ScreenDetails(0, 0);

            m_activity = activity;
            instance.getPhysicalDetails();
        }
        return instance;
    }
    public static ScreenDetails getInstance(int width, int height){
      //  if (instance == null)
            instance = new ScreenDetails(width,height);
        return instance;
    }
    @Override
    public void getPhysicalDetails(){
        // Set screen values
        DisplayMetrics dm = new DisplayMetrics();
        m_activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        details.x = width;
        details.y = height;

        //TODO will be use as screen ratio
        //int screenInches = 3 * (int)Math.round(Math.sqrt(x+y));
    }

    @Override
    public Point getScreenDetails() {
        return details;
    }
}
