package com.Veiled.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.Veiled.Activities.ArchivedActivity;
import com.Veiled.Activities.CollectedActivity;
import com.Veiled.Activities.HelpActivity;
import com.Veiled.Activities.InviteFriend;
import com.Veiled.Activities.SettingsActivity;
import com.Veiled.Activities.PreferencesActivity;

public class LeftPanelItemClicker {
    public static void OnItemClick(ListView i_Drawerlist, final Context i_context,final Activity i_currActivity){
        i_Drawerlist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {
                        switch(position) {
                            case 0:
                                Intent collected = new Intent(i_currActivity, CollectedActivity.class);//LogInFragmentActivity.class);
                                i_currActivity.startActivity(collected);
                                break;
                            case 1:
                                Intent archived = new Intent(i_currActivity, ArchivedActivity.class);
                                i_currActivity.startActivity(archived);
                                break;
                            case 2:
                                Intent invite = new Intent(i_currActivity, InviteFriend.class);
                                i_currActivity.startActivity(invite);
                                break;
                            case 3:
                                Intent preferences = new Intent(i_currActivity, PreferencesActivity.class);
                                i_currActivity.startActivity(preferences);
                                break;
                            case 4:
                                Intent settings = new Intent(i_currActivity, SettingsActivity.class);
                                i_currActivity.startActivity(settings);
                                break;

                            case 5:
                                Intent helpIntent = new Intent(i_currActivity, HelpActivity.class);
                                i_currActivity.startActivity(helpIntent);
                                break;
                        }
                    }});
    }
}
