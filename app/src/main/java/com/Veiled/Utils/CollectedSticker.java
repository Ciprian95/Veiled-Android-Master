package com.Veiled.Utils;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Laur on 5/11/2015.
 */
public class CollectedSticker {
    private String name;
    private String details;
    private String picked_from;
    private String available_until;
    private String created_by;
    private Uri img_uri;
    private String campaign_id;

    public CollectedSticker( String i_name, String i_details, String i_picked_from, String i_available_until, String i_created_by,
                            Uri i_img_uri, String i_campaign_id){//int i_img_src){
            name = i_name;
            details = i_details;
            picked_from = i_picked_from;
            available_until = i_available_until;
            created_by = i_created_by;
            img_uri = i_img_uri;
            campaign_id = i_campaign_id;
    }

    public String getName(){ return  name;}
    public String getDetails(){return details;}
    public String getPicked_from(){return  picked_from;}
    public String getAvailable_until(){return available_until;}
    public String getCreated_by(){return created_by;}
    public Uri getImg_src(){return img_uri;}
    public String getCampaign_id(){ return campaign_id;}

}
