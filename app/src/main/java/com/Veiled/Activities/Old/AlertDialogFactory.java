package com.Veiled.Activities.Old;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.Veiled.Activities.MainMenuActivity;

/**
 * Created by Laur on 11/14/2014.
 */
public class AlertDialogFactory {

    public static AlertDialog.Builder CreateAlertDialog(final Activity curr_activity, Context context,
                                                        String title_message, String message_content) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title_message);
        // set dialog message
        alertDialogBuilder
                .setMessage(message_content)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        curr_activity.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        return  alertDialogBuilder;
    }

    public static AlertDialog.Builder CreateAlertDialogOneButtonMessage(final Activity curr_activity, Context context,
                                                       String title_message, String message_content,final int value_type) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(title_message);
        // set dialog message
        alertDialogBuilder
                .setMessage(message_content)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();

                        if(value_type == 2){
                            //Starting a new Intent
                            Intent nextScreen = new Intent(curr_activity.getApplicationContext(), MainMenuActivity.class);
                            curr_activity.startActivity(nextScreen);
                        }
                    }
                });

        return  alertDialogBuilder;
    }
}
