package com.Veiled.SqlConnection.Tables;

import android.content.Context;
import android.widget.Toast;

import com.Veiled.Activities.IntroActivity;
import com.Veiled.Components.UserCredentials;
import com.Veiled.Utils.SharedPreferences.UserIdSave;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

import java.util.List;

public class UserQuery  implements TableQueryCallback {

    MobileServiceClient serviceClient;
    Context context;
    User curr_user;
    UserCredentials user_cred;
    IntroActivity intro_act;

    public UserQuery(Context i_context, MobileServiceClient i_serviceClient, User i_user,
                     UserCredentials i_user_cred, IntroActivity i_intro_act){
        context = i_context;
        serviceClient = i_serviceClient;
        curr_user = i_user;

        user_cred = i_user_cred;
        intro_act = i_intro_act;
    }

    @Override
    public void onCompleted(List list, int i, Exception e, ServiceFilterResponse serviceFilterResponse) {
        if(list.size() == 0){
            // Register the new user
            serviceClient.getTable(User.class).insert(curr_user, new TableOperationCallback<User>() {
                public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        // Insert succeeded
                        user_cred.setId(curr_user.id);
                        Toast.makeText(context, "Also registered in our application!", Toast.LENGTH_LONG).show();

                        // set as shared preference
                        UserIdSave.setIdToSharedPref(user_cred.getId(), context);

                        intro_act.jumpToMainActivity();
                    } else {
                        Toast.makeText(context, "Cannot register in our application!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            User user = (User)list.get(0);
            user_cred.setId(user.id);

            //Toast.makeText(context, user_cred.getId(), Toast.LENGTH_LONG).show();

            // set as shared preference
            UserIdSave.setIdToSharedPref(user_cred.getId(), context);

            // set preferences
            MobileServiceTable<User_pref> userPrefTable = serviceClient.getTable(User_pref.class);

            userPrefTable
                    .where()
                    .field("user_id")
                    .eq(user_cred.getId())
                    .execute(new User_prefSharedQuery(context, serviceClient, intro_act));
        }
    }
}
