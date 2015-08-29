package com.Veiled.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.Veiled.Activities.CollectedActivity;
import com.Veiled.SqlConnection.Tables.Campaign;
import com.Veiled.SqlConnection.Tables.CollectedCampaign;
import com.Veiled.SqlConnection.Tables.CollectedStickers;
import com.Veiled.SqlConnection.Tables.CurrentCampaigns;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyStickers {

    public static CollectedStickers getStickersFromSharedPref(Context collectedActivity) {
        SharedPreferences sharedStickers = collectedActivity.getSharedPreferences(GlobalData.mfilename, 0);
        String jsonDataString = sharedStickers.getString(GlobalData.mCollected, "");
        Gson gson = new Gson();
        if(jsonDataString.equals(""))
            return new CollectedStickers();
        return gson.fromJson(jsonDataString, CollectedStickers.class);
    }

    public static void addSharedCollected(Campaign toAdd, Context collectedActivity){
        SharedPreferences sharedStickers = collectedActivity.getSharedPreferences(GlobalData.mfilename, 0);
        String jsonDataString = sharedStickers.getString(GlobalData.mCollected, "");
        Gson gson = new Gson();
        CollectedStickers cs = gson.fromJson(jsonDataString, CollectedStickers.class);
        if(cs == null) {
            cs = new CollectedStickers();
        }
        cs.collected.add(toAdd);
        String newJsonString = gson.toJson(cs);
        SharedPreferences.Editor edit = sharedStickers.edit();
        edit.putString(GlobalData.mCollected, newJsonString);
        edit.commit();
        CollectedCampaign.saveCollected(toAdd);
    }

    public static void addSharedCurrCampaigns(ArrayList<Campaign> toAdd, Context context){
        SharedPreferences sharedStickers = context.getSharedPreferences(GlobalData.mfilename, 0);
        String jsonDataString = sharedStickers.getString(GlobalData.mCurrCampaigns, "");
        Gson gson = new Gson();
        CurrentCampaigns cc = gson.fromJson(jsonDataString, CurrentCampaigns.class);

        if(cc == null) {
            cc = new CurrentCampaigns();
        }
        cc.setCurrentCampaigns(toAdd);

        String newJsonString = gson.toJson(cc);
        SharedPreferences.Editor edit = sharedStickers.edit();
        edit.putString(GlobalData.mCurrCampaigns, newJsonString);
        edit.commit();
    }

    public static CurrentCampaigns getCurrCampaignsFromSharedPref(Context context) {
        SharedPreferences sharedCurrCampaigns = context.getSharedPreferences(GlobalData.mfilename, 0);
        String jsonDataString = sharedCurrCampaigns.getString(GlobalData.mCurrCampaigns, "");
        Gson gson = new Gson();
        if(jsonDataString.equals(""))
            return new CurrentCampaigns();
        return gson.fromJson(jsonDataString, CurrentCampaigns.class);
    }

    public static void resetShared(Context context, int type){
        SharedPreferences sharedStickers = context.getSharedPreferences(GlobalData.mfilename, 0);
        if(type == 1){
            CollectedStickers c = new CollectedStickers();
            Gson gson = new Gson();
            String newJsonString = gson.toJson(c);
            SharedPreferences.Editor edit = sharedStickers.edit();
            edit.putString(GlobalData.mCollected, newJsonString);
            edit.commit();
        }
        else{
            CurrentCampaigns c = new CurrentCampaigns();
            Gson gson = new Gson();
            String newJsonString = gson.toJson(c);
            SharedPreferences.Editor edit = sharedStickers.edit();
            edit.putString(GlobalData.mCurrCampaigns, newJsonString);
            edit.commit();
        }
    }
}
