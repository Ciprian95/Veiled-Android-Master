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
public class HelpActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView helpIconIV = (ImageView)findViewById(R.id.helpIconIV);
        helpIconIV.getLayoutParams().height = height / 15;
        helpIconIV.getLayoutParams().width =  height / 15;

        ImageView backButtonHelp = (ImageView)findViewById(R.id.backButtonHelp);
        backButtonHelp.getLayoutParams().height = height / 15;
        backButtonHelp.getLayoutParams().width =  height / 15;
        backButtonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView helpTV = (TextView)findViewById(R.id.helpTV);
        helpTV.getLayoutParams().height = height / 15;
        //helpTV.setTextSize(width/40);

        LinearLayout emptyBlackLineLayout = (LinearLayout)findViewById(R.id.emptyBlackLineLayoutHelp);
        emptyBlackLineLayout.getLayoutParams().height = height/100;


        TextView mainscreenTV = (TextView)findViewById(R.id.mainscreenTV);
        mainscreenTV.getLayoutParams().height = height / 15;
        //mainscreenTV.setTextSize(width/40);

        ImageView mainscreenPicCameraIV = (ImageView)findViewById(R.id.mainscreenPicCameraIV);
        mainscreenPicCameraIV.getLayoutParams().height = width/2;
        mainscreenPicCameraIV.getLayoutParams().width = width/2;

        ImageView mainscreenPicPrefsIV = (ImageView)findViewById(R.id.mainscreenPicPrefsIV);
        mainscreenPicPrefsIV.getLayoutParams().height = width/4;
        mainscreenPicPrefsIV.getLayoutParams().width = width/2;

        //TextView mainscreenDetailsTV = (TextView)findViewById(R.id.mainscreenDetailsTV);
        //mainscreenDetailsTV.setTextSize(width/70);

        LinearLayout emptyBlackLineLayoutHelp2 = (LinearLayout)findViewById(R.id.emptyBlackLineLayoutHelp2);
        emptyBlackLineLayoutHelp2.getLayoutParams().height = height/100;

        TextView settingsHelpTV = (TextView)findViewById(R.id.settingsHelpTV);
        settingsHelpTV.getLayoutParams().height = height / 15;
        //settingsHelpTV.setTextSize(width/40);

        ImageView settingsHelpIV = (ImageView)findViewById(R.id.settingsHelpIV);
        settingsHelpIV.getLayoutParams().height = height / 15;
        settingsHelpIV.getLayoutParams().width = height / 15;

        //TextView settingsHelpDetailsTV = (TextView)findViewById(R.id.settingsHelpDetailsTV);
        //settingsHelpDetailsTV.setTextSize(width/70);

        LinearLayout emptyBlackLineLayoutHelp3 = (LinearLayout)findViewById(R.id.emptyBlackLineLayoutHelp3);
        emptyBlackLineLayoutHelp3.getLayoutParams().height = height/100;

        TextView howToCollectHelpTV = (TextView)findViewById(R.id.howToCollectHelpTV);
        howToCollectHelpTV.getLayoutParams().height = height / 15;
        //howToCollectHelpTV.setTextSize(width/40);

        ImageView howToCollectHelpIV = (ImageView)findViewById(R.id.howToCollectHelpIV);
        howToCollectHelpIV.getLayoutParams().height = width/2;
        howToCollectHelpIV.getLayoutParams().width = width/2;

        //TextView howToCollectDetailsHelpTV = (TextView)findViewById(R.id.howToCollectDetailsHelpTV);
        //howToCollectDetailsHelpTV.setTextSize(width/70);

        LinearLayout emptyBlackLineLayoutHelp4 = (LinearLayout)findViewById(R.id.emptyBlackLineLayoutHelp4);
        emptyBlackLineLayoutHelp4.getLayoutParams().height = height/100;

        TextView wherecanIseeHelpTV = (TextView)findViewById(R.id.wherecanIseeHelpTV);
        wherecanIseeHelpTV.getLayoutParams().height = height / 15;
        //wherecanIseeHelpTV.setTextSize(width/50);

        ImageView whereCanISeeHelpIV = (ImageView)findViewById(R.id.whereCanISeeHelpIV);
        whereCanISeeHelpIV.getLayoutParams().height = height / 15;
        whereCanISeeHelpIV.getLayoutParams().width = height / 15;

        ImageView whereCanISeeHelpIV2 = (ImageView)findViewById(R.id.whereCanISeeHelp2IV);
        whereCanISeeHelpIV2.getLayoutParams().height = height / 15;
        whereCanISeeHelpIV2.getLayoutParams().width = height / 15;

        //TextView wherecanIseeHelpDetailsTV = (TextView)findViewById(R.id.wherecanIseeHelpDetailsTV);
        //wherecanIseeHelpDetailsTV.setTextSize(width/70);

        LinearLayout emptyBlackLineLayoutHelp5 = (LinearLayout)findViewById(R.id.emptyBlackLineLayoutHelp5);
        emptyBlackLineLayoutHelp5.getLayoutParams().height = height/100;

        TextView shareHelpTV = (TextView)findViewById(R.id.shareHelpTV);
        shareHelpTV.getLayoutParams().height = height / 15;
        //shareHelpTV.setTextSize(width/50);

        ImageView shareHelpIV = (ImageView)findViewById(R.id.shareHelpIV);
        shareHelpIV.getLayoutParams().height = width/2;
        shareHelpIV.getLayoutParams().width = width/2;

        //TextView shareHelpDetailsTV = (TextView)findViewById(R.id.shareHelpDetailsTV);
        //shareHelpDetailsTV.setTextSize(width/70);

        ImageView shareLogoHelpIV = (ImageView)findViewById(R.id.shareLogoHelpIV);
        shareLogoHelpIV.getLayoutParams().height = height / 15;
        shareLogoHelpIV.getLayoutParams().width = height / 15;

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent setIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
