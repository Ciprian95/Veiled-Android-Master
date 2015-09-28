package com.Veiled.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Veiled.Activities.Old.MessageViewer;
import com.Veiled.Adapters.LeftSidePanelAdapter;
import com.Veiled.Components.ScreenDetails.IScreenDetails;
import com.Veiled.Components.ScreenDetails.ScreenDetails;
import com.Veiled.Components.UserCredentials;
import com.Veiled.R;
import com.Veiled.SqlConnection.ConnectionEstablisher;
import com.Veiled.SqlConnection.Tables.User_pref;
import com.Veiled.SqlConnection.Tables.User_prefQuery;
import com.Veiled.Utils.LeftPanelItemClicker;
import com.Veiled.Utils.PreferencesManipulation;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;

public class PreferencesActivity extends Activity {

    ImageButton[] categsButtons ;
    int[] categIds = new int[]{
            R.drawable.icon2,   R.drawable.icon3,  R.drawable.icon4,  R.drawable.icon5,  R.drawable.icon6,
            R.drawable.icon7,  R.drawable.icon8,  R.drawable.icon9,  R.drawable.icon10,  R.drawable.icon11
    };
    int[] categIdsChecked = new int[]{
            R.drawable.icon2c,   R.drawable.icon3c,  R.drawable.icon4c,  R.drawable.icon5c,  R.drawable.icon6c,
            R.drawable.icon7c,  R.drawable.icon8c,  R.drawable.icon9c,  R.drawable.icon10c,  R.drawable.icon11c
    };
    int[] categIdsNormal = new int[]{
            R.drawable.icon2n,   R.drawable.icon3n,  R.drawable.icon4n,  R.drawable.icon5n,  R.drawable.icon6n,
            R.drawable.icon7n,  R.drawable.icon8n,  R.drawable.icon9n,  R.drawable.icon10n,  R.drawable.icon11n
    };
    // Categories
    private String[] categories = {"Food", "Drinks", "Games", "Electronics", "Cinema",
            "Theatre", "Travel", "Fashion", "Culture", "Coffee"};
    private IScreenDetails screenDetails;

    private int screen_width;

    private int screen_height;

    private MobileServiceClient serviceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        ImageView backButtonPref = (ImageView) findViewById(R.id.backButtonPref);

        LocationManager locationManager;
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        backButtonPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initScreenDetails();
        PreferencesManipulation.readPreferences(getApplicationContext());

        // REMOVE THIS ON REFACTOR
        MessageViewer.isMotioned = false;


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        screen_width = width;
        screen_height = height;

