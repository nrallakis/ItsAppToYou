package com.ndn.itsapptoyou.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class GiftJSONSerializer {

    private Context mContext;
    private String mFilename;

    public GiftJSONSerializer(Context c, String fName) {
        mContext = c;
        mFilename = fName;
    }

    public void saveGifts(ArrayList<Gift> gifts)
            throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Gift gift : gifts) {
            array.put(gift.toJSON());
        }

        // Write to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<Gift> loadGifts() throws JSONException, IOException {
        ArrayList<Gift> gifts = new ArrayList<>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant
                jsonString.append(line);
            }

            // Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                gifts.add(new Gift(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // This happens when starting for the first time
        } finally {
            if (reader != null)
                reader.close();
        }
        return gifts;
    }
}
