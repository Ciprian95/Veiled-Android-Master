package com.Veiled.Activities.Old;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.*;
import com.Veiled.R;
import com.Veiled.SqlConnection.Tables.Campaign;
import com.Veiled.Utils.FilterFunctions;
import com.Veiled.Utils.ImageData;

public class SensorManagement {
    // Phone sensors
    private boolean alreadyThere = false;
    private boolean alreadyThere2 = false;
    private double lastRollPosition;
    float rotation_angle = 0;


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetic;

    // Values from sensors
    float[] mGravity;
    float[] mGeomagnetic;

    // Last values from sensors
    float last_azimuth;
    float last_pitch;
    float last_roll;

    static final float ALPHA = 0.35f;
    static final float BETA = 0.05f;
    static final float ALPHA2 = 0.55f;

    public void InitializeSensors(Activity curr_activity, Context curr_context){
        mSensorManager = (SensorManager) curr_activity.getSystemService(curr_context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void ResumeActivityRegisterSensors(Activity curr_activity){
        mSensorManager.registerListener((SensorEventListener) curr_activity, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener((SensorEventListener) curr_activity,mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void UnregisterSensors(Activity curr_activity){
        mSensorManager.unregisterListener((SensorEventListener) curr_activity,mAccelerometer);
        mSensorManager.unregisterListener((SensorEventListener) curr_activity,mMagnetic);
    }

    public float[] GetSensorValues(SensorEvent curr_event) {
        // last_roll, rotation
        float[] sensorFinalValues = new float[2];
        float pitch = curr_event.values[1];
        float roll = curr_event.values[2];

        if (last_azimuth == last_pitch && last_azimuth == last_roll) {
            last_pitch = pitch;
            last_roll = roll;
            return null;
        }

        last_pitch = last_pitch + ALPHA * (pitch - last_pitch);

        if (curr_event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = curr_event.values;
            last_roll = last_roll + ALPHA * (roll - last_roll);
            sensorFinalValues[0] = Math.round(last_roll);
        }

        if (curr_event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = curr_event.values;

        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float R2[] = new float[9];
            float I[] = new float[9];

            if (SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic)) {
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X, R2);

                float orientation[] = new float[3];
                SensorManager.getOrientation(R2, orientation);

                float azimut = orientation[0];
                if(last_azimuth != 0)
                    last_azimuth = last_azimuth + ALPHA * (azimut - last_azimuth);
                else
                    last_azimuth = azimut;
                float rotation = -last_azimuth * 360 / (2 * 3.14159f);

                sensorFinalValues[1] = Math.round(rotation);
            }

        }
        return  sensorFinalValues;
    }

    /*added roll/rotation change methods*/
    public void setLastRotation(double rotation, int number, double rotationInArray, ArrayList<Double> lastPositionArray){
        double position_in_screen = rotation - rotationInArray;
        double difference = Math.abs(position_in_screen - lastPositionArray.get(number));
        if(difference > 10 && difference < 60) {
            lastPositionArray.set(number, lastPositionArray.get(number) + ALPHA2 * (position_in_screen - lastPositionArray.get(number)));
            alreadyThere = false;
        }
        else if(difference < 10) {
            lastPositionArray.set(number, lastPositionArray.get(number) + BETA * (position_in_screen - lastPositionArray.get(number)));
            alreadyThere = false;
        }
        else if(difference > 60){
            if(alreadyThere)
                lastPositionArray.set(number, position_in_screen);
            else
                alreadyThere = true;
        }
    }

    public void setLastRoll(double roll, int number, ArrayList<Double> lastRollArray){
        double positionInScreenRoll;
        positionInScreenRoll = lastRollArray.get(number) - roll;
        if(lastRollPosition == 0)
            lastRollPosition = positionInScreenRoll;

        double differenceRoll = Math.abs(positionInScreenRoll - lastRollPosition);
        if (lastRollPosition != 0 && differenceRoll > 2 && differenceRoll < 5) {
            //lastRollPosition = lastRollPosition + ALPHA * (positionInScreenRoll - lastRollPosition);
            lastRollPosition = FilterFunctions.CosineInterpolate(lastRollPosition, positionInScreenRoll, 0.35);
            alreadyThere2 = false;
        } else if (differenceRoll <= 2) {
            //lastRollPosition = lastRollPosition + BETA * (positionInScreenRoll - lastRollPosition);
            lastRollPosition = FilterFunctions.CosineInterpolate(lastRollPosition, positionInScreenRoll, 0.15);
            alreadyThere2 = false;
        } else if (differenceRoll > 5) {
            if (alreadyThere2)
                lastRollPosition = positionInScreenRoll;
            else
                alreadyThere2 = true;
        }
    }

    public void setImagesOnCamera(int number, ArrayList<ImageView> imageViews, ArrayList<Double> lastPositionArray,
                                  Uri image_uri){
        if(imageViews.get(number) != null) {
            RelativeLayout.LayoutParams params =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
            int width = MessageViewer.width;
            int height = MessageViewer.height;
            int widthRatio = 7;
            int heightRatio = 12;

            int elemSizeWidth = width / widthRatio;
            int elemSizeHeight = height / heightRatio;

            int lastLeftMargin = 0;

            // sticker for the screen
            if(number == 0){
                lastLeftMargin = ((RelativeLayout.LayoutParams)imageViews.get(number).getLayoutParams()).leftMargin;

                params.leftMargin = width / 4 + elemSizeWidth + (int) Math.round(lastPositionArray.get(number)) * 10; //* 20;
                params.rightMargin = width / 4 + elemSizeWidth - (int) Math.round(lastPositionArray.get(number))* 10; //* 20;

                params.topMargin = height / 4 + elemSizeHeight + (int) Math.round(lastRollPosition * 75); //* 150);
                //pus asta
                params.bottomMargin = height / 4 + elemSizeHeight - (int) Math.round(lastRollPosition * 75); //* 150);
                params.height = 2 * elemSizeHeight;
                params.width = 2 * elemSizeWidth;
            }
            if(number == 1){
                elemSizeWidth = 3 * width / 4;
                elemSizeHeight = height / 2;

                lastLeftMargin = ((RelativeLayout.LayoutParams)imageViews.get(number).getLayoutParams()).leftMargin;

                params.leftMargin = (width - elemSizeWidth)/2 + (int) Math.round(lastPositionArray.get(number)) * 10; //* 20;
                params.rightMargin = (width - elemSizeWidth)/2 - (int) Math.round(lastPositionArray.get(number))* 10; //* 20;

                params.topMargin =  elemSizeWidth + (int) Math.round(lastRollPosition * 75); //* 150);
                //pus asta
                params.bottomMargin = /*(width - elemSizeWidth)  -*/ (int) Math.round(lastRollPosition * 75); //* 150);

                params.height = elemSizeHeight;
                params.width = elemSizeWidth;
            }

            if(number == 2){
                elemSizeWidth = 2 * width / 3;
                //elemSizeHeight = 3 * height / 4;

                lastLeftMargin = ((RelativeLayout.LayoutParams)imageViews.get(number).getLayoutParams()).leftMargin;

                params.leftMargin =  20;//(width - elemSizeWidth)   ;
                ///params.rightMargin =  (width - elemSizeWidth)/2 + (width - elemSizeWidth)/6   ;

                params.topMargin = 20;//elemSizeHeight; //* 150);
                //params.bottomMargin = height / 4 + elemSizeHeight; //* 150);

                params.height = 2 * elemSizeHeight;
                params.width = elemSizeWidth;
            }


            if (number == 0)
                imageViews.get(number).setImageURI(image_uri);
                                    //setImageResource(R.drawable.cupcake_red_velvet); //R.drawable.cupcake);
            if(number == 1) {
                imageViews.get(number).setImageResource(R.drawable.character_man_thinker);
            }
            if(number == 2){
                imageViews.get(number).setImageResource(R.drawable.drag_cloud);
            }

           imageViews.get(number).setLayoutParams(params);

            // rotate only the sticker
            if(number == 0) {
                rotation_angle = (lastLeftMargin - params.leftMargin) * 2;
                if (rotation_angle > 0)
                    rotation_angle = 7;
                else if (rotation_angle < 0)
                    rotation_angle = -7;

                if (rotation_angle != 0)
                    imageViews.get(number).setRotation(rotation_angle);

            }
        }
    }


}
