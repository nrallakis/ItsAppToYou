package com.ndn.itsapptoyou.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndn.itsapptoyou.MyBounceInterpolator;
import com.ndn.itsapptoyou.R;
import com.ndn.itsapptoyou.model.Gift;
import com.ndn.itsapptoyou.model.GiftLab;

public class WinActivity extends AppCompatActivity {

    TextView tvTriesLeft;
    Animation bounceAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        bounceAnimation = AnimationUtils
                .loadAnimation(WinActivity.this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnimation.setInterpolator(interpolator);

        int giftIndex = getIntent().getExtras().getInt(PlayActivity.EXTRA_GIFT_INDEX);
        Gift gift = GiftLab.get(this).getGiftsOfWheelArray()[giftIndex];

        ImageView giftImage = findViewById(R.id.image_gift);
        giftImage.setImageDrawable(ContextCompat.getDrawable(this, gift.getImageId()));

        TextView giftText = findViewById(R.id.tv_gift_text);
        giftText.setText(gift.getText());

        final Button buttonReturn = findViewById(R.id.btn_return);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonReturn.startAnimation(bounceAnimation);
                onBackPressed();
            }
        });

        tvTriesLeft = findViewById(R.id.tv_win_tries_left);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int triesLeft = prefs.getInt(getString(R.string.key_tries), PlayActivity.DEFAULT_TRIES);
        if (triesLeft == 1) {
            tvTriesLeft.setText(Html.fromHtml("Έχεις <b>1</b> προσπάθεια"));
        } else {
            tvTriesLeft.setText(Html.fromHtml("Έχεις <b>" + triesLeft + "</b> προσπάθεια"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
