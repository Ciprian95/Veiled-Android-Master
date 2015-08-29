package com.Veiled.Utils;

public class SensorModifier {
    private double last_roll;
    private double last_rotation;
    public static double roll;
    public static double rotation;
    private boolean here = false;
    public SensorModifier(){

    }

    public void modify(float[] sensorValues) {
        if (sensorValues == null)
            return;

        roll = sensorValues[0];
        rotation = sensorValues[1];


        // a bit processing TODO think about it

        if(last_rotation != 0 ){
            double alpha = 0.01;
            rotation = rotation + alpha * (rotation - last_rotation);
        }

        if(last_roll != 0 && Math.abs(roll-last_roll) < 1 ){
            roll = FilterFunctions.CosineInterpolate(last_roll, roll, 0.15);
        }else if( Math.abs(roll-last_roll) >= 1 ) {
            roll = FilterFunctions.CosineInterpolate(last_roll, roll, 0.25);
        }
        last_rotation = rotation;
        last_roll = roll;
    }
}
