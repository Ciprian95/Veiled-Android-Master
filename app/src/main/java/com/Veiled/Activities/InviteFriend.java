package com.Veiled.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Veiled.R;

/**
 * Created by Laur on 5/14/2015.
 */
public class InviteFriend extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView friendsIconIV = (ImageView)findViewById(R.id.friendsIconIV);
        friendsIconIV.getLayoutParams().height = height / 15;
        friendsIconIV.getLayoutParams().width =  height / 15;

        ImageView backButtonFriends = (ImageView)findViewById(R.id.backButtonFriends);
        backButtonFriends.getLayoutParams().height = height / 15;
        backButtonFriends.getLayoutParams().width =  height / 15;
        backButtonFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView friendsTV = (TextView)findViewById(R.id.friendsTV);
        friendsTV.getLayoutParams().height = height / 15;
        //friendsTV.setTextSize(width/40);

        LinearLayout emptyBlackLineLayoutFriends = (LinearLayout)findViewById(R.id.emptyBlackLineLayoutFriends);
        emptyBlackLineLayoutFriends.getLayoutParams().height = height/100;

        TextView shareToFriendsTV = (TextView)findViewById(R.id.shareToFriendsTV);
        shareToFriendsTV.getLayoutParams().height = height / 6;
        shareToFriendsTV.getLayoutParams().width = width;
        //shareToFriendsTV.setTextSize(width/50);

        ImageView qrcodeIV = (ImageView)findViewById(R.id.qrcodeIV);
        qrcodeIV.getLayoutParams().width = 2 * width / 3;
        qrcodeIV.getLayoutParams().height = 2 * width / 3;
    }



    @Override
    public void onBackPressed() {
        finish();
        Intent setIntent = new Intent(getApplicationContext(), NewMenu.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
