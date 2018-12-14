package com.ndn.itsapptoyou.model;

import android.content.Context;
import android.util.Log;

import com.ndn.itsapptoyou.R;

import java.util.ArrayList;

public class GiftLab {

    private static final String TAG = "GiftLab";
    private static final String FILENAME = "gifts.json";

    private static GiftLab instance;

    private Context appContext;

    private ArrayList<Gift> gifts;
    private GiftJSONSerializer serializer;

    private Gift[] giftsOfWheel;

    private GiftLab() {}

    public static GiftLab get(Context c) {
        if (instance == null) {
            instance = new GiftLab(c.getApplicationContext());
        }
        return instance;
    }

    private GiftLab(Context appContext) {
        this.appContext = appContext;
        gifts = new ArrayList<>();
        serializer = new GiftJSONSerializer(this.appContext, FILENAME);
        try {
            gifts = serializer.loadGifts();
        } catch (Exception e) {
            gifts = new ArrayList<>();
            Log.e(TAG, "Error loading absenceDays ", e);
        }
        initGiftsOfWheelArray();
    }

    private void initGiftsOfWheelArray() {
        Gift burger = new Gift("1+1 burger meal στα Simply Burgers", R.drawable.burger);
        Gift pizza = new Gift("1+1 πίτσα στην Pizza Hut", R.drawable.pizza);
        Gift cinema = new Gift("1+1 εισητήριο στα Odeon Cinemas", R.drawable.popcorn);
        Gift breakfast = new Gift("5€ κουπόνι πρωινού στα TGI Fridays", R.drawable.egg);
        Gift hotdog = new Gift("1+1 hot dog στο Street Dog", R.drawable.sanduits);
        Gift none = new Gift("", 0);
        giftsOfWheel = new Gift[]{
                none, pizza, breakfast, none,
                cinema, burger, none, pizza,
                breakfast, none, hotdog, burger
        };
    }

    public boolean saveGifts() {
        try {
            serializer.saveGifts(gifts);
            Log.d(TAG, "Gifts saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving gifts");
            return false;
        }
    }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public Gift getGift(int index) {
        return gifts.get(index);
    }

    public Gift[] getGiftsOfWheelArray() {
        return giftsOfWheel;
    }

    public int getTotalGifts() {
        return gifts.size();
    }

    public void addGift(Gift gift) {
        gifts.add(0, gift);
    }

    public void resetGifts() {
        gifts.clear();
    }
}
