package com.Veiled.SqlConnection.Tables;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.Toast;

import com.Veiled.Utils.PreferencesManipulation;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableDeleteCallback;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

public class User_prefQuery implements TableQueryCallback {

    MobileServiceClient serviceClient;
    Context context;
    User_pref curr_userpref;
    Dialog dialog;

    ImageButton currImage;
    int currResource;
    boolean checked;


    public User_prefQuery(Context i_context, MobileServiceClient i_serviceClient,
                          User_pref i_curr_userpref, Dialog i_dialog, ImageButton i_currImage, int i_currResource,
                          boolean i_checked){
        context = i_context;
        serviceClient = i_serviceClient;

        curr_userpref = i_curr_userpref;
        dialog = i_dialog;

        currImage = i_currImage;
        currResource = i_currResource;

        checked = i_checked;
    }

    @Override
    public void onCompleted(List list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
        if(!checked) {
            // persistent save
            currImage.setBackgroundResource(currResource);

            PreferencesManipulation.changeCurrentPreference(curr_userpref.preference_id - 1, 1);
            PreferencesManipulation.savePreferences(context);

            dialog.dismiss();

            serviceClient.getTable(User_pref.class).insert(curr_userpref, new TableOperationCallback<User_pref>() {
                    public void onCompleted(User_pref entity, Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            // Insert succeeded
                            //Toast.makeText(context, "Preference added !", Toast.LENGTH_LONG).show();

                        } else {
                        }
                    }
                });
        }
        else {
            // persistent save
           currImage.setBackgroundResource(currResource);

            PreferencesManipulation.changeCurrentPreference(curr_userpref.preference_id - 1, 0);
            PreferencesManipulation.savePreferences(context);

            dialog.dismiss();

            if (list.size() > 0) {
                User_pref user_pref = (User_pref)list.get(0);

                serviceClient.getTable(User_pref.class).delete(user_pref, new TableDeleteCallback() {
                    @Override
                    public void onCompleted(Exception exception, ServiceFilterResponse response) {
                        if (exception == null) {
                            // delete
                            //Toast.makeText(context, "Preference removed !", Toast.LENGTH_LONG).show();

                        }else{

                        }
                    }
                });
            }
        }
    }

}