package com.Veiled.Activities.Old;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Dani on 4/21/2015.
 */
public class MyLocationListener implements LocationListener{
    @Override
    public void onLocationChanged(Location location) {
        GpsPositioning.setCurrentLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
