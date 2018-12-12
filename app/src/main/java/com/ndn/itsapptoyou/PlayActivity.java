package com.ndn.itsapptoyou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private static final float ITEM_DEGREES = 30f;
    private static final int DEFAULT_TRIES = 5;
    private static final int ROTATION_DURATION = 3500;

    private Random random;
    private Animation animation;
    private ImageView wheel;
    private TextView triesText;
    private Button spinButton;

    private float previousDegrees;
    private int triesLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        triesLeft = prefs.getInt(getString(R.string.key_tries), DEFAULT_TRIES);

        random = new Random();
        animation = randomAnimation(previousDegrees);

        wheel = findViewById(R.id.wheelView);

        spinButton = findViewById(R.id.btn_spin);
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (triesLeft == 0) {
                    Toast.makeText(PlayActivity.this, R.string.toast_no_tries_left, Toast.LENGTH_SHORT).show();
                    return;
                }
                spinButton.setEnabled(false);
                triesLeft--;
                wheel.startAnimation(animation);
                animation = randomAnimation(previousDegrees);
                updateTriesText(triesLeft);
            }
        });

        triesText = findViewById(R.id.tv_tries);
        updateTriesText(triesLeft);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(this).edit();
        if (triesLeft == 0) {
            editor.putInt(getString(R.string.key_tries), DEFAULT_TRIES);
        } else {
            editor.putInt(getString(R.string.key_tries), triesLeft);
        }
        editor.apply();
    }

    private Animation randomAnimation(float fromDegrees) {
        final float randomDegrees = ITEM_DEGREES * random.nextInt(36) + 540f;

        RotateAnimation animation = new RotateAnimation(fromDegrees, fromDegrees + randomDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(ROTATION_DURATION);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                spinButton.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        previousDegrees = (fromDegrees + randomDegrees) % 360;

        return animation;
    }

    private void updateTriesText(int triesLeft) {
        triesText.setText(String.format("Έχεις ακόμα %s προσπάθειες", triesLeft));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
