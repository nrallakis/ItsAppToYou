package com.ndn.itsapptoyou.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndn.itsapptoyou.CustomGestureListener;
import com.ndn.itsapptoyou.R;
import com.ndn.itsapptoyou.SoundPlayer;
import com.ndn.itsapptoyou.model.Gift;
import com.ndn.itsapptoyou.model.GiftLab;
import com.ndn.itsapptoyou.view.Wheel;

public class PlayActivity extends AppCompatActivity {

    private static final int DEFAULT_TRIES = 5;

    private static final String FILENAME = "gifts.json";

    private ConstraintLayout layout;

    private Wheel wheel;
    private TextView triesText;
    private Button spinButton;
    private ImageView backButton;

    private int triesLeft;

    private Gift[] giftsOfWheel;

    private GestureDetectorCompat gestureDetector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Gift burger = new Gift("1+1 burger meal στα Simply Burgers", R.drawable.burger);
        Gift pizza = new Gift("1+1 πίτσα στην Pizza Hut", R.drawable.pizza);
        Gift cinema = new Gift("1+1 εισητήριο στα Village Cinemas", R.drawable.popcorn);
        Gift breakfast = new Gift("5€ κουπόνι πρωινού στα TGI Fridays", R.drawable.egg);
        Gift hotdog = new Gift("1+1 hot dog στο Street Dog", R.drawable.sanduits);
        Gift none = new Gift("", 0);
        giftsOfWheel = new Gift[]{
                none, pizza, breakfast, none,
                cinema, burger, none, pizza,
                breakfast, none, hotdog, burger
        };

        loadTriesLeft();
        initUI();
    }

    private void loadTriesLeft() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        triesLeft = prefs.getInt(getString(R.string.key_tries), DEFAULT_TRIES);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUI() {
        layout = findViewById(R.id.layout);

        wheel = findViewById(R.id.wheelView);
        initGestureDetector();
        wheel.setGestureDetector(gestureDetector);
        wheel.setSpinListener(new Wheel.SpinListener() {
            @Override
            public void onSpinStart() {
                SoundPlayer.get(PlayActivity.this)
                        .playWheelSound();
            }

            @Override
            public void onSpinEnd(float degrees) {
                spinButton.setEnabled(true);
                if (lost(degrees)) {
                    SoundPlayer.get(PlayActivity.this)
                            .playLosingSound();
                } else {
                    SoundPlayer.get(PlayActivity.this)
                            .playWinningSound();
                    showWinningScreen(degrees);
                }
            }
        });

        spinButton = findViewById(R.id.btn_spin);
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSpinClick();
            }
        });

        triesText = findViewById(R.id.tv_tries);
        updateTriesText(triesLeft);

        backButton = findViewById(R.id.btn_back);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onBackPressed();
                return true;
            }
        });
    }

    private void initGestureDetector() {
        gestureDetector = new GestureDetectorCompat(this, new CustomGestureListener(wheel) {
            @Override
            public boolean onSwipeRight() {
                if (triesLeft == 0) {
                    showNoTriesLeftSnack();
                    return true;
                }
                //Do not let user spin while the wheel is moving.
                spinButton.setEnabled(false);
                triesLeft--;
                updateTriesText(triesLeft);
                wheel.spinRight();
                return true;
            }

            @Override
            public boolean onSwipeLeft() {
                if (triesLeft == 0) {
                    showNoTriesLeftSnack();
                    return true;
                }
                //Do not let user spin while the wheel is moving.
                spinButton.setEnabled(false);
                triesLeft--;
                updateTriesText(triesLeft);
                wheel.spinLeft();
                return true;
            }
        });
    }

    private boolean lost(float finalDegrees) {
        return ((finalDegrees % 360) / 30) % 3 == 0;
    }

    private void showWinningScreen(float finalDegrees) {
        //Find gift from degrees
        int index = 12 - (int) ((finalDegrees % 360) / 30);
        Log.d("DEBUG", "index: " + index + " degrees: " + finalDegrees);
        Gift gift = giftsOfWheel[index];
        GiftLab.get(this).addGift(gift);

        //Make screen
        //Show gift image
    }

    private void onSpinClick() {
        if (triesLeft == 0) {
            showNoTriesLeftSnack();
            return;
        }
        //Do not let user spin while the wheel is moving.
        spinButton.setEnabled(false);

        triesLeft--;
        wheel.spinRight();
        updateTriesText(triesLeft);

    }

    private void showNoTriesLeftSnack() {
        Snackbar.make(layout, R.string.toast_no_tries_left, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GiftLab.get(this).saveGifts();
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(this).edit();
        if (triesLeft == 0) {
            editor.putInt(getString(R.string.key_tries), DEFAULT_TRIES);
            GiftLab.get(this).resetGifts();
        } else {
            editor.putInt(getString(R.string.key_tries), triesLeft);
        }
        editor.apply();
    }

    private void updateTriesText(int triesLeft) {
        triesText.setText(String.format("Έχεις ακόμα %s προσπάθειες", triesLeft));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundPlayer.get(this).stop();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
