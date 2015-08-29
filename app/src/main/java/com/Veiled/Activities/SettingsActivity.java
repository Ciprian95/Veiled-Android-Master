package com.Veiled.Activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Veiled.Geofencing.GeofenceRequester;
import com.Veiled.Geofencing.GeofenceUtils;
import com.Veiled.R;
import com.Veiled.SyncAdapter.SyncUtils;
import com.Veiled.Utils.CustomSlideView.SliderView;
import com.Veiled.Utils.SharedPreferences.NotificationIntervalSave;
import com.facebook.login.widget.LoginButton;

/**
 * Created by Laur on 4/28/2015.
 */
public class SettingsActivity  extends Activity {

    private int weekDaysSH, weekDaysEH, weekEndsSH, weekEndsEH;


    // Sync campaigns
    public static GeofenceRequester mGeofenceRequester;
    private SettingsActivity.GeofenceSampleReceiver mBroadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView settingsIconIV = (ImageView)findViewById(R.id.settingsIconIV);
        settingsIconIV.getLayoutParams().height = height / 15;
        settingsIconIV.getLayoutParams().width =  height / 15;

        ImageView backButton = (ImageView)findViewById(R.id.backButton);
        backButton.getLayoutParams().height = height / 15;
        backButton.getLayoutParams().width =  height / 15;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView settingsTV = (TextView)findViewById(R.id.settingsTV);
        settingsTV.getLayoutParams().height = height / 15;
        //settingsTV.setTextSize(width/40);

        LinearLayout emptyBlackLineLayout = (LinearLayout)findViewById(R.id.emptyBlackLineLayout);
        emptyBlackLineLayout.getLayoutParams().height = height/100;

        LinearLayout emptyBlackLineLayout2 = (LinearLayout)findViewById(R.id.emptyBlackLineLayout2);
        emptyBlackLineLayout2.getLayoutParams().height = height/100;
        LinearLayout emptyBlackLineLayout3 = (LinearLayout)findViewById(R.id.emptyBlackLineLayout3);
        emptyBlackLineLayout3.getLayoutParams().height = height/100;

        RelativeLayout headerNotLinLay = (RelativeLayout)findViewById(R.id.headerNotLinLay);
        headerNotLinLay.getLayoutParams().height = height / 15;

        TextView notificationsTV = (TextView)findViewById(R.id.notificationsTV);
        notificationsTV.getLayoutParams().height = height / 15;
        //notificationsTV.setTextSize(width/40);

        TextView syncCampaignsTV = (TextView)findViewById(R.id.syncCampaignsTV);
        syncCampaignsTV.getLayoutParams().height = height / 15;
        //syncCampaignsTV.setTextSize(width/40);

        /*
        Button syncCampaignsB = (Button)findViewById(R.id.syncCampaignsB);
        syncCampaignsB.getLayoutParams().height = width / 8;
        syncCampaignsB.getLayoutParams().width = width / 8;

        Button unSyncCampaignsB = (Button)findViewById(R.id.unSyncCampaignsB);
        unSyncCampaignsB.getLayoutParams().height = width / 8;
        unSyncCampaignsB.getLayoutParams().width = width / 8;
        */
        ImageView syncCampaignIV = (ImageView)findViewById(R.id.syncCampaignsIV);
        syncCampaignIV.getLayoutParams().width = width/2;
        syncCampaignIV.getLayoutParams().height = width/2;

        final ImageButton syncCampaignIB = (ImageButton)findViewById(R.id.syncCampaignsIB);
        syncCampaignIB.getLayoutParams().width = width/4;
        syncCampaignIB.getLayoutParams().height = width/4;

