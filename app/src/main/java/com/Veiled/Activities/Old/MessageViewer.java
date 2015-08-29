package com.Veiled.Activities.Old;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Criteria;

import com.Veiled.Activities.MainMenuActivity;
import com.Veiled.Animations.PopOnScreenShakeAnimation;
import com.Veiled.Animations.TouchListener;
import com.Veiled.R;
import com.Veiled.SqlConnection.Tables.Campaign;
import com.Veiled.SqlConnection.Tables.CurrentCampaigns;
import com.Veiled.Utils.ImageData;
import com.Veiled.Utils.MyStickers;
import com.Veiled.Utils.StickerCheck;
import com.Veiled.Utils.SensorModifier;

public class MessageViewer extends Activity implements SurfaceHolder.Callback,SensorEventListener {
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    boolean previewing = false;
    private LocationManager locationManager;
    private Location currentLocation;
    StickerCheck checker;
    StickerCheck checker_character_reading;
    ArrayList<StickerCheck> checker_stickers;
    ArrayList<Boolean> isStickerVisibleArray;

    private ArrayList<ImageView>imageViews;
    private ArrayList<Double> toMessageRotation;
    private ArrayList<Double> lastRollArray;
    private ArrayList<Double> lastPositionArray;
    private TextView textSensorValues;
    private float[] sensorValues;
    private SensorManagement sensorManager;
    final Context context = this;
    public static int width;
    public static int height;

    private SensorModifier modifier;

    private boolean beenHere = false;
    //private boolean isStickerVisible = false;
    private ImageView basketView;
    private ImageView stickerView;
    public static boolean isMotioned = false;
    private ArrayList<Double> last_values ;
    private MediaPlayer mp;
    private boolean previewIsRunning;

    private Uri image_uri;
    private Campaign currentCampaign;

