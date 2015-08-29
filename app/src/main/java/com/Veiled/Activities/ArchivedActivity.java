package com.Veiled.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;

import com.Veiled.Adapters.ArchivedStickerAdapter;
import com.Veiled.Adapters.CollectedStickerAdapter;
import com.Veiled.SqlConnection.Tables.Campaign;
import com.Veiled.SqlConnection.Tables.CollectedStickers;
import com.Veiled.Utils.CollectedSticker;
import com.Veiled.Utils.DownloadImageTask;

import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Veiled.R;
import com.Veiled.Utils.GlobalData;
import com.Veiled.Utils.ImageData;
import com.Veiled.Utils.MyStickers;

import java.util.ArrayList;
import java.util.Date;

public class ArchivedActivity extends Activity {
    private CollectedStickers mcollected = new CollectedStickers();
    ListView lv;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archived);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView backButtonArchived = (ImageView)findViewById(R.id.backButtonArchived);
        backButtonArchived.getLayoutParams().height = height / 15;
        backButtonArchived.getLayoutParams().width =  height / 15;
        backButtonArchived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView archivedIconIV = (ImageView)findViewById(R.id.archivedIconIV);
        archivedIconIV.getLayoutParams().height = height / 15;
        archivedIconIV.getLayoutParams().width =  height / 15;

        TextView archivedTV = (TextView)findViewById(R.id.archivedTV);
        archivedTV.getLayoutParams().height = height / 15;
        //archivedTV.setTextSize(width/40);

        LinearLayout emptyBlackLineArchivedLayout = (LinearLayout)findViewById(R.id.emptyBlackLineArchivedLayout);
        emptyBlackLineArchivedLayout.getLayoutParams().height = height/100;

        context=this;

        /*TODO HERE CHECK FOR NO SHARED PREFS*/
        mcollected.setCollected(MyStickers.getStickersFromSharedPref(this).getCollected());
        displayData(width, height);
    }

    public void displayData(int width, int height) {

        RelativeLayout containerStickers = (RelativeLayout) findViewById(R.id.stickersArchivedContainer);
        containerStickers.getLayoutParams().height = height - height / 6;

        ArrayList<CollectedSticker> collectedStickers = new ArrayList<>();
        //addFakeData(collectedStickers);

        // get current date time
        Date currDate = new Date();
        boolean campaignsArchivedExist = false;

        if (mcollected.collected != null && mcollected.size() != 0) {
            for (Campaign s : mcollected.getCollected()) {
                // is still active
                if (currDate.compareTo(s.end_date) >= 0) {
                    collectedStickers.add(new CollectedSticker(
                            s.Name, s.description, s.location,
                            s.end_date.toLocaleString() + "",
                            s.campaign_id,
                            ImageData.getUriFromFile(s.image_id, 1), s.campaign_id));
                }
            }
        }

        if (!campaignsArchivedExist) {
            ImageView noStickersArchivedIV = (ImageView) findViewById(R.id.noStickersArchivedIV);
            noStickersArchivedIV.getLayoutParams().width = width;
            noStickersArchivedIV.getLayoutParams().height = 2 * width/3;
            noStickersArchivedIV.setVisibility(View.VISIBLE);

            TextView noStickersArchivedTV = (TextView) findViewById(R.id.noStickersArchivedTV);
            //noStickersArchivedTV.setTextSize(width / 40);
            noStickersArchivedTV.setVisibility(View.VISIBLE);
        }

        lv = (ListView) findViewById(R.id.stickerslistViewArchived);
        lv.setAdapter(new ArchivedStickerAdapter(this, collectedStickers, height, width));
   }

    @Override
    protected void onResume() {
        super.onResume();
        mcollected.setCollected(MyStickers.getStickersFromSharedPref(this).getCollected());
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent setIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    /*
    private void addFakeData(ArrayList<CollectedSticker> collectedStickers){
        Uri defaultUri = Uri.parse("android.resource://" + context.getPackageName()
                + "/" +  R.drawable.mocha_whip);
        collectedStickers.add(new CollectedSticker("Campaign no 1", "Details for campaign no 1", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea",defaultUri));
        collectedStickers.add(new CollectedSticker("Campaign no 2", "Details for campaign no 2", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea",defaultUri));
        collectedStickers.add(new CollectedSticker("Campaign no 3", "Details for campaign no 3", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea",defaultUri));
        collectedStickers.add(new CollectedSticker("Campaign no 4", "Details for campaign no 4", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", defaultUri));
        collectedStickers.add(new CollectedSticker("Campaign no 5", "Details for campaign no 5", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", defaultUri));
        collectedStickers.add(new CollectedSticker("Campaign no 6", "Details for campaign no 6", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea",defaultUri));
        collectedStickers.add(new CollectedSticker("Campaign no 7", "Details for campaign no 7", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea",defaultUri));
        collectedStickers.add(new CollectedSticker("Campaign no 8", "Details for campaign no 8", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", defaultUri));
        collectedStickers.add(new CollectedSticker("Campaign no 9", "Details for campaign no 9", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", defaultUri));
    }
    */


}
