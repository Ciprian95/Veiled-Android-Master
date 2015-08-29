package com.Veiled.SqlConnection.Tables;

import android.content.Context;
import android.location.Location;

import com.Veiled.Utils.MyStickers;

import java.util.ArrayList;

public class CurrentCampaigns {
    private ArrayList<Campaign> campaigns;

    public CurrentCampaigns(){
        campaigns = new ArrayList<>();
    }

    public void setCurrentCampaigns(ArrayList<Campaign> coll){
        campaigns = new ArrayList<>(coll);
    }
    public ArrayList<Campaign> getCurrentCampaigns(){
        return campaigns;
    }
    public void addCampaign(Campaign campaign){
        campaigns.add(campaign);
    }
    public int size(){
        return campaigns.size();
    }

    public Campaign getCampaignWhithinRange(Location location, Context context){
        CollectedStickers coll = MyStickers.getStickersFromSharedPref(context);
        for(Campaign c : campaigns){
            boolean isWithin = isWithinRange(c, location);
            if(isWithin && !coll.containsCampaign(c)){
                return c;
            }
        }
        return null;
    }

    private boolean isWithinRange(Campaign c, Location location){
        return (location.getLatitude() <= c.latitude + 0.01) &&
                (location.getLatitude() >= c.latitude - 0.01) &&
                (location.getLongitude() <= c.longitude + 0.01) &&
                (location.getLongitude() >= c.longitude - 0.01);
    }
}
