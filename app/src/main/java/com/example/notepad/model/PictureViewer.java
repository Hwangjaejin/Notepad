package com.example.notepad.model;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.notepad.R;

import androidx.annotation.Nullable;

public class PictureViewer extends LinearLayout {
    private ImageView imageView;

    public PictureViewer(Context context) {
        super(context);

        init(context);
    }

    public PictureViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.picture_view,this,true);

        imageView = (ImageView)findViewById(R.id.picture_view);
    }
    public void setItem(PictureItem pictureItem){
        imageView.setImageResource(pictureItem.getImageView());
    }
}
