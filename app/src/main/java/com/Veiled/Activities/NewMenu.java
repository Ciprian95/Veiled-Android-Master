package com.Veiled.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.Veiled.Activities.Old.MessageViewer;
import com.Veiled.Activities.Old.MyLocationListener;
import com.Veiled.Adapters.LeftSidePanelAdapter;
import com.Veiled.Components.ScreenDetails.IScreenDetails;
import com.Veiled.Components.ScreenDetails.ScreenDetails;
import com.Veiled.Components.UserCredentials;
import com.Veiled.R;
import com.Veiled.SqlConnection.ConnectionEstablisher;
import com.Veiled.SqlConnection.Tables.Preference;
import com.Veiled.SqlConnection.Tables.User;
import com.Veiled.SqlConnection.Tables.UserQuery;
import com.Veiled.SqlConnection.Tables.User_pref;
import com.Veiled.SqlConnection.Tables.User_prefQuery;
import com.Veiled.Utils.GlobalData;
import com.Veiled.Utils.LeftPanelItemClicker;
import com.Veiled.Utils.PreferencesManipulation;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

/**
 * Created by Laur on 3/19/2015.
 */
public class NewMenu extends Activity {

    // Left side panel
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private IScreenDetails screenDetails;

    private int screen_width;
    private int screen_height;


    // ZOOM
    static final int NONE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu);
        LocationManager locationManager;
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        initScreenDetails();
        //PreferencesManipulation.readPreferences(getApplicationContext());

        // REMOVE THIS ON REFACTOR
        MessageViewer.isMotioned = false;
        //finishAffinity(); TEST

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        screen_width = width;
        screen_height = height;

        ImageView headerIV = (ImageView)findViewById(R.id.headerIV);
        headerIV.getLayoutParams().height = height/2;

        ImageView headerLogoIV = (ImageView)findViewById(R.id.headerLogoIV);
        ((RelativeLayout.LayoutParams)headerLogoIV.getLayoutParams()).topMargin = height/16;
        headerLogoIV.getLayoutParams().height = height/5;
        headerLogoIV.getLayoutParams().width = width;

        UserCredentials enrolled_user = UserCredentials.getUserCredentialsInstance();

        RelativeLayout imgProfile = (RelativeLayout) findViewById(R.id.imgProfile);
        imgProfile.getLayoutParams().height = height/7;

        RelativeLayout newlayout = (RelativeLayout) findViewById(R.id.newlayout);
        newlayout.getLayoutParams().height=height;

        TextView userName = (TextView) findViewById(R.id.userName);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/stentiga.ttf");
        userName.setTypeface(font);
        userName.setText(enrolled_user.getUserName());
        userName.getLayoutParams().width = width/4;
        userName.getLayoutParams().height = height/7;
        //userName.setTextSize(width/50);


        ImageView userPic = (ImageView) findViewById(R.id.userPicture);
        userPic.setImageDrawable(enrolled_user.getUserPicture().getDrawable());
        userPic.getLayoutParams().height = height/8;



        initializeLeftSidePanel();


        // go in best to worst order
        Location current = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        boolean foundLastKnownLocation;
        if(current == null) {
            current = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(current == null) {
                current = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if(current == null) {
                    foundLastKnownLocation = false;
                }
                else {
                    foundLastKnownLocation = true;
                }
            }
            else {
                foundLastKnownLocation = true;
            }
        }
        else {
            foundLastKnownLocation = true;
        }
        if(!foundLastKnownLocation && !locationManager.isProviderEnabled(locationManager.GPS_PROVIDER) &&
                !locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER))
        {
            openDialogLocationServices();
        }

    }

    public void initializeLeftSidePanel(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new LeftSidePanelAdapter(this, NewMenu.this));
        // Set the list's click listener
        LeftPanelItemClicker.OnItemClick(mDrawerList, getApplicationContext(), NewMenu.this);

        final ImageButton showPanel = (ImageButton) findViewById(R.id.showPanel);
        showPanel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        // Toggle efect on left side panel
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            // TODO work with this nice effect on open left side panel
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        final Point details = screenDetails.getScreenDetails();
/*
        // HEADER
        final RelativeLayout headerMenu = (RelativeLayout) findViewById(R.id.headerMenu);
        headerMenu.getLayoutParams().height = details.y / 30;
        headerMenu.getLayoutParams().width = details.x;

        showPanel.getLayoutParams().height = details.y / 14;
        showPanel.getLayoutParams().width = details.x / 14;
*/
        ImageButton accessCameraButton = (ImageButton) findViewById(R.id.accessCameraImageButtonInside);
        accessCameraButton.getLayoutParams().height = details.x - 8*details.x/18;
        accessCameraButton.getLayoutParams().width = details.x - 8*details.x/18;
        accessCameraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goToNextActivity = new Intent(getApplicationContext(), MessageViewer.class); //AearialFinder.class);
                startActivity(goToNextActivity);
            }
        });
        /*
        final RelativeLayout accessCamera = (RelativeLayout)findViewById(R.id.accessCameraImageButton);
        accessCamera.getLayoutParams().width = details.x/2 - 10;
        accessCamera.getLayoutParams().height = details.x/2 - 10;

        UserCredentials enrolled_user = UserCredentials.getUserCredentialsInstance();
        //int[] user_pref = enrolled_user.getPreferences();

*/
    }
    public void openDialogLocationServices() {

        final Dialog dialog = new Dialog(NewMenu.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoglayoutnolocation);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        RelativeLayout dialogLayoutnolocation = (RelativeLayout) dialog.getWindow().findViewById(R.id.dialogLayoutnolocation);
        dialogLayoutnolocation.getLayoutParams().width = 2 * screen_width / 3;
        dialogLayoutnolocation.getLayoutParams().height = screen_height / 2;

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/softelegance.ttf");

        TextView locservText = (TextView) ((RelativeLayout) dialogLayoutnolocation.getChildAt(0)).getChildAt(1);
        locservText.setTypeface(font);

        TextView locservDetailsText = (TextView) dialogLayoutnolocation.getChildAt(1);
        locservDetailsText.setTypeface(font);

        final Button acceptLocServices = (Button) dialog.getWindow().findViewById(R.id.acceptLocServices);
        acceptLocServices.getLayoutParams().width = 2 * screen_width / 12;
        acceptLocServices.getLayoutParams().height = 2 * screen_width / 24;
        acceptLocServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            }
        });

        dialog.show();
    }

    public void initScreenDetails() {
        if (screenDetails == null){
            screenDetails = ScreenDetails.getInstance(this);
        }
    }

    private MobileServiceClient serviceClient;

}

