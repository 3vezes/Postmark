package com.ericrgon.nearbox;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

/**
 * A view used to display the the user would like a high resolution of the current image. It listens for the following gestures.
 *
 * <ul>
 *     <li>Pinch open</li>
 *     <li>Single Touch</li>
 *     <li>Double tap</li>
 * </ul>
 */
public class ZoomDetectedImageView extends ImageView implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private final ScaleGestureDetector scaleGestureDetector;
    private final GestureDetectorCompat gestureDetectorCompat;

    private static final String LOG_TAG = "ZOOM";

    public ZoomDetectedImageView(Context context) {
        super(context);
        this.scaleGestureDetector = new ScaleGestureDetector(context,this);
        this.gestureDetectorCompat = new GestureDetectorCompat(context,this);
        this.gestureDetectorCompat.setOnDoubleTapListener(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetectorCompat.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d(LOG_TAG,"onScale() " + detector.getScaleFactor());
        if(detector.getScaleFactor() >= 1){
            Log.d(LOG_TAG,"Pinch Open " + detector.toString());
            performClick();
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d(LOG_TAG,"Single tap detected");
        performClick();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d(LOG_TAG,"Double tap zoom");
        performClick();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }
}