    private static final String TAG = "MessageViewer";
    int SDK_INT = android.os.Build.VERSION.SDK_INT;
    int MIN_SDK_INT = 18; // Android 4.3  -- JELLY_BEAN_MR2

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmessagescamera);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.camerapreview2);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);


        Log.d(TAG, "SDK version is" + SDK_INT);
        modifier = new SensorModifier();
        // set last values array of limited number of values
        last_values = new ArrayList<>();
        //get gps position
        //maybe move to app init

        locationManager = (LocationManager) this.getSystemService(context.LOCATION_SERVICE);
        MyLocationListener myListener = new MyLocationListener();
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, myListener);
        //locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, myListener);
        locationManager.requestLocationUpdates(locationManager.PASSIVE_PROVIDER, 0, 0, myListener);
        currentLocation = GpsPositioning.getCurrentLocation();

        Criteria criteria;
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);

        if(currentLocation == null){
            Toast.makeText(getApplicationContext(), "Location service is loading data. Please return in few seconds !",Toast.LENGTH_LONG).show();
            super.onBackPressed();
        }
        else {
            InitializeLists(2);

            // textview sensors
            textSensorValues = new TextView(getApplicationContext());
            textSensorValues.setTextColor(Color.parseColor("#21406E"));
            RelativeLayout.LayoutParams params2 =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            //params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            textSensorValues.setLayoutParams(params2);

            // sensors
            sensorValues = new float[2];
            sensorManager = new SensorManagement();
            sensorManager.InitializeSensors(this, this);
            checker = new StickerCheck(-50, 0);

            checker_character_reading = new StickerCheck(40,0); //30,0
            checker_stickers.add(checker);
            checker_stickers.add(checker_character_reading);
            checker_stickers.add(new StickerCheck(-50, 0));

            // query the stored campaigns
            CurrentCampaigns allCampaigns = MyStickers.getCurrCampaignsFromSharedPref(this);
            currentCampaign = allCampaigns.getCampaignWhithinRange(currentLocation, this);
            AddCampaign(currentCampaign);
        }

    }

    Button fakeCampaign;
    boolean wannaFake = false;
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FORESLASH = "/";

    RelativeLayout viewMessagesLayout;
    private void AddCampaign(Campaign currentCampaign) {
        viewMessagesLayout = new RelativeLayout(this);
        viewMessagesLayout.setMinimumWidth(width);
        viewMessagesLayout.setMinimumHeight(height);

        LayoutParams layoutParamsControl
                = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        this.addContentView(viewMessagesLayout, layoutParamsControl);

        if(currentCampaign == null){
            Toast.makeText(getApplicationContext(), "No aerials around !!", Toast.LENGTH_SHORT).show();

            if(fakeCampaign == null) {
                fakeCampaign = new Button(getApplicationContext());
                fakeCampaign.setTextColor(Color.parseColor("#21406E"));
                fakeCampaign.setText("Fake a campaign");
                RelativeLayout.LayoutParams params2 =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);

                params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                //params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                fakeCampaign.setLayoutParams(params2);
                fakeCampaign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fakeCampaign.setVisibility(View.GONE);
                        wannaFake = true;
                        image_uri = Uri.parse(ANDROID_RESOURCE + context.getPackageName()
                                + FORESLASH + R.drawable.cupcake_red_velvet);

                        AddCampaign(new Campaign("Wanna play ha?","fakeID", "fakisimo", new Date(), new Date()));
                    }
                });
                viewMessagesLayout.addView(fakeCampaign);
            }
            return;
        }
        else{
            Toast.makeText(getApplicationContext(), "Good luck in finding " + currentCampaign.Name + " aerial", Toast.LENGTH_LONG).show();
        }

        if(!wannaFake)
            image_uri = ImageData.getUriFromFile(currentCampaign.image_id, 0);

        GetPhoneWidthHeight();

        /*initialize the lists to messages.size size*/
        double to_message_rotation;

        //StickerCheck class has the implementation for to_message_rotation calculation
        ImageView imageMessage = new ImageView(this);
        //use the overloaded method for different rotation from 90 degrees
        to_message_rotation = checker.getRotationToDisplay();

        stickerView = imageMessage;
        imageViews.add(imageMessage);
        toMessageRotation.add(to_message_rotation);
        lastPositionArray.add(to_message_rotation);
        lastRollArray.add((double)0);

        // test something TODO remove if necessary
        ImageView character_reading = new ImageView(this);
        viewMessagesLayout.addView(character_reading);
        imageViews.add(character_reading);

        toMessageRotation.add((double)30);
        lastPositionArray.add((double)30);
        lastRollArray.add((double)0);

        ImageView drag_text = new ImageView(this);
        viewMessagesLayout.addView(drag_text);
        imageViews.add(drag_text);

        toMessageRotation.add(to_message_rotation);
        lastPositionArray.add(to_message_rotation);
        lastRollArray.add((double)0);

        viewMessagesLayout.addView(imageMessage);

        if(!beenHere) {
            viewMessagesLayout.addView(textSensorValues);
            beenHere = true;
        }

        basketView = new ImageView(this);
        basketView.setImageResource(R.drawable.move_big);
        basketView.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = 0;
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.width = width/4;
        params.height= height/4;

        basketView.setLayoutParams(params);
        viewMessagesLayout.addView(basketView);

        basketView.setVisibility(View.INVISIBLE);

        TouchListener m_listener;
        m_listener = new TouchListener(getApplicationContext(),MessageViewer.this, stickerView, basketView, width, height, currentCampaign, sensorManager, wannaFake);

        //viewMessagesLayout.setOnTouchListener(m_listener);
        surfaceView.setOnTouchListener(m_listener);
        if(stickerView != null ) {
            stickerView.setOnTouchListener(m_listener);
        }
        else
            Toast.makeText(getApplicationContext(), "No aerials around !!", Toast.LENGTH_SHORT).show();


    }

    private void GetPhoneWidthHeight(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    private void InitializeLists(int size){
        imageViews = new ArrayList<>(size);
        toMessageRotation = new ArrayList<>(size);
        lastPositionArray = new ArrayList<>(size);
        lastRollArray = new ArrayList<>(size);

        checker_stickers = new ArrayList<>(size);
        isStickerVisibleArray = new ArrayList<>(size);

        isStickerVisibleArray.add(false);
        isStickerVisibleArray.add(false);
        isStickerVisibleArray.add(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                //Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        this.setCameraDisplayOrientation(cameraId, this.camera);

        if(previewing){
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null){
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        myStartPreview();
    }

    public void myStartPreview() {
        if (!previewIsRunning && (camera != null)) {
            try{
                camera.setPreviewDisplay(surfaceHolder);
            }catch(IOException e){

            }
            camera.startPreview();
            previewIsRunning = true;
        }
    }

    // same for stopping the preview
    public void myStopPreview() {
        if (previewIsRunning && (camera != null)) {
            camera.stopPreview();
            previewIsRunning = false;
        }
    }

    @SuppressLint("NewApi")
    public void setCameraDisplayOrientation(int cameraId, android.hardware.Camera camera) {
        int rotation = ((WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        try {
            camera.setDisplayOrientation(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        myStartPreview();
        super.onResume();

        /*
        MyLocationListener myListener = new MyLocationListener();
        locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 100, 1, myListener);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 100, 1, myListener);
        */

        sensorManager.ResumeActivityRegisterSensors(this);
        CurrentCampaigns allCampaigns = MyStickers.getCurrCampaignsFromSharedPref(this);
        currentCampaign = allCampaigns.getCampaignWhithinRange(currentLocation, this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
    }

   @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        /*camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;*/

        myStopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(SDK_INT >= MIN_SDK_INT ) {
            sensorValues = sensorManager.GetSensorValues(event);
            modifier.modify(sensorValues);
            int number = -1;

            textSensorValues.setText(Math.round(SensorModifier.roll * 10)/10 + "\n" +
                                     Math.round(SensorModifier.rotation * 10)/10 + "\n");

            if (toMessageRotation == null)
                return;

            for (double rotationInArray : toMessageRotation) {
                number++;


                sensorManager.setLastRotation(SensorModifier.rotation, number, rotationInArray, lastPositionArray);
                sensorManager.setLastRoll(SensorModifier.roll, number, lastRollArray);
                if (checker_stickers.get(number).StickerWithinCamera(SensorModifier.rotation, SensorModifier.roll) && !isStickerVisibleArray.get(number)
                        && checker_stickers.get(number).isVeridicLastValuesArray(last_values)) {
                    isStickerVisibleArray.set(number, true);
                    if (!isMotioned)
                        sensorManager.setImagesOnCamera(number, imageViews, lastPositionArray, image_uri);

                    // real sticker
                    if(number == 0) {
                        PopOnScreenShakeAnimation animation;
                        animation = new PopOnScreenShakeAnimation();
                        animation.SetUpAnimation(getApplicationContext(), stickerView, basketView);

                        mp = MediaPlayer.create(this, R.raw.bbloop);
                        if (mp != null)
                            mp.start();
                    }
                }
                if (checker_stickers.get(number).StickerOutOfCamera(SensorModifier.rotation, SensorModifier.roll))
                    isStickerVisibleArray.set(number, false);

                if (isStickerVisibleArray.get(number)) {
                    if (!isMotioned)
                        sensorManager.setImagesOnCamera(number, imageViews, lastPositionArray, image_uri);
                    imageViews.get(number).setVisibility(View.VISIBLE);
                } else {
                    imageViews.get(number).setVisibility(View.INVISIBLE);

                    // just for 0
                    if(!isStickerVisibleArray.get(0)) {
                        basketView.clearAnimation();
                        basketView.setVisibility(View.INVISIBLE);
                    }
                }
            }
            // shift the arraylist each time -- remove the last element
            if (last_values.size() >= 3) {
                last_values.remove(0);
            }
            last_values.add(SensorModifier.rotation);


        }
        else{
            textSensorValues.setText("Tracking sensors is not accurate !");

            int number = -1;

            if (toMessageRotation == null)
                return;

            for (double rotationInArray : toMessageRotation) {
                number++;
                if (!isStickerVisibleArray.get(number)) {
                    PopOnScreenShakeAnimation animation;
                    animation = new PopOnScreenShakeAnimation();
                    animation.SetUpAnimation(getApplicationContext(), stickerView, basketView);

                    mp = MediaPlayer.create(this, R.raw.bbloop);
                    if (mp != null)
                        mp.start();
                    isStickerVisibleArray.set(number, true);
                }

                //sensorManager.setImagesOnCamera(0, imageViews, lastPositionArray);
                int width = MessageViewer.width;
                int height = MessageViewer.height;
                int widthRatio = 7;
                int heightRatio = 12;

                int elemSizeWidth = width / widthRatio;
                int elemSizeHeight = height / heightRatio;

                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = width / 4;
                params.rightMargin = width / 4;
                params.topMargin = height / 4;
                params.bottomMargin = height / 4;
                params.height = 2 * elemSizeHeight;
                params.width = 2 * elemSizeWidth;

                imageViews.get(number).setLayoutParams(params);
                if(!wannaFake)
                    imageViews.get(number).setImageURI(ImageData.getUriFromFile(currentCampaign.image_id, 0));//setImageResource(R.drawable.cupcake_red_velvet);
                else{
                    image_uri = Uri.parse(ANDROID_RESOURCE + context.getPackageName()
                            + FORESLASH + R.drawable.cupcake_red_velvet);
                    imageViews.get(number).setImageURI(image_uri);  //setImageResource(R.drawable.cupcake_red_velvet);
                }

                imageViews.get(number).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onPause() {
        sensorManager.UnregisterSensors(this);

        myStopPreview();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        sensorManager.UnregisterSensors(this);

        finish();
        Intent setIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

}