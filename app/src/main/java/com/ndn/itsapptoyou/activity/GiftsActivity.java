package com.ndn.itsapptoyou.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ndn.itsapptoyou.R;
import com.ndn.itsapptoyou.model.GiftAdapter;
import com.ndn.itsapptoyou.model.GiftLab;

public class GiftsActivity extends AppCompatActivity {

    private GiftAdapter adapter;
    private ListView listView;
    private ImageButton backButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts);

        adapter = new GiftAdapter(this, GiftLab.get(this).getGifts());

        listView = findViewById(R.id.list_gifts);
        listView.setAdapter(adapter);

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
