package com.example.notepad.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.notepad.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class PictureAdapter extends BaseAdapter {
    public ArrayList<Uri> items;
    Context mContext;

    public PictureAdapter() {
    }

    public PictureAdapter(Context mContext, ArrayList<Uri> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Uri getItem(int position) {
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
        final Uri item_uri = (Uri) this.getItem(position);
        ImageView imageView = (ImageView)view.findViewById(R.id.picture_view);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Glide.with(mContext)
                .load(item_uri)
                .into(imageView);

        return view;
    }

}
