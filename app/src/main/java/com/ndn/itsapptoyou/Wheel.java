package com.ndn.itsapptoyou;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.util.Random;

public class Wheel extends AppCompatImageView {

    private static final int ITEM_DEGREES = 30;
    private static final int ROTATION_DURATION = 3500;
    private static final int CIRCLE = 360;

    private GestureDetectorCompat gestureDetector;
    private Animation animation;
    private Random random;
    private SpinListener spinListener;

    private float previousDegrees;

    private void init(Context context) {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return !gestureDetector.onTouchEvent(event);
            }
        });

        random = new Random();
        animation = makeAnimation(previousDegrees, randomDegrees());
    }

    public Wheel(Context context) {
        super(context);
        init(context);
    }

    public Wheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Wheel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void setGestureDetector(GestureDetectorCompat gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public void setSpinListener(SpinListener spinListener) {
        this.spinListener = spinListener;
    }

    public void spinRight() {
        animation = makeAnimation(previousDegrees, randomDegrees());
        startAnimation(animation);
    }

    public void spinLeft() {
        animation = makeAnimation(previousDegrees, -randomDegrees());
        startAnimation(animation);
    }

    private float randomDegrees() {
        return ITEM_DEGREES * random.nextInt(3*CIRCLE/ITEM_DEGREES) + 1.5f*CIRCLE;
    }

    private Animation makeAnimation(float fromDegrees, float randomDegrees) {
        RotateAnimation animation = new RotateAnimation(fromDegrees, fromDegrees + randomDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(ROTATION_DURATION);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (spinListener != null) spinListener.onSpinStart();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (spinListener != null) spinListener.onSpinEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        previousDegrees = (fromDegrees + randomDegrees) % 360;
        return animation;
    }

    public interface SpinListener {
        void onSpinStart();
        void onSpinEnd();
    }
}
