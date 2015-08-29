package com.Veiled.Utils.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Laur on 5/10/2015.
 */
public class NotificationIntervalSave {

    private static String mfilename = "NotificationInterval";

    private static String mWeekDaysStartHour = "WeekDaysStartHour";
    private static String mWeekDaysEndHour = "WeekDaysEndHour";
    private static String mWeekendsStartHour = "WeekendsStartHour";
    private static String mWeekendsEndHour = "WeekendsEndHour";

    public static String[] getNotificationIntervalsFromSharedPref(Context context) {
        SharedPreferences sharedStickers = context.getSharedPreferences(mfilename, 0);


        String jsonDataStringWDSH = sharedStickers.getString(mWeekDaysStartHour, "");
        String jsonDataStringWDEH = sharedStickers.getString(mWeekDaysEndHour, "");
        String jsonDataStringWESH = sharedStickers.getString(mWeekendsStartHour, "");
        String jsonDataStringWEEH = sharedStickers.getString(mWeekendsEndHour, "");

        String[] hours = {jsonDataStringWDSH, jsonDataStringWDEH, jsonDataStringWESH, jsonDataStringWEEH};
        return hours;
    }


    public static void setNotificationIntervalsToSharedPref(String[] hours, Context context){
        SharedPreferences sharedStickers = context.getSharedPreferences(mfilename, 0);

        SharedPreferences.Editor edit = sharedStickers.edit();

        edit.putString(mWeekDaysStartHour, hours[0]);
        edit.putString(mWeekDaysEndHour, hours[1]);
        edit.putString(mWeekendsStartHour, hours[2]);
        edit.putString(mWeekendsEndHour, hours[3]);

        edit.commit();
    }
}
