package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.notepad.adapter.PictureAdapter;
import com.example.notepad.model.PictureItem;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_GALLERY = 1;
    private static final int PICK_FROM_IMAGE = 2;
    private static final int CAMERA = 0;
    private static final int DELETE = 1;

    private TabLayout tabLayout;
    private GridView gridview;
    private EditText edit_title,edit_detail;
    private ScrollView scrollView_edit, scrollView_text;
    private ImageView imageView;
    private Toolbar toolbar;
    private boolean menu_flag = false;
    private PictureAdapter pictureAdapter;
    private PictureItem pictureItem;
    private ArrayList<PictureItem> pictureItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        imageView = findViewById(R.id.imgtest);

        scrollView_edit = (ScrollView)findViewById(R.id.scrollView_edit);
        scrollView_text = (ScrollView)findViewById(R.id.scrollView_text);
        scrollView_edit.setVisibility(View.VISIBLE);
        scrollView_text.setVisibility(View.GONE);

        gridview = (GridView)findViewById(R.id.gridview);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_orange_24dp);
        getSupportActionBar().setTitle("");

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_photo_camera_orange_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_delete_orange_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case CAMERA:
                        click_cameraTab();
                        break;
                    case DELETE:
                        click_deleteTab();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case CAMERA:
                        click_cameraTab();
                        break;
                    case DELETE:
                        click_deleteTab();
                        break;
                }
            }
        });
        edit_title = (EditText)findViewById(R.id.memo_title_edit);
        edit_detail = (EditText)findViewById(R.id.memo_detail_edit);
        edit_title.setOnClickListener(this);
        edit_detail.setOnClickListener(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK){
            Uri uri = data.getData();

            pictureItem = new PictureItem();
            pictureItem.setUri(uri);
            pictureItems.add(pictureItem);
            pictureAdapter = new PictureAdapter(this,pictureItems);
            gridview.setAdapter(pictureAdapter);

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pictureItems.remove(position);
                    pictureAdapter = new PictureAdapter(MemoActivity.this,pictureItems);
                    gridview.setAdapter(pictureAdapter);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.memo_title_edit:
                click_layout();
                break;
            case R.id.memo_detail_edit:
                click_layout();
                break;
        }
    }
    private void click_cameraTab(){
        showCameraDialog();
        gridview.setVisibility(View.VISIBLE);
    }
    private void click_deleteTab(){
        gridview.setVisibility(View.GONE);
    }
    private void click_layout(){
        gridview.setVisibility(View.GONE);
    }
    private void saveMemo(){
        supportInvalidateOptionsMenu();
        scrollView_edit = (ScrollView)findViewById(R.id.scrollView_edit);
        scrollView_text = (ScrollView)findViewById(R.id.scrollView_text);
        scrollView_edit.setVisibility(View.GONE);
        scrollView_text.setVisibility(View.VISIBLE);
        gridview.setVisibility(View.INVISIBLE);

        File imgfile = new File("/storage/emulated/0/DCIM/20200213_234735.jpg");
        if(imgfile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgfile.getAbsolutePath());
            ImageView imagetest = (ImageView)findViewById(R.id.imgtest);
            imagetest.setImageBitmap(bitmap);
            Log.d("jaejin","succ");
        }else{
            Log.d("jaejin","fail");
        }
    }
    private void editMemo(){
        supportInvalidateOptionsMenu();
        scrollView_edit = (ScrollView)findViewById(R.id.scrollView_edit);
        scrollView_text = (ScrollView)findViewById(R.id.scrollView_text);
        scrollView_edit.setVisibility(View.VISIBLE);
        scrollView_text.setVisibility(View.GONE);
        gridview.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(menu_flag){
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        }else{
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        }
        menu_flag = !menu_flag;
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case R.id.done_menu:
                saveMemo();
                break;
            case R.id.edit_menu:
                editMemo();
        }
        return true;
    }

    private void showCameraDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메모에 사진 넣기");
        builder.setItems(R.array.cameraItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                switch (pos){
                    case PICK_FROM_CAMERA:
                        goCamera();
                        break;
                    case PICK_FROM_GALLERY:
                        goGallery();
                        break;
                    case PICK_FROM_IMAGE:
                        goImage();
                        break;
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void goCamera(){

    }
    private void goGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_FROM_GALLERY);
    }
    private void goImage(){

    }

}
