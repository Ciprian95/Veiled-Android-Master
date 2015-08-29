package com.Veiled.SqlConnection.Tables;

import android.app.Service;
import android.content.Context;
import android.content.IntentFilter;
import android.text.format.DateUtils;


import com.Veiled.Activities.SettingsActivity;
import com.Veiled.Components.UserCredentials;
import com.Veiled.Geofencing.GeofenceRemover;
import com.Veiled.Geofencing.GeofenceRequester;
import com.Veiled.Geofencing.GeofenceUtils;
import com.Veiled.Geofencing.SimpleGeofence;
import com.Veiled.Geofencing.SimpleGeofenceStore;
import com.Veiled.R;
import com.Veiled.Utils.DownloadImageTask;
import com.Veiled.Utils.MyStickers;
import com.Veiled.Utils.PreferencesManipulation;
import com.google.android.gms.location.Geofence;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CampaignQueryService implements TableQueryCallback {
    private Context m_context;
    private Service m_service;

    // GEOFENCING STUFF
    private List<SimpleGeofence> mUIGeofences;

    // Persistent storage for geofences
    private SimpleGeofenceStore mPrefs;

    // Store a list of geofences to add
    List<Geofence> mCurrentGeofences;

    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 24;
    private static final long GEOFENCE_DAY_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;

    // An intent filter for the broadcast receiver
    private IntentFilter mIntentFilter;
    // decimal formats for latitude, longitude, and radius
    private DecimalFormat mLatLngFormat;
    private DecimalFormat mRadiusFormat;

    private CollectedStickers mcollected = new CollectedStickers();


    public CampaignQueryService(Context context, Service service){
            m_context = context;
            m_service = service;
    }


    @Override
    public void onCompleted(List result, int count, Exception exception, ServiceFilterResponse response) {

        ArrayList<Campaign> mresult = (ArrayList)result;

        // Set the pattern for the latitude and longitude format
        String latLngPattern = m_context.getString(R.string.lat_lng_pattern);
        // Set the format for latitude and longitude
        mLatLngFormat = new DecimalFormat(latLngPattern);
        // Localize the format
        mLatLngFormat.applyLocalizedPattern(mLatLngFormat.toLocalizedPattern());
        // Set the pattern for the radius format
        String radiusPattern = m_context.getString(R.string.radius_pattern);
        // Set the format for the radius
        mRadiusFormat = new DecimalFormat(radiusPattern);
        // Localize the pattern
        mRadiusFormat.applyLocalizedPattern(mRadiusFormat.toLocalizedPattern());
        // Create an intent filter for the broadcast receiver
        mIntentFilter = new IntentFilter();
        // Action for broadcast Intents that report successful addition of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);
        // Action for broadcast Intents that report successful removal of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);
        // Action for broadcast Intents containing various types of geofencing errors
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);
        // All Location Services sample apps use this category
        mIntentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);
        // Instantiate a new geofence storage area
        mPrefs = new SimpleGeofenceStore(m_context);
        // Instantiate the current List of geofences
        mCurrentGeofences = new ArrayList<>();

        mUIGeofences = new ArrayList<>();

        int counter = 1;

        if(mresult != null){

         // remove the collected ones
         mcollected.setCollected(MyStickers.getStickersFromSharedPref(m_context).getCollected());
         ArrayList<Campaign> filtered_result = new ArrayList<>();
         for(Campaign campaign : mresult){
             if(!mcollected.containsCampaign(campaign)){
                 //mresult.remove(campaign);
                 filtered_result.add(campaign);
             }
         }
         mresult = filtered_result;

         PreferencesManipulation.readPreferences(m_context);
         UserCredentials enrolled_user = UserCredentials.getUserCredentialsInstance();
         int[] user_pref = enrolled_user.getPreferences();

         ArrayList<Campaign> filtered_result_byprefs = new ArrayList<>();
         for(Campaign campaign : mresult){
            if(user_pref[(int)campaign.preference_id - 1] == 1){
                filtered_result_byprefs.add(campaign);
            }
         }
         mresult = filtered_result_byprefs;


         for(Campaign campaign : mresult) {
             Date date = new Date();
             long diff =  campaign.end_date.getTime() - date.getTime();
             long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

             String notificationMessage = campaign.Name;


            // geofence not expired
             if(days > 0) {
                 SimpleGeofence mUIGeofence = new SimpleGeofence(notificationMessage,
                         campaign.latitude,
                         campaign.longitude,
                         1500,  // meters  - 1000
                         GEOFENCE_DAY_EXPIRATION_IN_MILLISECONDS * days,
                         // Only detect entry transitions
                         Geofence.GEOFENCE_TRANSITION_ENTER);
                 mPrefs.setGeofence(Integer.toString(counter), mUIGeofence);
                 mUIGeofences.add(mUIGeofence);
                 counter++;
             }
         }
        }

        /*
         * Add Geofence objects to a List. toGeofence()
         * creates a Location Services Geofence object from a
         * flat object
         */
        for (SimpleGeofence curr_geofence : mUIGeofences) {
            mCurrentGeofences.add(curr_geofence.toGeofence());
        }

        // Start the request. Fail if there's already a request in progress
        try {
           SettingsActivity.mGeofenceRequester = new GeofenceRequester(m_service);
           SettingsActivity.mGeofenceRequester.addGeofences(mCurrentGeofences);
        } catch (UnsupportedOperationException e) {
        }

        if(mresult != null) {
            for (Campaign campaign : mresult) {
                new DownloadImageTask(campaign.image_id, 0).execute(campaign.image_id);
            }

            // Save Campaigns to Shared Preferences
            MyStickers.addSharedCurrCampaigns(mresult, m_context);
        }
    }
}