        Button saveHours = (Button)findViewById(R.id.saveHours);
        saveHours.getLayoutParams().height = height / 25;
        saveHours.getLayoutParams().width = width / 8;
        saveHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] hours = {weekDaysSH + "", weekDaysEH + "",weekEndsSH + "", weekEndsEH + "" };
                NotificationIntervalSave.setNotificationIntervalsToSharedPref(hours, getApplicationContext());
                Toast.makeText(getApplicationContext(), "Notification intervals saved !" , Toast.LENGTH_LONG).show();
           }
        });

        //saveHours.setTextSize(width/70);

        TextView notificationsDetailsTV = (TextView)findViewById(R.id.notificationsDetailsTV);
        notificationsDetailsTV.getLayoutParams().height = height / 10;
        //notificationsDetailsTV.setTextSize(width/60);

        TextView syncDetailsTV = (TextView)findViewById(R.id.syncDetailsTV);
        syncDetailsTV.getLayoutParams().height = height / 10;
        //syncDetailsTV.setTextSize(width/60);

        TextView manageAccountTV = (TextView)findViewById(R.id.manageAccountTV);
        manageAccountTV.getLayoutParams().height = height / 15;
        //manageAccountTV.setTextSize(width/40);

        TextView workdaysTV = (TextView)findViewById(R.id.workdaysTV);
        workdaysTV.getLayoutParams().height = height / 15;
        //workdaysTV.setTextSize(width/60);

        TextView weekendsTV = (TextView)findViewById(R.id.weekendsTV);
        weekendsTV.getLayoutParams().height = height / 15;
        //weekendsTV.setTextSize(width/60);

        // Used for sync campaigns
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create account, if needed
        SyncUtils.CreateSyncAccount(this);

        syncCampaignIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //SyncUtils.TriggerRefresh();
                SyncUtils.TriggerManualrefresh();
                // this is wtf
                SyncUtils.TriggerUnrefresh();

                syncCampaignIB.setBackgroundResource(R.drawable.button_pressed);
                syncCampaignIB.setEnabled(false);
            }
        });

        /*
        unSyncCampaignsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SyncUtils.TriggerUnrefresh();
            }
        });
        */

        // Create a new broadcast receiver to receive updates from the listeners and service
        mBroadcastReceiver = new SettingsActivity.GeofenceSampleReceiver();

        String[] hours = NotificationIntervalSave.getNotificationIntervalsFromSharedPref(this);

        // not set
        if(hours[0] ==""){
            weekDaysSH = 0;
            weekDaysEH = 24;
            weekEndsSH = 0;
            weekEndsEH = 24;
        }
        else {
            weekDaysSH = Integer.parseInt(hours[0]);
            weekDaysEH = Integer.parseInt(hours[1]);
            weekEndsSH = Integer.parseInt(hours[2]);
            weekEndsEH = Integer.parseInt(hours[3]);
        }

        // first group with slider
        SliderView slider = (SliderView)findViewById(R.id.slider);
        slider.setIntervalHours(weekDaysSH,weekDaysEH);
        final TextView text_interval =  (TextView)findViewById(R.id.text_interval);
        SetUpSlider(slider, text_interval, weekDaysSH, weekDaysEH);
        slider.setOnKnobValuesChangedListener(new SliderView.KnobValuesChangedListener() {

            @Override
            public void onValuesChanged(boolean knobStartChanged,
                                        boolean knobEndChanged, int knobStart, int knobEnd) {
                if(knobStartChanged || knobEndChanged) {
                    text_interval.setText("Interval from " + knobStart + ":00 to  " + knobEnd + ":00 ");
                    setValuesWeekdays(knobStart, knobEnd);
                }
            }
        });

        // second group with slider
        SliderView slider2 = (SliderView)findViewById(R.id.slider2);
        slider2.setIntervalHours(weekEndsSH,weekEndsEH);
        final TextView text_interval2 =  (TextView)findViewById(R.id.text_interval2);
        SetUpSlider(slider2, text_interval2, weekEndsSH, weekEndsEH);
        slider2.setOnKnobValuesChangedListener(new SliderView.KnobValuesChangedListener() {

            @Override
            public void onValuesChanged(boolean knobStartChanged,
                                        boolean knobEndChanged, int knobStart, int knobEnd) {
                if(knobStartChanged || knobEndChanged) {
                    text_interval2.setText("Interval from " + knobStart + ":00 to  " + knobEnd + ":00 ");
                    setValuesWeekends(knobStart, knobEnd);
                }
            }
        });

        final LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        Button fakeLogin = (Button)findViewById(R.id.fakeLogin);
        fakeLogin.getLayoutParams().height = height / 25;
        fakeLogin.getLayoutParams().width = width / 6;
        fakeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    loginButton.callOnClick();

                    Intent settingsIntent = new Intent(getApplicationContext(), IntroActivity.class);
                    settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(settingsIntent);
            }
        });
    }

    private void SetUpSlider(SliderView slider, final TextView text_interval,final int startHour,final int endHour) {
        //size of screen and knob
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        //text_interval.setTextSize(width/80);
        text_interval.setText("Interval from " + startHour + ":00 to  " + endHour + ":00 ");

        Bitmap knobImage = BitmapFactory.decodeResource(getResources(), R.drawable.patrat); // R.drawable.knob);

        //we use the sizes for the slider
        ViewGroup.LayoutParams params = slider.getLayoutParams();
        params.width = width;
        params.height = 2*knobImage.getHeight();
        slider.setLayoutParams(params );
    }

    public void  setValuesWeekdays(int knobStart,int  knobEnd){
        weekDaysSH = knobStart;
        weekDaysEH = knobEnd;
    }

    public void  setValuesWeekends(int knobStart,int  knobEnd){
        weekEndsSH = knobStart;
        weekEndsEH = knobEnd;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent setIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }



    // Sync campaigns
    /**
     * Define a Broadcast receiver that receives updates from connection listeners and
     * the geofence transition service.
     */
    public class GeofenceSampleReceiver extends BroadcastReceiver {
        /*
         * Define the required method for broadcast receivers
         * This method is invoked when a broadcast Intent triggers the receiver
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            // Check the action code and determine what to do
            String action = intent.getAction();

            // Intent contains information about errors in adding or removing geofences
            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

                handleGeofenceError(context, intent);

                // Intent contains information about successful addition or removal of geofences
            } else if (
                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
                            ||
                            TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

                handleGeofenceStatus(context, intent);

                // Intent contains information about a geofence transition
            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

                handleGeofenceTransition(context, intent);

                // The Intent contained an invalid action
            } else {
                Log.e(GeofenceUtils.APPTAG, getString(R.string.invalid_action_detail, action));
                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
            }
        }

        /**
         * If you want to display a UI message about adding or removing geofences, put it here.
         *
         * @param context A Context for this component
         * @param intent The received broadcast Intent
         */
        private void handleGeofenceStatus(Context context, Intent intent) {

        }

        /**
         * Report geofence transitions to the UI
         *
         * @param context A Context for this component
         * @param intent The Intent containing the transition
         */
        private void handleGeofenceTransition(Context context, Intent intent) {
            /*
             * If you want to change the UI when a transition occurs, put the code
             * here. The current design of the app uses a notification to inform the
             * user that a transition has occurred.
             */
        }

        /**
         * Report addition or removal errors to the UI, using a Toast
         *
         * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
         */
        private void handleGeofenceError(Context context, Intent intent) {
            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
            Log.e(GeofenceUtils.APPTAG, msg);
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }
}
