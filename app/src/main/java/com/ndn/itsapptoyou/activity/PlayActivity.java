package com.ndn.itsapptoyou.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ndn.itsapptoyou.CustomGestureListener;
import com.ndn.itsapptoyou.R;
import com.ndn.itsapptoyou.SoundPlayer;
import com.ndn.itsapptoyou.model.Gift;
import com.ndn.itsapptoyou.model.GiftLab;
import com.ndn.itsapptoyou.view.Wheel;

public class PlayActivity extends AppCompatActivity {

    public static final String EXTRA_GIFT_INDEX = "key_gift_index";
    public static final int DEFAULT_TRIES = 5;

    private static final String FILENAME = "gifts.json";


    private ConstraintLayout layout;

    private Wheel wheel;
    private TextView triesText;
    private Button spinButton;
    private ImageButton backButton;
    private RelativeLayout tutorialDialog;

    private int triesLeft;
    private int giftIndex;
    private Animation scaleAnimation;

    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        LinearInterpolator interpolator = new LinearInterpolator();
        scaleAnimation.setDuration(400);
        scaleAnimation.setInterpolator(interpolator);

        loadTriesLeft();
        initUI();
    }

    private void loadTriesLeft() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        triesLeft = prefs.getInt(getString(R.string.key_tries), DEFAULT_TRIES);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUI() {
        tutorialDialog = findViewById(R.id.rl_tutorial);
        tutorialDialog.startAnimation(scaleAnimation);

        layout = findViewById(R.id.layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideTutorial();
                return true;
            }
        });

        wheel = findViewById(R.id.wheelView);
        initGestureDetector();
        wheel.setGestureDetector(gestureDetector);
        wheel.setSpinListener(new Wheel.SpinListener() {
            @Override
            public void onSpinStart(float finalDegrees) {
                SoundPlayer.get(PlayActivity.this)
                        .playWheelSound();
                giftIndex = validateWinner(finalDegrees);
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
                    showWinningScreen(giftIndex);
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initGestureDetector() {
        gestureDetector = new GestureDetectorCompat(this, new CustomGestureListener(wheel) {
            @Override
            public boolean onSwipeRight() {
                hideTutorial();
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
                hideTutorial();
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

            @Override
            public boolean onTouch() {
                hideTutorial();
                return true;
            }
        });
    }

    private boolean lost(float finalDegrees) {
        return ((finalDegrees % 360) / 30) % 3 == 0;
    }

    private void showWinningScreen(int giftIndex) {
        Intent intent = new Intent(this, WinActivity.class);
        intent.putExtra(EXTRA_GIFT_INDEX, giftIndex);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private int validateWinner(float finalDegrees) {
        int index = (12 - (int) ((finalDegrees % 360) / 30)) % 12;
        if (lost(finalDegrees)) return 0;
        GiftLab lab = GiftLab.get(this);
        Gift gift = lab.getGiftsOfWheelArray()[index];
        lab.addGift(gift);
        return index;
    }

    private void onSpinClick() {
        hideTutorial();
        if (triesLeft == 0) {
            showNoTriesLeftSnack();
            return;
        }
        //Do not let user spin while the wheel is moving.
        spinButton.setEnabled(false);

        triesLeft--;
        updateTriesText(triesLeft);
        wheel.spinRight();

    }

    private void showNoTriesLeftSnack() {
        Snackbar snackbar =
                Snackbar.make(layout, R.string.toast_no_tries_left, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.snack));
        snackbar.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GiftLab.get(this).saveGifts();
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
        if (triesLeft == 1) {
            triesText.setText(Html.fromHtml("Έχεις ακόμα <b>1</b> προσπάθεια"));
        } else {
            triesText.setText(Html.fromHtml(getString(R.string.play_tries_left, String.valueOf(triesLeft))));
        }
    }

    private void hideTutorial() {
        if (tutorialDialog.getVisibility() == View.VISIBLE) {
            tutorialDialog.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SoundPlayer.get(this).pause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
