package com.Veiled.SqlConnection.Tables;

import java.util.ArrayList;

public class CollectedStickers {
    public ArrayList<Campaign> collected;

    public void setCollected(ArrayList<Campaign> coll){
        collected = new ArrayList<>(coll);
    }
    public CollectedStickers(){
        collected = new ArrayList<>();
    }
    public ArrayList<Campaign> getCollected(){
        return collected;
    }

    public int size(){
        return collected.size();
    }

    public boolean containsCampaign(Campaign campaign){
        for(Campaign c : collected){
            if(c.id.equals(campaign.id))
                return true;
        }
        return false;
    }
}
