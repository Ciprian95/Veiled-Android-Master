package com.Veiled.Animations;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.Veiled.R;


public class PopOnScreenShakeAnimation {
    private Context m_context;

    private Animation m_scale;
    private Animation m_shake;
    private ImageView m_basket;

    public  void SetUpAnimation(Context cur_context, ImageView sticker, final ImageView basket){
        m_basket = basket;
        m_context = cur_context;

        // Scaling
        int fromXscale = 0;
        int toXscale = 1;
        int fromYscale = 0;
        int toYscale = 1;

        m_scale = new ScaleAnimation(fromXscale, toXscale, fromYscale, toYscale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        m_scale.setDuration(500);

        // Moving up
        Animation slideUp = new TranslateAnimation(0, 0, 0, 0);
        slideUp.setDuration(500);

        // Animation set to join both scaling and moving
        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillEnabled(false);//true);
        animSet.addAnimation(m_scale);
        animSet.addAnimation(slideUp);
        // Launching animation set
        sticker.startAnimation(animSet);

        setScaleAnimationListener();
    }

    private void setScaleAnimationListener(){
        // BASKET SHAKE
        m_shake  = AnimationUtils.loadAnimation(m_context, R.anim.shake);
        m_shake.setRepeatCount(Animation.INFINITE);

        m_scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                m_basket.setVisibility(View.VISIBLE);
                m_basket.startAnimation(m_shake);
            }
        });
    }
}
