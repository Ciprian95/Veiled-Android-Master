package com.Veiled.Utils;

import android.location.Location;

import java.util.ArrayList;
import com.Veiled.Activities.Old.Message;


public class StickerCheck {

    private double rotationToDisplay ;//= 90;
    private double rollToDisplay; //= 0;

    public StickerCheck(double i_rotationToDisplay, double i_rollToDisplay){
        rotationToDisplay = i_rotationToDisplay;
        rollToDisplay = i_rollToDisplay;
    }

    public boolean workingMagnetometer(ArrayList<Double> last_values){
        if(last_values != null && last_values.size() >= 3) {
            return (last_values.get(0).compareTo(last_values.get(1)) != 0) ||
                    (last_values.get(0).compareTo(last_values.get(2)) != 0);
        }
        else
            return true;
    }

    public boolean StickerWithinCamera(double rotation, double roll){
        return (rotation <= rotationToDisplay+25 && rotation >= rotationToDisplay -25)
                && (roll <= rollToDisplay+3 && roll >= rollToDisplay-3);
    }

    public boolean StickerOutOfCamera(double rotation, double roll){
        return (rotation >= rotationToDisplay+50 || rotation <= rotationToDisplay -50)
                || (roll >= rollToDisplay+7 || roll <= rollToDisplay-7); // modified from 30 to 50
        // and from 5 to 7
    }

    public boolean isVeridicLastValuesArray(ArrayList<Double> last_values){
        if(last_values == null || last_values.size() ==0)
            return false;

        for(int i = 0 ; i < last_values.size() ; i++)
        {
            // in screen
            if(last_values.get(i) < rotationToDisplay - 50 || last_values.get(i) > rotationToDisplay + 50)
                return false;
        }
        return true;
    }

    public double getRotationToDisplay(Message message, Location currentLocation){

        // fake location
        //double userLatitude = 44.44489739;
        //double userLongitude = 26.05656421;
        double userLatitude = currentLocation.getLatitude();
        double userLongitude = currentLocation.getLongitude();

        double diffLat = Math.abs(userLatitude - message.latitude);
        double diffLong = Math.abs(userLongitude - message.longitude);
         /*
        folosit pentru a calcula alfa...rotatia mesajului in cadrane
         */
        double to_message_rotation;
        to_message_rotation = 57.2957795 * Math.atan(diffLat / diffLong); // radians
        // N - E
        if (userLongitude < message.longitude && userLatitude < message.latitude) {
            // keeps its value
        } else {
            // N - W
            if (userLongitude < message.longitude && userLatitude > message.latitude) {
                to_message_rotation += 90;
            } else
                // S - W
                if (userLongitude > message.longitude && userLatitude > message.latitude) {
                    to_message_rotation = -90 - to_message_rotation;
                }
                // S - E
                else {
                    to_message_rotation = -to_message_rotation;
                }
        }
        return to_message_rotation;
    }

    public double getRotationToDisplay(){
        return rotationToDisplay;
    }

    public double getRollToDisplay(){
        return rollToDisplay;
    }
}
