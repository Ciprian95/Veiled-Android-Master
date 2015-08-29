package com.Veiled.Activities.ZoomActivity;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Laur on 4/4/2015.
 */
public class ZoomRelativeLayout  extends RelativeLayout {
    public ZoomRelativeLayout(Context context) {
        super(context);
        handler = new  UpdateHandler(context, this);
    }
    public ZoomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        handler = new  UpdateHandler(context, this);
    }
    public ZoomRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        handler = new  UpdateHandler(context, this);
    }

   private UpdateHandler handler;
    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    private static final String TAG = "Touch";
    int mode = NONE;

    boolean childClick = false;
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(childClick)
            return false;
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        WrapMotionEvent ev = WrapMotionEvent.wrap(event);

        // Handle touch events here
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                start.set(ev.getX(), ev.getY());
                Log.d(TAG, "mode=DRAGInside");
                childClick = true;
                mode = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(ev);
                childClick = false;
                Log.d(TAG, "oldDistInside" + oldDist);
                if (oldDist > 10f) {
                    midPoint(mid, ev);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOMInside");
                }
                break;

            case MotionEvent.ACTION_UP:
                this.onInterceptTouchEvent(event);
                Log.d(TAG, "mode=UPInside");

            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=POINTERUPInside");
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                } else if (mode == ZOOM) {
                    float newDist = spacing(ev);
                       if (newDist > 10f) {
                            float scale = newDist / oldDist;
                            // zoom in
                            if(scale > 1) {
                                handler.sendEmptyMessageDelayed(1, 2);
                            }
                            else{
                                handler.sendEmptyMessageDelayed(2, 2);
                            }
                        }
                 }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    /** Determine the space between the first two fingers */
    private float spacing(WrapMotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }
    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, WrapMotionEvent event) {
        // ...
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
