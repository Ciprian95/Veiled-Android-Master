package com.Veiled.Activities.Old;

import android.location.Location;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageQuery implements TableQueryCallback {
    List<Message> m_messages;
    MessageViewer m_activity;

    boolean isAlreadyInitialized = false;

    public MessageQuery(MessageViewer currentActivity){
        m_messages = new ArrayList<Message>();
        m_activity = currentActivity;
    }

    @Override
    public void onCompleted(List list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
        m_messages = list;
        final Location currentLocation = GpsPositioning.getCurrentLocation();
        /*used for getting the most appropiate message possition*/
        Collections.sort(m_messages, new Comparator<Message>() {
            public int compare(Message m1, Message m2) {

                double userLatitude = currentLocation.getLatitude();
                double userLongitude = currentLocation.getLongitude();

                double distMessage1 = Message.calculateMessageUserDistance(userLatitude, userLongitude, m1);
                double distMessage2 = Message.calculateMessageUserDistance(userLatitude, userLongitude, m2);

                if (distMessage1 == distMessage2)
                    return 0;
                return distMessage1 < distMessage2 ? -1 : 1;
            }
        });
    /*
        if(isAlreadyInitialized == false) {
            m_activity.AddMessages(m_messages);
            isAlreadyInitialized = true;
        }*/
    }
}