        initialize();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent setIntent = new Intent(getApplicationContext(), NewMenu.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    public void initialize(){



        final Point details = screenDetails.getScreenDetails();


        RelativeLayout[] categs = new RelativeLayout[]{(RelativeLayout)findViewById(R.id.categFood),
                (RelativeLayout)findViewById(R.id.categDrinks), (RelativeLayout)findViewById(R.id.categGames),
                (RelativeLayout)findViewById(R.id.categElectronics),(RelativeLayout)findViewById(R.id.categCinema),
                (RelativeLayout)findViewById(R.id.categTheatre), (RelativeLayout)findViewById(R.id.categTravel),
                (RelativeLayout)findViewById(R.id.categFashion), (RelativeLayout)findViewById(R.id.categCulture),
                (RelativeLayout)findViewById(R.id.categCoffee)
        };

        categsButtons  = new ImageButton[]{(ImageButton)findViewById(R.id.categFoodIB),
                (ImageButton)findViewById(R.id.categDrinksIB), (ImageButton)findViewById(R.id.categGamesIB),
                (ImageButton)findViewById(R.id.categElectronicsIB), (ImageButton)findViewById(R.id.categCinemaIB),
                (ImageButton)findViewById(R.id.categTheatreIB), (ImageButton)findViewById(R.id.categTravelIB),
                (ImageButton)findViewById(R.id.categFashionIB), (ImageButton)findViewById(R.id.categCultureIB),
                (ImageButton)findViewById(R.id.categCoffeeIB)
        };



        UserCredentials enrolled_user = UserCredentials.getUserCredentialsInstance();
        int[] user_pref = enrolled_user.getPreferences();

        for(int i = 0 ; i < categs.length ; i++){
            categs[i].getLayoutParams().width = details.x/4 - 10; // - paddingLeft
            categs[i].getLayoutParams().height =  details.x/4 - 10;//details.y/8 - 10; // - paddingTop
            final int index = i;

            categsButtons[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    openDialog(index);
                }
            });


            if(user_pref[i] == 0){
                categsButtons[i].setBackgroundResource(categIds[i]);
            }
            else{ // 1
                categsButtons[i].setBackgroundResource(categIdsChecked[i]);
            }

        }
    }

    private void openDialog(int index){
        final Dialog dialog = new Dialog(PreferencesActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoglayout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        RelativeLayout dialogLayout = (RelativeLayout)dialog.getWindow().findViewById(R.id.dialogLayout);
        dialogLayout.getLayoutParams().width = 2 * screen_width /3;
        dialogLayout.getLayoutParams().height = screen_height/2;

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/softelegance.ttf");

        ImageView categPic = (ImageView)((RelativeLayout)dialogLayout.getChildAt(0)).getChildAt(0);
        categPic.setBackgroundResource(categIdsNormal[index]);

        TextView categText = (TextView)((RelativeLayout)dialogLayout.getChildAt(0)).getChildAt(1);
        categText.setText(categories[index]);
        categText.setTypeface(font);

        ImageView dismissBut = (ImageView)((RelativeLayout)dialogLayout.getChildAt(0)).getChildAt(2);
        dismissBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView categDetails = (TextView)dialogLayout.getChildAt(1);
        categDetails.setTypeface(font);
        categDetails.setText(getResources().getStringArray(R.array.categ_details)[index]);

        final int curr_index = index;
        final Button btnDismiss = (Button)dialog.getWindow().findViewById(R.id.dismiss);
        btnDismiss.getLayoutParams().width = 2 * screen_width /12;
        btnDismiss.getLayoutParams().height =  2 * screen_width /24;

        UserCredentials enrolled_user = UserCredentials.getUserCredentialsInstance();
        int[] prefs = enrolled_user.getPreferences();
        if(prefs[index] == 1)
            btnDismiss.setText("Unselect");

        btnDismiss.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                savePreferenceForUserEnrolled(curr_index, dialog);
                btnDismiss.setEnabled(false);
            }});

        dialog.show();
    }

    public void openDialogLocationServices(){

        final Dialog dialog = new Dialog(PreferencesActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialoglayoutnolocation);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        RelativeLayout dialogLayoutnolocation = (RelativeLayout)dialog.getWindow().findViewById(R.id.dialogLayoutnolocation);
        dialogLayoutnolocation.getLayoutParams().width = 2 * screen_width /3;
        dialogLayoutnolocation.getLayoutParams().height = screen_height/2;

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/softelegance.ttf");

        TextView locservText = (TextView)((RelativeLayout)dialogLayoutnolocation.getChildAt(0)).getChildAt(1);
        locservText.setTypeface(font);

        TextView locservDetailsText = (TextView)dialogLayoutnolocation.getChildAt(1);
        locservDetailsText.setTypeface(font);

        final Button acceptLocServices = (Button)dialog.getWindow().findViewById(R.id.acceptLocServices);
        acceptLocServices.getLayoutParams().width = 2 * screen_width /12;
        acceptLocServices.getLayoutParams().height =  2 * screen_width /24;
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

    private void savePreferenceForUserEnrolled(int index, Dialog dialog){
        if(serviceClient == null)
            serviceClient = ConnectionEstablisher.getMobileService();

        UserCredentials enrolled_user = UserCredentials.getUserCredentialsInstance();
        int[] prefs = enrolled_user.getPreferences();

        MobileServiceTable<User_pref> userPrefTable = serviceClient.getTable(User_pref.class);
        User_pref curr_userpref = new User_pref(enrolled_user.getId(), index + 1);

        // check
        if(prefs[index] == 0) {
            userPrefTable
                    .where()
                    .field("user_id")
                    .eq(enrolled_user.getId())
                    .and()
                    .field("preference_id")
                    .eq(index + 1)
                    .execute(new User_prefQuery(getApplicationContext(), serviceClient, curr_userpref, dialog,
                            categsButtons[index], categIdsChecked[index], false));
        }
        // uncheck
        else{
            userPrefTable
                    .where()
                    .field("user_id")
                    .eq(enrolled_user.getId())
                    .and()
                    .field("preference_id")
                    .eq(index + 1)
                    .execute(new User_prefQuery(getApplicationContext(), serviceClient, curr_userpref, dialog,
                            categsButtons[index], categIds[index], true));
        }
    }
}
