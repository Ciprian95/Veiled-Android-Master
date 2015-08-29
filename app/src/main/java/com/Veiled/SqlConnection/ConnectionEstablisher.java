package com.Veiled.SqlConnection;

import android.app.Activity;
import android.app.Service;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

/**
 * Created by Laur on 01/04/2015
 */
public class ConnectionEstablisher {

    private static MobileServiceClient mClient;
    private boolean isSet;

    private String url = "https://veiled.azure-mobile.net/";
    private String connString = "BrWeCcLvUcGVKsTUhLJbDhMAYxXuOU63";

    private ConnectionEstablisher(Activity current_activity){
        if(!isSet){
            try {
                mClient = new MobileServiceClient(url, connString, current_activity);
                isSet = true;
            } catch (MalformedURLException e) {
                mClient = null;
                e.printStackTrace();
                isSet = false;
            }{}
        }
    }

    private ConnectionEstablisher(Service current_service){
        if(!isSet){
            try {
                mClient = new MobileServiceClient(url, connString, current_service);
                isSet = true;
            } catch (MalformedURLException e) {
                mClient = null;
                e.printStackTrace();
                isSet = false;
            }{}
        }
    }

    public static MobileServiceClient getMobileService(){
        return mClient;
    }

    public static ConnectionEstablisher SetConnectionToDatabase(Activity m_activity){
        return new ConnectionEstablisher(m_activity);
    }

    public static ConnectionEstablisher SetConnectionToDatabaseFromService(Service m_service){
        return new ConnectionEstablisher(m_service);
    }
}