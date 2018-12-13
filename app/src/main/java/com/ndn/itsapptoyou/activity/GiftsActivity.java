package com.ndn.itsapptoyou.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ndn.itsapptoyou.R;
import com.ndn.itsapptoyou.model.Gift;
import com.ndn.itsapptoyou.model.GiftAdapter;
import com.ndn.itsapptoyou.model.GiftLab;

import java.util.List;

public class GiftsActivity extends AppCompatActivity {

    private List<Gift> gifts;

    private GiftAdapter adapter;
    private ListView listView;
    private ImageView backButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

//        Gift burger = new Gift("1+1 burger meal στα Simply Burgers", R.drawable.burger);
//        Gift pizza = new Gift("1+1 πίτσα στην Pizza Hut", R.drawable.pizza);
//        Gift cinema = new Gift("1+1 εισητήριο στα Village Cinemas", R.drawable.popcorn);
//        Gift breakfast = new Gift("5€ κουπόνι πρωινού στα TGI Fridays", R.drawable.egg);
//        Gift hotdog = new Gift("1+1 hot dog στο Street Dog", R.drawable.sanduits);
//        Gift none = new Gift("", 0);
//
//        Gift[] giftArr = {pizza, breakfast, cinema, burger, pizza, breakfast, hotdog, burger};

        adapter = new GiftAdapter(this, GiftLab.get(this).getGifts());

        listView = findViewById(R.id.list_gifts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        backButton = findViewById(R.id.btn_back);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                onBackPressed();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
