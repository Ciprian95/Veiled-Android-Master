package com.Veiled.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Veiled.Adapters.CollectedStickerAdapter;
import com.Veiled.R;
import com.Veiled.SqlConnection.Tables.Campaign;
import com.Veiled.SqlConnection.Tables.CollectedCampaign;
import com.Veiled.SqlConnection.Tables.CollectedStickers;
import com.Veiled.Utils.CollectedSticker;
import com.Veiled.Utils.ImageData;
import com.Veiled.Utils.MyStickers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CollectedActivity extends Activity{
    ListView lv;
    Context context;
    private CollectedStickers mcollected = new CollectedStickers();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collected);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ImageView backButtonCol = (ImageView)findViewById(R.id.backButtonCol);
        backButtonCol.getLayoutParams().height = height / 15;
        backButtonCol.getLayoutParams().width =  height / 15;
        backButtonCol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView collectedIconIV = (ImageView)findViewById(R.id.collectedIconIV);
        collectedIconIV.getLayoutParams().height = height / 15;
        collectedIconIV.getLayoutParams().width =  height / 15;

        TextView collectedTV = (TextView)findViewById(R.id.collectedTV);
        collectedTV.getLayoutParams().height = height / 15;
        //collectedTV.setTextSize(width/40);

        LinearLayout emptyBlackLineCollectedLayout = (LinearLayout)findViewById(R.id.emptyBlackLineCollectedLayout);
        emptyBlackLineCollectedLayout.getLayoutParams().height = height/100;

        context=this;

        /*TODO HERE CHECK FOR NO SHARED PREFS*/
        mcollected.setCollected(MyStickers.getStickersFromSharedPref(this).getCollected());
        displayData(width, height);


        Button cancelColl = (Button)findViewById(R.id.cancelCollected);
        cancelColl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyStickers.resetShared(getApplicationContext(), 1);
                lv.setAdapter(new CollectedStickerAdapter());
                lv.invalidateViews();

            }
        });
    }

    public void displayData(int width, int height){

        RelativeLayout containerStickers = (RelativeLayout)findViewById(R.id.stickersContainer);
        containerStickers.getLayoutParams().height = height - height/6;

        ArrayList<CollectedSticker> collectedStickers= new ArrayList<>();
        //addFakeData(collectedStickers);

        // get current date time
        Date currDate = new Date();
        boolean campaignsArchivedExist = false;

        if(mcollected.collected != null && mcollected.size() != 0)
        {
            for(Campaign s : mcollected.getCollected()){
                // is still active
                if(currDate.compareTo(s.end_date) < 0) {
                    collectedStickers.add(new CollectedSticker(
                            s.Name, s.description, s.location,
                            s.end_date.toLocaleString() + "",
                            s.campaign_id,
                            ImageData.getUriFromFile(s.image_id+"_promo", 1), s.image_id)
                            );
                            //R.drawable.mocha_whip));

                    campaignsArchivedExist = true;
                }
            }
        }

        if (!campaignsArchivedExist) {
            ImageView noStickersIV = (ImageView)findViewById(R.id.noStickersIV);
            noStickersIV.getLayoutParams().width = width;
            noStickersIV.getLayoutParams().height = 2 * width/3;
            noStickersIV.setVisibility(View.VISIBLE);

            TextView noStickersTV = (TextView)findViewById(R.id.noStickersTV);
            //noStickersTV.setTextSize(width/40);
            noStickersTV.setVisibility(View.VISIBLE);

            Button cancelCollected = (Button)findViewById(R.id.cancelCollected);
            cancelCollected.setVisibility(View.GONE);
         }

        lv=(ListView) findViewById(R.id.stickerslistView);
        lv.setAdapter(new CollectedStickerAdapter(this, collectedStickers, height, width));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mcollected.setCollected(MyStickers.getStickersFromSharedPref(this).getCollected());
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent setIntent = new Intent(getApplicationContext(), NewMenu.class);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    /*
    private void addFakeData(ArrayList<CollectedSticker> collectedStickers){
        collectedStickers.add(new CollectedSticker("Campaign no 1", "Details for campaign no 1", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.cupcake_red_velvet));
        collectedStickers.add(new CollectedSticker("Campaign no 2", "Details for campaign no 2", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.mocha_whip));
        collectedStickers.add(new CollectedSticker("Campaign no 3", "Details for campaign no 3", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.cocacola));
        collectedStickers.add(new CollectedSticker("Campaign no 4", "Details for campaign no 4", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.cookie));
        collectedStickers.add(new CollectedSticker("Campaign no 5", "Details for campaign no 5", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.cupcake_red_velvet));
        collectedStickers.add(new CollectedSticker("Campaign no 6", "Details for campaign no 6", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.mocha_whip));
        collectedStickers.add(new CollectedSticker("Campaign no 7", "Details for campaign no 7", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.cocacola));
        collectedStickers.add(new CollectedSticker("Campaign no 8", "Details for campaign no 8", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.cookie));
        collectedStickers.add(new CollectedSticker("Campaign no 9", "Details for campaign no 9", "Piata Unirii, Bucharest",
                "06.09.2015", "Carrefour Orhideea", R.drawable.cupcake_red_velvet));
    }
    */

}
