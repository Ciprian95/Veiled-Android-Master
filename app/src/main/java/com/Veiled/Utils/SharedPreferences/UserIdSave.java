package com.Veiled.Utils.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.Veiled.Activities.CollectedActivity;
import com.Veiled.SqlConnection.Tables.Campaign;
import com.Veiled.SqlConnection.Tables.CollectedStickers;
import com.google.gson.Gson;

/**
 * Created by Laur on 5/6/2015.
 */
public class UserIdSave {
    private static String mfilename = "UserDetails";
    private static String mUserId = "Id";

    public static String getIdFromSharedPref(Context context) {
        SharedPreferences sharedStickers = context.getSharedPreferences(mfilename, 0);
        String jsonDataString = sharedStickers.getString(mUserId, "");

        return jsonDataString;
    }


    public static void setIdToSharedPref(String toAdd, Context context){
        SharedPreferences sharedStickers = context.getSharedPreferences(mfilename, 0);
 
        SharedPreferences.Editor edit = sharedStickers.edit();
        edit.putString(mUserId, toAdd);
        edit.commit();
    }
}
