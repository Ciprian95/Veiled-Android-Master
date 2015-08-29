package com.Veiled.Components.ConnectionChecker;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Laur on 3/18/2015.
 */
public class ConnectionChecker implements IConnectionChecker {
    @Override
    public boolean isNetworkAvailable(Context i_context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) i_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
