package com.example.notepad.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.notepad.MemoActivity;
import com.example.notepad.R;
import com.example.notepad.model.PictureItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PictureAdapter extends BaseAdapter {
    ArrayList<PictureItem> items;
    Context mContext;
    public PictureAdapter(Context mContext, ArrayList<PictureItem> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public PictureItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.picture_view,viewGroup,false);
        }
        final PictureItem pictureItem = (PictureItem) this.getItem(position);
        ImageView imageView = (ImageView)view.findViewById(R.id.picture_view);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(mContext).load(pictureItem.getUri()).into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //items.remove(position);
            }
        });
        return view;
    }

    public void addItem(PictureItem pictureItem){
        items.add(pictureItem);
    }
}
