package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.notepad.model.PictureItem;
import com.example.notepad.model.PictureViewer;
import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;
import java.util.ArrayList;

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
    private Button save_button;
    private ImageView imageView;
    private Toolbar toolbar;
    private boolean menu_flag = false;
    private PictureAdapter pictureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

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
        /*
        scrollView_edit = (ScrollView)findViewById(R.id.scrollView_edit);
        scrollView_text = (ScrollView)findViewById(R.id.scrollView_text);
        scrollView_edit.setVisibility(View.GONE);
        scrollView_text.setVisibility(View.VISIBLE);
        */
        //save_button = (Button)findViewById(R.id.save_btn);
        //save_button.setOnClickListener(this);
        edit_title = (EditText)findViewById(R.id.memo_title_edit);
        edit_detail = (EditText)findViewById(R.id.memo_detail_edit);
        edit_title.setOnClickListener(this);
        edit_detail.setOnClickListener(this);

        pictureAdapter = new PictureAdapter();
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));
        pictureAdapter.addItem(new PictureItem(R.drawable.ic_photo_camera_black_24dp));

        gridview.setAdapter(pictureAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("jaejin",position+"");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PICK_FROM_GALLERY){
            if(resultCode == RESULT_OK){
                try{
                    Log.d("jaejin","success");
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    imageView.setImageBitmap(img);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
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

    }
    private void goImage(){

    }

    public class PictureAdapter extends BaseAdapter {
        ArrayList<PictureItem> items = new ArrayList<PictureItem>();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            PictureViewer pictureViewer = new PictureViewer(getApplicationContext());
            pictureViewer.setItem(items.get(position));
            return pictureViewer;
        }

        public void addItem(PictureItem pictureItem){
            items.add(pictureItem);
        }
    }
}
