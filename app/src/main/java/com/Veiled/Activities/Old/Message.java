package com.Veiled.Activities.Old;

public class Message {

        public String Id;
        public String message;
        public double latitude;
        public double longitude;
        public double altitude;

        public Message(String i_message, double i_latitude, double i_longitude, double i_altitude){
            message = i_message;
            latitude = i_latitude;
            longitude = i_longitude;
            altitude = i_altitude;
        }

        public static double calculateMessageUserDistance(double userLatitude, double userLongitude, Message m){
            return Math.sqrt(Math.pow(Math.abs(userLatitude-m.latitude),2)
                    +Math.pow(Math.abs(userLongitude-m.longitude),2));
        }

 }

