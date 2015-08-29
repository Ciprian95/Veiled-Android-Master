package com.Veiled.Activities.ZoomActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Veiled.R;

import org.w3c.dom.Text;

/**
 * Created by Laur on 4/4/2015.
 */
public class UpdateHandler extends Handler {
    private RelativeLayout categLayout;
    private Context context;
    private int zoomLevel = 0;
    private boolean zoomedOut = false;

    private int baseHeight;
    private int baseWidth;
    private  boolean isSet;
    private final String[] categoriesName = new String[]{"Pizza", "Fashion", "Beauty", "Electronics",
                                                         "Travel", "Art", "Tools"};

    public UpdateHandler(Context i_context, RelativeLayout i_categLayout){
        context = i_context;
        categLayout = i_categLayout;
    }
    @Override
    public void handleMessage(Message msg) {
        if(!isSet){
            baseHeight = categLayout.getChildAt(1).getHeight();
            baseWidth = categLayout.getChildAt(1).getWidth();
            isSet = true;
        }

        switch (msg.what) {
            case 1:
                if(zoomLevel >= 0 && zoomLevel <= 8) {

                    for (int i = 0; i < categLayout.getChildCount(); i++) {
                        final RelativeLayout child = (RelativeLayout) categLayout.getChildAt(i);

                        if(zoomLevel == 0){
                            zoomInLevel1(child, i);
                        }

                        if(zoomLevel == 8){
                            zoomInLevel2(child, i);
                        }
                    }
                    zoomLevel++;
                }
                break;
            // more cases
            case 2:
                if(zoomLevel >= 0 && zoomLevel <= 16) {
                    for (int i = 0; i < categLayout.getChildCount(); i++) {
                        if(zoomLevel == 8) {
                            final RelativeLayout child = (RelativeLayout) categLayout.getChildAt(i);
                            if(child.getChildCount() >=3)
                                child.removeViewAt(2);
                            if(child.getChildCount() >=2)
                                child.removeViewAt(1);
                            zoomInLevel1(child, i);
                        }
                    }

                    if(zoomLevel > 0)
                        zoomLevel--;
                    if (zoomLevel == 0 && !zoomedOut) {
                        zoomedOut = true;
                        for (int i = 0; i < categLayout.getChildCount(); i++) {
                            final RelativeLayout child = (RelativeLayout) categLayout.getChildAt(i);
                            zoomOutLevel1(child, i);
                        }
                  }
                }
                break;
        }

        categLayout.postInvalidate(); // Forces redrawing of all overlays
    }

