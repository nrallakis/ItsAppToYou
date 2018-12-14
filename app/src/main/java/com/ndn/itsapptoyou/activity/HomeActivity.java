package com.ndn.itsapptoyou.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndn.itsapptoyou.MyBounceInterpolator;
import com.ndn.itsapptoyou.R;

public class HomeActivity extends AppCompatActivity {

    private TextView tvTriesLeft;
    private Button playButton;
    private Animation bounceAnimation;
    private ImageButton myGiftsButton;
    private ConstraintLayout layout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        layout = findViewById(R.id.my_layout);

        bounceAnimation = AnimationUtils
                .loadAnimation(HomeActivity.this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnimation.setInterpolator(interpolator);

        playButton = findViewById(R.id.btn_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton.startAnimation(bounceAnimation);
                startPlayActivity();
            }
        });

        myGiftsButton = findViewById(R.id.view_gifts);
        myGiftsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myGiftsButton.startAnimation(bounceAnimation);
                onGiftsClick();
            }
        });

        TextView tvMyGifts = findViewById(R.id.tv_my_gifts);
        tvMyGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGiftsClick();
            }
        });

        tvTriesLeft = findViewById(R.id.tv_home_tries);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "";
                int id = view.getId();
                switch (id) {
                    case R.id.icon_hotdog:
                        text = "1+1 hot dog στο Street Dog";
                        break;
                    case R.id.icon_burger:
                        text = "1+1 burger meal στα Simply Burgers";
                        break;
                    case R.id.icon_eggs:
                        text = "5€ κουπόνι πρωινού στα TGI Fridays";
                        break;
                    case R.id.icon_popcorn:
                        text = "1+1 εισητήριο στα Odeon Cinemas";
                        break;
                    case R.id.icon_pizza:
                        text = "1+1 πίτσα στην Pizza Hut";
                        break;
                }

                Snackbar snackbar =
                        Snackbar.make(layout, text, Snackbar.LENGTH_LONG);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.snack));
                snackbar.show();
            }
        };

        ImageView burger = findViewById(R.id.icon_burger);
        burger.setOnClickListener(listener);
        ImageView hotdog = findViewById(R.id.icon_hotdog);
        hotdog.setOnClickListener(listener);
        ImageView cinema = findViewById(R.id.icon_popcorn);
        cinema.setOnClickListener(listener);
        ImageView pizza = findViewById(R.id.icon_pizza);
        pizza.setOnClickListener(listener);
        ImageView breakfast = findViewById(R.id.icon_eggs);
        breakfast.setOnClickListener(listener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int triesLeft = prefs.getInt(getString(R.string.key_tries), PlayActivity.DEFAULT_TRIES);
        if (triesLeft == 1) {
            tvTriesLeft.setText(Html.fromHtml("Έχεις <b>1</b> προσπάθεια"));
        } else {
            tvTriesLeft.setText(Html.fromHtml(getString(R.string.home_tries_left, triesLeft + "")));
        }

        playButton.startAnimation(bounceAnimation);
    }

    private void onGiftsClick() {
        Intent intent = new Intent(HomeActivity.this, GiftsActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void startPlayActivity() {
        Intent activity = new Intent(this, PlayActivity.class);
        startActivity(activity);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {}
}
