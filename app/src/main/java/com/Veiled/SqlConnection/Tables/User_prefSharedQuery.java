package com.Veiled.SqlConnection.Tables;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageButton;

import com.Veiled.Activities.IntroActivity;
import com.Veiled.Utils.PreferencesManipulation;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.ArrayList;
import java.util.List;

public class User_prefSharedQuery implements TableQueryCallback {

    MobileServiceClient serviceClient;
    Context context;
    User_pref curr_userpref;
    IntroActivity introActivity;


    public User_prefSharedQuery(Context i_context, MobileServiceClient i_serviceClient,
                                IntroActivity i_introActivity ) {
        context = i_context;
        serviceClient = i_serviceClient;

        introActivity = i_introActivity;
    }

    @Override
    public void onCompleted(List list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
        ArrayList<User_pref> prefs = (ArrayList)list;

        for(User_pref pref : prefs){
            PreferencesManipulation.changeCurrentPreference(pref.preference_id - 1, 1);
        }
        PreferencesManipulation.savePreferences(context);

        introActivity.jumpToMainActivity();

    }
}