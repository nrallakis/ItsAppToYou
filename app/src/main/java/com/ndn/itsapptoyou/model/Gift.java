package com.ndn.itsapptoyou.model;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Gift {

    private static final String JSON_TEXT = "text";
    private static final String JSON_IMAGE = "image";

    private String text;
    private int imageId;

    public Gift(@NonNull String text, int imageId) {
        String code = "\nΚωδικός: " + (new Random().nextInt(70000) + 123456);
        this.text = text + code;
        this.imageId = imageId;
    }

    public Gift(JSONObject object) throws JSONException {
        text = object.getString(JSON_TEXT);
        imageId = object.getInt(JSON_IMAGE);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(JSON_TEXT, text);
        object.put(JSON_IMAGE, imageId);

        return object;
    }

    public String getText() {
        return text;
    }

    public int getImageId() {
        return imageId;
    }
}
