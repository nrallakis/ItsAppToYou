package com.ndn.itsapptoyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ndn.itsapptoyou.R;
import com.ndn.itsapptoyou.model.GiftLab;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GiftLab.get(getApplicationContext());

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
