package com.ndn.itsapptoyou;

import android.view.animation.Interpolator;

public class WheelInterpolator implements Interpolator {
    private double mAmplitude = 1;
    private double mFrequency = 10;
    private double R = 1;

    @Override
    public float getInterpolation(float t) {
        float x = t*2.0f;
        if (t<0.5f) return 0.5f*x*x*x*x*x;
        x = (t-0.5f)*2-1;
        return 0.5f*x*x*x*x*x+1;
    }
}
