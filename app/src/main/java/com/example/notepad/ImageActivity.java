package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.notepad.adapter.SlideAdapter;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {

    private int position;
    private ArrayList<Uri> pictureItems = new ArrayList<>();
    private SlideAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();
        position = intent.getExtras().getInt("id");
        pictureItems = intent.getExtras().getParcelableArrayList("pictureList");

        viewPager = (ViewPager)findViewById(R.id.pager);
        adapter = new SlideAdapter(this,pictureItems,position);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
}
