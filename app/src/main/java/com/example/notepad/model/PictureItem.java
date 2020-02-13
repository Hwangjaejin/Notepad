package com.example.notepad.model;

import android.util.Log;
import android.widget.ImageView;

public class PictureItem {
    private int imageView;

    public PictureItem(int imageView) {
        this.imageView = imageView;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }


}
