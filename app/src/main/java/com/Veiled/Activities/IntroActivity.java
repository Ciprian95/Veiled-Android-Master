package com.Veiled.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.Veiled.Activities.Old.MyLocationListener;
import com.Veiled.Components.ConnectionChecker.ConnectionChecker;
import com.Veiled.Components.ConnectionChecker.IConnectionChecker;
import com.Veiled.Components.Rememberer.IRememberer;
import com.Veiled.Components.ScreenDetails.IScreenDetails;
import com.Veiled.Components.ScreenDetails.ScreenDetails;
import com.Veiled.Components.UserCredentials;
import com.Veiled.Geofencing.GeofenceRequester;
import com.Veiled.R;
import com.Veiled.SqlConnection.ConnectionEstablisher;
import com.Veiled.SqlConnection.Tables.User;
import com.Veiled.SqlConnection.Tables.UserQuery;
import com.Veiled.SyncAdapter.SyncUtils;
import com.Veiled.Utils.MyStickers;
import com.Veiled.Utils.SharedPreferences.UserIdSave;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.location.LocationClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class IntroActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    // Facebook
    private CallbackManager callbackManager;
    private ProfilePictureView profilePictureView;
    private TextView greeting;
    private final String PENDING_ACTION_BUNDLE_KEY =
            "com.Veiled.Activities.IntroActivity:PendingAction";
    private PendingAction pendingAction = PendingAction.NONE;
    private enum PendingAction {
        NONE
    }
    private ProfileTracker profileTracker;


    private IScreenDetails screenDetails;
    private IConnectionChecker connectionChecker;
    private IRememberer rememberer;

    private UserCredentials credentials;

    private int screen_width;
    private int screen_height;

    final private Context m_context = this;

    public static GeofenceRequester mGeofenceRequester;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationManager locationManager;
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        MyLocationListener myListener = new MyLocationListener();
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, myListener);
        //locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, myListener);
        locationManager.requestLocationUpdates(locationManager.PASSIVE_PROVIDER, 0, 0, myListener);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        screen_width = width;
        screen_height = height;

        // Used for sync campaigns
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Delete the pending notifications
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(mNotificationManager != null)
            mNotificationManager.cancelAll();

        //Facebook init
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        updateUI();
                    }
                    @Override
                    public void onCancel() {
                        updateUI();
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        updateUI();
                    }
                });

        setContentView(R.layout.intro);

        LoginButton login_button = (LoginButton)findViewById(R.id.login_buttonIntro);
        //login_button.setBackgroundColor(0xFF0000FF);
        //login_button.setPublishPermissions( "publish_actions");
        login_button.setReadPermissions(Arrays.asList("user_status", "user_friends", "public_profile",
                                        "basic_info","email"));

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
            }
        };

        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePictureIntro);
        greeting = (TextView) findViewById(R.id.greeting);

        initScreenDetails();
        scaleLabels();

    }

    public void jumpToMainActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 1 seconds

                // Create account, if needed
                SyncUtils.CreateSyncAccount(m_context);

                SyncUtils.TriggerManualrefresh();
                // sync auto refresh
                SyncUtils.TriggerRefresh();

                //SyncUtils.TriggerUnrefresh();

                Intent goToNextActivity = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(goToNextActivity);
                finish();
            }
        }, 1000);
    }

    public void closeApplication(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 1 seconds
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        }, 1000);
    }

    public void scaleLabels() {
        final Point details = screenDetails.getScreenDetails();
        final ImageView logo_mare = (ImageView) findViewById(R.id.logoImageView);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(details.x / 2, details.y / 2);
                params.setMargins(details.x / 4, details.y / 8, 0, 0);
                logo_mare.setLayoutParams(params);
            }
        });
    }

    public void isRememberChecked() {
        if (rememberer.hasBeenChecked())
            credentials = rememberer.getSavedCredentials();
    }

    public void setRememberer(IRememberer i_rememberer) {
        rememberer = i_rememberer;
    }

    public void setConnectionChecker(IConnectionChecker i_connectionChecker) {
        connectionChecker = i_connectionChecker;
    }

    public void initScreenDetails() {
        if (screenDetails == null) {
            screenDetails = ScreenDetails.getInstance(this);
        }
    }

    public void initScreenDetails(int width, int height) {
        screenDetails = ScreenDetails.getInstance(width, height);
    }

    public IScreenDetails getScreenInstance() {
        return screenDetails;
    }

    public boolean isNetworkConnected() {
        if (connectionChecker == null) {
            connectionChecker = new ConnectionChecker();
        }
        return connectionChecker.isNetworkAvailable(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    // Facebook
    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {
            profilePictureView.setDrawingCacheEnabled(true);
            profilePictureView.setProfileId(profile.getId());

            ImageView user_picture = ((ImageView)profilePictureView.getChildAt(0));
            credentials = new UserCredentials(profile.getName(), "facebook", user_picture);
            UserCredentials.setUserCredentialsInstance(credentials);

            LoginButton login_button = (LoginButton)findViewById(R.id.login_buttonIntro);
            login_button.setVisibility(View.INVISIBLE);

            profilePictureView.setVisibility(View.VISIBLE);

            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));

            if(credentials != null) {

                AccessToken.getCurrentAccessToken().getPermissions();

                // check internet connection
                if(isNetworkConnected()){
                    ConnectionEstablisher.SetConnectionToDatabase(this);
                    String email = profile.getId();
                    User curr_user = new User(email, profile.getFirstName(), profile.getLastName(), "facebook");
                    MobileServiceClient serviceClient = ConnectionEstablisher.getMobileService();

                    String id = UserIdSave.getIdFromSharedPref(getApplicationContext());

                    // No shared preferences
                    if(id == "") {
                        // check if it's registered
                        MobileServiceTable<User> userTable = serviceClient.getTable(User.class);
                        userTable.where()
                                .field("email")
                                .eq(email)
                                .execute(new UserQuery(getApplicationContext(), serviceClient, curr_user, credentials, IntroActivity.this));
                    }
                    else{
                        credentials.setId(id);

                        //Toast.makeText(getApplicationContext(), "Shared : " + credentials.getId(), Toast.LENGTH_LONG).show();
                        this.jumpToMainActivity();
                    }
                }
                else{
                    //Toast.makeText(getApplicationContext(), "No internet connection is available !",Toast.LENGTH_LONG).show();
                    openDialogInternetConnection();
               }
            }

        } else {
            credentials = null;
            UserCredentials.setUserCredentialsInstance(credentials);

            profilePictureView.setProfileId(null);
            greeting.setText(null);

            profilePictureView.setVisibility(View.INVISIBLE);
        }
    }

    public void openDialogInternetConnection(){

        final Dialog dialog = new Dialog(IntroActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoglayoutnointernet);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        RelativeLayout dialogLayoutnointernet = (RelativeLayout)dialog.getWindow().findViewById(R.id.dialogLayoutnointernet);
        dialogLayoutnointernet.getLayoutParams().width = 2 * screen_width /3;
        dialogLayoutnointernet.getLayoutParams().height = screen_height/2;

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/softelegance.ttf");

        TextView internetText = (TextView)((RelativeLayout)dialogLayoutnointernet.getChildAt(0)).getChildAt(1);
        internetText.setTypeface(font);

        TextView internetDetailsText = (TextView)dialogLayoutnointernet.getChildAt(1);
        internetDetailsText.setTypeface(font);

        final Button acceptInternet = (Button)dialog.getWindow().findViewById(R.id.acceptInternet);
        acceptInternet.getLayoutParams().width = 2 * screen_width /12;
        acceptInternet.getLayoutParams().height =  2 * screen_width /24;
        acceptInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);

            }
        });

        dialog.show();
    }
}
