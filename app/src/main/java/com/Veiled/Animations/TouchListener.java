package com.Veiled.Animations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Veiled.Activities.MainMenuActivity;
import com.Veiled.Activities.Old.MessageViewer;
import com.Veiled.Activities.Old.SensorManagement;
import com.Veiled.R;
import com.Veiled.SqlConnection.Tables.Campaign;
import com.Veiled.Utils.DownloadImageTask;
import com.Veiled.Utils.MyStickers;

public class TouchListener implements View.OnTouchListener {
    private Context m_context;
    private MessageViewer m_activity;

    private ImageView m_stickerView;
    private ImageView m_basketView;
    private boolean dragging = false;
    private Campaign mCurrentCampaign;
    private int m_width;
    private int m_height;
    private SensorManagement m_sensorManager;
    private boolean inTheBasket = false;

    private Rect hitRect = new Rect();
    private boolean m_isfake;

    public TouchListener(Context context,MessageViewer activity, ImageView stickerView,
                         ImageView basketView, int width, int height, Campaign currentCampaign, SensorManagement sensorManager, boolean isfake){
        m_context = context;
        m_activity = activity;

        m_basketView = basketView;
        m_stickerView = stickerView;

        m_width = width;
        m_height = height;

        m_sensorManager = sensorManager;
        mCurrentCampaign = currentCampaign;

        m_isfake = isfake;

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean eventConsumed = true;
        int x = (int)event.getX();
        int y = (int)event.getY();

        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (v == m_stickerView) {
                MessageViewer.isMotioned = true;
                dragging = true;
                eventConsumed = false;
            }
        } else if (action == MotionEvent.ACTION_UP) {

            if (dragging) {
                m_basketView.getHitRect(hitRect);
                if (hitRect.contains(x, y))
                    setSameAbsoluteLocation(m_stickerView, m_basketView);
            }
            if(!inTheBasket)
                MessageViewer.isMotioned = false;
            dragging = false;
            eventConsumed = false;

        } else if (action == MotionEvent.ACTION_MOVE) {
            if (v != m_stickerView) {
                if (dragging) {
                    setAbsoluteLocationCentered(m_stickerView, x, y);
                }
            }
        }
        return eventConsumed;
    }

    private void setSameAbsoluteLocation(View v1, View v2) {
        RelativeLayout.LayoutParams alp2 = (RelativeLayout.LayoutParams) v2.getLayoutParams();
        setAbsoluteLocation(v1, alp2.leftMargin + 100, alp2.topMargin + 100);
        //v2.bringToFront();
        m_basketView.clearAnimation();

        m_stickerView.setVisibility(View.INVISIBLE);
        //IGeofence geo = IGeofence.getGeofenceById(m_stickerView.getId());
        //MyStickers.saveSticker(geo);
        inTheBasket = true;

        // not a fake campaign
        if(!m_isfake) {
            MyStickers.addSharedCollected(mCurrentCampaign, m_context);
            new DownloadImageTask(mCurrentCampaign.image_id + "_barcode", 2).execute();
            new DownloadImageTask(mCurrentCampaign.image_id + "_promo", 1).execute();
        }

        Toast.makeText(m_context, "Aerial collected! Congratulations !!",Toast.LENGTH_LONG).show();

        MediaPlayer mp = MediaPlayer.create(m_context, R.raw.collected);
        if (mp != null)
            mp.start();

        m_sensorManager.UnregisterSensors(m_activity);

        m_activity.finish();
        Intent nextScreen = new Intent(m_context, MainMenuActivity.class);
        nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        nextScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        m_context.startActivity(nextScreen);
    }


    private void setAbsoluteLocationCentered(View v, int x, int y) {
        setAbsoluteLocation(v, x - v.getWidth() / 2, y - v.getHeight() / 2);
    }

    private void setAbsoluteLocation(View v, int x, int y) {
        RelativeLayout.LayoutParams alp = (RelativeLayout.LayoutParams) v.getLayoutParams();
        alp.bottomMargin = 2 * m_height;
        alp.topMargin = y;
        alp.rightMargin = 2 * m_width ;
        alp.leftMargin = x ;

        v.setLayoutParams(alp);
    }
}
