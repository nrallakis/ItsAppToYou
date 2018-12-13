package com.ndn.itsapptoyou;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

    private final View view;

    public CustomGestureListener(View view) {
        this.view = view;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        view.onTouchEvent(e);
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() < e2.getX()) {
            return onSwipeRight();
        }

        if (e1.getX() > e2.getX()) {
            return onSwipeLeft();
        }

        return true;
    }

    public abstract boolean onSwipeRight();
    public abstract boolean onSwipeLeft();
}
