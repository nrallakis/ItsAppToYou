package com.ndn.itsapptoyou.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndn.itsapptoyou.R;

import java.util.List;

public class GiftAdapter extends ArrayAdapter<Gift> {

    private Context context;
    private List<Gift> giftList;


    public GiftAdapter(@NonNull Context context, List<Gift> giftList) {
        super(context, 0, giftList);
        this.giftList = giftList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.gift_list_item, parent, false);
        }

        //Find the gift object at the given position
        Gift gift = getItem(position);

        TextView text = listItemView.findViewById(R.id.li_text);
        ImageView imageView = listItemView.findViewById(R.id.li_image);

        text.setText(gift.getText());
        bindImage(gift, imageView);

        return listItemView;
    }

    private void bindImage(Gift gift, ImageView imageView) {
        imageView.setImageDrawable(ContextCompat.getDrawable(context, gift.getImageId()));
    }

    @Nullable
    @Override
    public Gift getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Gift item) {
        return super.getPosition(item);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

}
