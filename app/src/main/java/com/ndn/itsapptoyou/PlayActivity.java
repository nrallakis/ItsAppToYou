package com.ndn.itsapptoyou;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    private static final int DEFAULT_TRIES = 5;

    private ConstraintLayout layout;

    private Wheel wheel;
    private TextView triesText;
    private Button spinButton;

    private int triesLeft;

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

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
                wheel.spinRight();
                updateTriesText(triesLeft);
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
                wheel.spinLeft();
                updateTriesText(triesLeft);
                return true;
            }
        });
        wheel.setGestureDetector(gestureDetector);
        wheel.setSpinListener(new Wheel.SpinListener() {
            @Override
            public void onSpinStart() {
                //Start sound
            }

            @Override
            public void onSpinEnd() {
                spinButton.setEnabled(true);
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
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(this).edit();
        if (triesLeft == 0) {
            editor.putInt(getString(R.string.key_tries), DEFAULT_TRIES);
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
