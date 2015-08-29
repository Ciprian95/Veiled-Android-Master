package com.Veiled.SqlConnection.Tables;

import android.content.Context;

import com.Veiled.Components.UserCredentials;
import com.Veiled.SqlConnection.ConnectionEstablisher;
import com.Veiled.Utils.CloudImage;
import com.Veiled.Utils.DownloadImageTask;
import com.Veiled.Utils.ImageData;
import com.Veiled.Utils.MyStickers;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

public class CollectedCampaign {

    public String id;
    public String user_id;
    public String campaign_id;

    public CollectedCampaign(String user_id, String campaign_id){
        this.user_id = user_id;
        this.campaign_id = campaign_id;
    }

    public static void saveCollected(Campaign toAdd){
        MobileServiceClient serviceClient = ConnectionEstablisher.getMobileService();
        CollectedCampaign mcollect = new CollectedCampaign(UserCredentials.getUserCredentialsInstance().getId(), toAdd.id);
        //new DownloadImageTask(toAdd.id).execute(toAdd.id);
        serviceClient.getTable(CollectedCampaign.class).insert(mcollect, new TableOperationCallback<CollectedCampaign>() {
            public void onCompleted(CollectedCampaign entity, Exception exception, ServiceFilterResponse response) {
                if (exception == null) {
                    // Insert succeeded
                    //Toast.makeText(context, "Preference added !", Toast.LENGTH_LONG).show();
                } else {
                }
            }
        });
    }
}