    private void zoomInLevel1(RelativeLayout child, int i) {
        if (i == 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
            params.height = baseHeight*2 + baseHeight / 2 + baseHeight / 4 + 15;
            params.width = baseWidth*2 + baseWidth / 2;
            child.setLayoutParams(params);

            RelativeLayout.LayoutParams paramsImageButton = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsImageButton.height = params.height - params.height / 6;
            paramsImageButton.width = params.width;
            child.getChildAt(0).setLayoutParams(paramsImageButton);

            TextView details = new TextView(context);
            details.setText("Collect aerials");
            child.addView(details);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.width = params.width;
            params2.height = baseHeight / 2 + 15;
            params2.addRule(RelativeLayout.BELOW, R.id.accessCameraImageButtonInside);
            details.setLayoutParams(params2);

            details.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            child.setBackgroundColor(Color.parseColor("#000000"));

            zoomedOut = false;
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.height = baseHeight + baseHeight / 4 + baseHeight / 8;
            params.width = baseWidth + baseWidth / 4;

            RelativeLayout.LayoutParams paramsImageButton = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsImageButton.height = params.height - params.height / 12;
            paramsImageButton.width = params.width;
            child.getChildAt(0).setLayoutParams(paramsImageButton);

            if (i == 1) {
                params.addRule(RelativeLayout.RIGHT_OF, R.id.accessCameraImageButton);
                params.setMargins(15, 15, 0, 0);
            }

            if (i == 2) {
                params.addRule(RelativeLayout.BELOW, R.id.categFood);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.accessCameraImageButton);
                params.setMargins(15, 15, 0, 0);

            }
            if (i == 3) {
                params.addRule(RelativeLayout.BELOW, R.id.accessCameraImageButton);
                params.setMargins(5, 5, 0, 0);
            }
            if (i == 4) {
                params.addRule(RelativeLayout.BELOW, R.id.accessCameraImageButton);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.categGames);
                params.setMargins(5, 5, 0, 0);
            }
            if (i == 5) {
                params.addRule(RelativeLayout.BELOW, R.id.categDrinks);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.categElectronics);
                params.setMargins(5, 5, 0, 0);
            }
            if (i == 6) {
                params.addRule(RelativeLayout.BELOW, R.id.categGames);
                params.setMargins(5, 5, 0, 0);
            }
            if (i == 7) {
                params.addRule(RelativeLayout.BELOW, R.id.categElectronics);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.categTheatre);
                params.setMargins(5, 5, 0, 0);
            }

            TextView details = new TextView(context);
            details.setText(categoriesName[i - 1]);
            child.addView(details);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.width = params.width;
            params2.height = baseHeight / 4;
            params2.addRule(RelativeLayout.BELOW, child.getChildAt(0).getId());
            details.setLayoutParams(params2);

            details.setGravity(Gravity.CENTER_HORIZONTAL);
            details.setTextSize(details.getTextSize() / 6);
            child.setBackgroundColor(Color.parseColor("#000000"));

            child.setLayoutParams(params);
        }
    }

    private void zoomInLevel2(RelativeLayout child, int i){
        if(i == 0){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
            params.width += 2* baseWidth;
            child.setLayoutParams(params);

            RelativeLayout.LayoutParams paramsImageButton =  new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsImageButton.height = params.height;
            paramsImageButton.width =  2* baseWidth;
            child.getChildAt(0).setLayoutParams(paramsImageButton);

            TextView details = (TextView)child.getChildAt(1);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.width = 2* baseWidth;
            params2.height = params.height/2;
            details.setId(2);
            params2.addRule(RelativeLayout.RIGHT_OF, child.getChildAt(0).getId());
            details.setLayoutParams(params2);

            details.setGravity(Gravity.BOTTOM);
            details.setTextSize(details.getTextSize()/2);
            details.setText("Collect aerials");

            TextView detailsMore = new TextView(context);
            detailsMore.setText("tap to view the available offers around!");
            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params3.width = 2* baseWidth;
            params3.setMargins(20,0,0,0);
            params3.height = params.height/2;
            params3.addRule(RelativeLayout.BELOW, 2);
            params3.addRule(RelativeLayout.RIGHT_OF, child.getChildAt(0).getId());
            detailsMore.setLayoutParams(params3);
            detailsMore.setTextSize(details.getTextSize()/6);
            child.addView(detailsMore);

            Drawable d = context.getResources().getDrawable(android.R.color.holo_red_light);
            d.setAlpha(50);
            child.setBackgroundDrawable(d);

        }
        else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.height = 2* baseHeight;
            params.width = 4* baseWidth;

            RelativeLayout.LayoutParams paramsImageButton = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);

            paramsImageButton.height = params.height;
            paramsImageButton.width =  2* baseWidth;
            child.getChildAt(0).setLayoutParams(paramsImageButton);

            if (i == 1) {
                params.addRule(RelativeLayout.BELOW, R.id.accessCameraImageButton);
                params.setMargins(15, 15, 0, 0);
            }

            if (i == 2) {
                params.addRule(RelativeLayout.BELOW, R.id.categFood);
                params.setMargins(5, 15, 0, 0);

            }
            if (i == 3) {
                params.addRule(RelativeLayout.BELOW, R.id.categDrinks);
                params.setMargins(5, 5, 0, 0);
            }
            if (i == 4) {
                params.addRule(RelativeLayout.BELOW, R.id.categGames);
                 params.setMargins(5, 5, 0, 0);
            }
            if (i == 5) {
                params.addRule(RelativeLayout.BELOW, R.id.categElectronics);
                params.setMargins(5, 5, 0, 0);
            }
            if (i == 6) {
                params.addRule(RelativeLayout.BELOW, R.id.categCinema);
                params.setMargins(5, 5, 0, 0);
            }
            if (i == 7) {
                params.addRule(RelativeLayout.BELOW, R.id.categTheatre);
                params.setMargins(5, 5, 0, 0);
            }


            TextView details = (TextView)child.getChildAt(1);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params2.width = 2* baseWidth;
            params2.height = params.height/2;
            params2.removeRule(RelativeLayout.BELOW);
            params2.addRule(RelativeLayout.RIGHT_OF, child.getChildAt(0).getId());
            details.setLayoutParams(params2);
            details.setGravity(Gravity.BOTTOM);
            details.setTextSize(details.getTextSize());
            details.setText(categoriesName[i - 1]);
            details.setId(5);

            TextView detailsMore = new TextView(context);
            detailsMore.setText("subscribe to category to receive the offers");
            RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params3.width = 2* baseWidth;
            params3.setMargins(20,0,0,0);
            params3.height = params.height/2;
            params3.addRule(RelativeLayout.BELOW, details.getId());
            params3.addRule(RelativeLayout.RIGHT_OF, child.getChildAt(0).getId());
            detailsMore.setLayoutParams(params3);
            detailsMore.setTextSize(details.getTextSize()/6);
            child.addView(detailsMore);

            child.setLayoutParams(params);

            Drawable d = context.getResources().getDrawable(android.R.color.holo_red_light);
            d.setAlpha(50);
            child.setBackgroundDrawable(d);
        }
    }
    private void zoomOutLevel1(RelativeLayout child, int i) {

        if (i == 0) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
            params.height -= (baseHeight/2 + baseHeight/4);
            params.width -= baseWidth/2;
            child.setLayoutParams(params);
            if(child.getChildCount() >= 2)
                child.removeViewAt(1);
        }
        else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.height = baseHeight;
            params.width = baseWidth;

            if(i == 1){
                params.addRule(RelativeLayout.RIGHT_OF,R.id.accessCameraImageButton);
                params.setMargins(5, 15, 0, 0);
            }

            if(i == 2){
                params.addRule(RelativeLayout.RIGHT_OF,R.id.categFood);
                params.setMargins(5, 15, 0, 0);

            }
            if(i == 3){
                params.addRule(RelativeLayout.BELOW, R.id.categFood);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.accessCameraImageButton);
                params.setMargins(5, 15, 0, 0);
            }
            if(i == 4){
                params.addRule(RelativeLayout.BELOW, R.id.categDrinks);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.categGames);
                params.setMargins(5, 10, 0, 0);
            }
            if(i == 5){
                params.addRule(RelativeLayout.BELOW, R.id.accessCameraImageButton);
                params.setMargins(5, 5, 0, 0);
            }
            if(i == 6){
                params.addRule(RelativeLayout.BELOW, R.id.accessCameraImageButton);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.categTravel);
                params.setMargins(5, 5, 0, 0);
            }
            if(i == 7){
                params.addRule(RelativeLayout.BELOW, R.id.categGames);
                params.addRule(RelativeLayout.RIGHT_OF, R.id.categTheatre);
                params.setMargins(5, 10, 0, 0);
            }
            if(child.getChildCount() >= 2)
                child.removeViewAt(1);
            child.setLayoutParams(params);
        }
    }
}
