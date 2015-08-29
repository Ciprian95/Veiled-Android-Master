package com.Veiled.Activities.Old;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

public class GpsPositioning {
    private static Location currentLocation;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    public static void setCurrentLocation(Location location){
        //if(isBetterLocation(currentLocation, location) || currentLocation == null)
        currentLocation = location;
    }

    public static Location getCurrentLocation(){
        return currentLocation;
    }

    protected static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

/*
    private LocationManager locationManager;
    private String bestProvider;

    public Location getLastKnownLocation() {
        return locationManager.getLastKnownLocation(bestProvider);
    }

    public GpsPositioning(Activity currentActivity, Context currentContext){
        Criteria criteria;
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        locationManager = (LocationManager) currentActivity.getSystemService(currentContext.LOCATION_SERVICE);
        bestProvider = locationManager.getBestProvider(criteria, false);
    }

    public LocationManager getLocationMananger(){
        return locationManager;
    }

    public String getBestProvider(){
        LocationProvider provider = locationManager.getProvider()
        return bestProvider;
    }
    public void requestLocationUpdates(LocationListener currentListener) {
        locationManager.requestLocationUpdates(bestProvider, 10000, 1,  currentListener);
    }*/



}
