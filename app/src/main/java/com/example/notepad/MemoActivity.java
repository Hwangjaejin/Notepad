package com.example.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.realm.Realm;
import io.realm.RealmResults;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.adapter.PictureAdapter;
import com.example.notepad.data.NotepadItem;
import com.example.notepad.data.PictureItem;
import com.google.android.material.tabs.TabLayout;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;
import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_FROM_GALLERY = 0;
    private static final int PICK_FROM_IMAGE = 1;
    private static final int CAMERA = 0;
    private static final int DELETE = 1;
    private static final int NO_ID = -1;
    private Realm realm;
    private TabLayout tabLayout;
    private LinearLayout text_scroll_layout;
    private GridView gridview;
    private ImageView imageView;
    private EditText edit_title,edit_detail;
    private TextView text_title,text_detail;
    private ScrollView scrollView_edit, scrollView_text;
    private Toolbar toolbar;
    private boolean menu_flag = false;
    private PictureAdapter pictureAdapter;
    private ArrayList<Uri> pictureItems = new ArrayList<>(); // 현재 저장되어 있는 모든 이미지 주소
    private ArrayList<Uri> imagePaths = new ArrayList<>(); // 새로 가져온 이미지 주소
    private InputMethodManager imm;
    private int delete_img_pos;
    private boolean isEditMode;
    private int notepadId;
    private boolean isImgDup = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Intent intent = getIntent();
        notepadId = intent.getExtras().getInt("id");
        if(notepadId == NO_ID) isEditMode = true;
        else isEditMode = false;

        realm = Realm.getDefaultInstance();

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        text_scroll_layout = (LinearLayout)findViewById(R.id.text_scroll_layout);
        scrollView_edit = (ScrollView)findViewById(R.id.scrollView_edit);
        scrollView_text = (ScrollView)findViewById(R.id.scrollView_text);
        edit_title = (EditText)findViewById(R.id.memo_title_edit);
        edit_detail = (EditText)findViewById(R.id.memo_detail_edit);
        edit_title.setOnClickListener(this);
        edit_detail.setOnClickListener(this);
        text_title = (TextView)findViewById(R.id.memo_title_text);
        text_detail = (TextView)findViewById(R.id.memo_detail_text);

        gridview = (GridView)findViewById(R.id.gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
                builder.setTitle("안내");
                builder.setMessage("첨부된 사진을 삭제하시겠습니까?");
                delete_img_pos = position;
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pictureItems.remove(delete_img_pos);
                        pictureAdapter = new PictureAdapter(MemoActivity.this,pictureItems);
                        gridview.setAdapter(pictureAdapter);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                builder.show();
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_orange_24dp);
        getSupportActionBar().setTitle("");

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_photo_camera_orange_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_delete_orange_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorOrange), PorterDuff.Mode.SRC_IN);
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
        initMemo();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case Define.ALBUM_REQUEST_CODE:
                if(resultCode == RESULT_OK){

                    imagePaths = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    for(Uri img_uri : imagePaths){
                        if(pictureItems.contains(img_uri)) isImgDup = true;
                        else pictureItems.add(img_uri);
                    }
                    if(isImgDup){
                        isImgDup = false;
                        Toast toast = Toast.makeText(this.getApplicationContext(),"중복사진을 제외하고 추가하였습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    pictureAdapter = new PictureAdapter(this,pictureItems);
                    gridview.setAdapter(pictureAdapter);

                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
                            builder.setTitle("안내");
                            builder.setMessage("첨부된 사진을 삭제하시겠습니까?");
                            delete_img_pos = position;
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pictureItems.remove(delete_img_pos);
                                    pictureAdapter = new PictureAdapter(MemoActivity.this,pictureItems);
                                    gridview.setAdapter(pictureAdapter);
                                }
                            });
                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });
                            builder.show();

                        }
                    });
                }
                break;
        }

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.memo_title_edit:
                click_title_edit();
                break;
            case R.id.memo_detail_edit:
                click_detail_edit();
                break;
        }
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
            menu.getItem(2).setVisible(true);
        }else{
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
            menu.getItem(2).setVisible(false);
        }
        menu_flag = !menu_flag;
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                break;
            case R.id.done_menu:
                saveMemo();
                break;
            case R.id.delete_menu:
                deleteMemo();
                break;
            case R.id.edit_menu:
                editMemo();
        }
        return true;
    }
    private void initMemo(){
        if(isEditMode){
            scrollView_edit.setVisibility(View.VISIBLE);
            scrollView_text.setVisibility(View.GONE);
            menu_flag = false;
        }else{
            scrollView_edit.setVisibility(View.GONE);
            scrollView_text.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.INVISIBLE);
            NotepadItem notepadItem = realm.where(NotepadItem.class).equalTo("id",notepadId).findFirst();
            text_title.setText(notepadItem.getTitle_text());
            text_detail.setText(notepadItem.getDetail_text());
            menu_flag = true;
            RealmResults<PictureItem> pictureitems;
            pictureitems = realm.where(PictureItem.class).equalTo("id",notepadId).findAll();
            if(pictureitems.size() > 0){
                for(int i = 0; i < pictureitems.size(); i++){
                    pictureItems.add(Uri.parse(pictureitems.get(i).getUri()));
                }
                pictureAdapter = new PictureAdapter(this,pictureItems);
                gridview.setAdapter(pictureAdapter);
            }

            RealmResults<PictureItem> items = realm.where(PictureItem.class).equalTo("id",notepadId).findAll();
            for(PictureItem pictureItem : items){
                imageView = new ImageView(this);
                imageView.setImageURI(Uri.parse(pictureItem.getUri()));
                imageView.setId(notepadId);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                text_scroll_layout.addView(imageView);
            }
        }
    }

    private void click_cameraTab() {
        imm.hideSoftInputFromWindow(edit_detail.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);
        showCameraDialog();
        gridview.setVisibility(View.VISIBLE);
    }
    private void click_deleteTab(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
        builder.setTitle("안내");
        builder.setMessage("사진을 전부 삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pictureItems.clear();
                pictureAdapter = new PictureAdapter(MemoActivity.this,pictureItems);
                gridview.setAdapter(pictureAdapter);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    private void click_title_edit(){
        edit_title.post(new Runnable() {
            @Override
            public void run() {
                edit_title.setFocusable(true);
                edit_title.requestFocus();
                imm.showSoftInput(edit_title,0);
                gridview.setVisibility(View.GONE);
            }
        });
    }
    private void click_detail_edit(){
        edit_detail.post(new Runnable() {
            @Override
            public void run() {
                edit_detail.setFocusable(true);
                edit_detail.requestFocus();
                imm.showSoftInput(edit_detail,0);
                gridview.setVisibility(View.GONE);
            }
        });
    }
    private void saveMemo(){
        final String title = edit_title.getText().toString().trim();
        final String detail = edit_detail.getText().toString().trim();

        if(title.getBytes().length <= 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
            builder.setTitle("안내");
            builder.setMessage("제목을 입력해주세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }else if(detail.getBytes().length <= 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
            builder.setTitle("안내");
            builder.setMessage("내용을 입력해주세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }else{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if(notepadId == NO_ID){
                        Number currentId, nextId;
                        currentId = realm.where(NotepadItem.class).max("id");

                        if(currentId == null) nextId = 0;
                        else nextId = currentId.intValue() + 1;
                        notepadId = nextId.intValue();

                        NotepadItem notepadItem = realm.createObject(NotepadItem.class, nextId.intValue());
                        notepadItem.setTitle_text(title);
                        notepadItem.setDetail_text(detail);
                    }else{
                        NotepadItem notepadItem = realm.where(NotepadItem.class).equalTo("id",notepadId).findFirst();
                        notepadItem.setTitle_text(title);
                        notepadItem.setDetail_text(detail);
                    }
                    RealmResults<PictureItem> pictureitem = realm.where(PictureItem.class).equalTo("id",notepadId).findAll();
                    if(pictureitem.size() > 0) pictureitem.deleteAllFromRealm();
                    for(final Uri img_path : pictureItems){
                        PictureItem pictureItem = realm.createObject(PictureItem.class);
                        pictureItem.setId(notepadId);
                        pictureItem.setUri(img_path.toString());
                    }
                }
            });
            text_scroll_layout.removeViews(2,text_scroll_layout.getChildCount() - 2);
            RealmResults<PictureItem> items = realm.where(PictureItem.class).equalTo("id",notepadId).findAll();
            for(PictureItem pictureItem : items){
                imageView = new ImageView(this);
                imageView.setImageURI(Uri.parse(pictureItem.getUri()));
                imageView.setId(notepadId);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                text_scroll_layout.addView(imageView);
            }

            supportInvalidateOptionsMenu(); // onPrepareOptionsMenu 실행
            scrollView_edit.setVisibility(View.GONE);
            scrollView_text.setVisibility(View.VISIBLE);
            text_title.setText(edit_title.getText().toString());
            text_detail.setText(edit_detail.getText().toString());
            imm.hideSoftInputFromWindow(edit_detail.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);
            tabLayout.setVisibility(View.INVISIBLE);
            gridview.setVisibility(View.GONE);
            isEditMode = false;
        }
    }
    private void deleteMemo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MemoActivity.this);
        builder.setTitle("안내");
        builder.setMessage("메모를 삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realm.executeTransaction(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<NotepadItem> notepadItems;
                        RealmResults<PictureItem> pictureItems;
                        notepadItems = realm.where(NotepadItem.class).equalTo("id",notepadId).findAll();
                        pictureItems = realm.where(PictureItem.class).equalTo("id",notepadId).findAll();
                        notepadItems.deleteAllFromRealm();
                        if(pictureItems.size() > 0) pictureItems.deleteAllFromRealm();
                    }
                });
                Intent intent = new Intent(MemoActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }
    private void editMemo(){
        isEditMode = true;
        edit_title.post(new Runnable() {
            @Override
            public void run() {
                edit_title.setFocusable(true);
                edit_title.requestFocus();
                imm.showSoftInput(edit_title,0);
                gridview.setVisibility(View.GONE);
            }
        });
        edit_title.setText(text_title.getText().toString());
        edit_detail.setText(text_detail.getText().toString());
        supportInvalidateOptionsMenu();
        tabLayout.setVisibility(View.VISIBLE);
        scrollView_edit.setVisibility(View.VISIBLE);
        scrollView_text.setVisibility(View.GONE);
    }

    private void showCameraDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메모에 사진 넣기");
        builder.setItems(R.array.cameraItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int pos) {
                switch (pos){
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

    private void goGallery(){
        FishBun.with(MemoActivity.this)
                .setImageAdapter(new GlideAdapter())
                .setCamera(true)
                .setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
                .startAlbum();
    }
    private void goImage(){
        final EditText editText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이미지 붙여넣기");
        builder.setMessage("이미지 URL 입력");
        builder.setView(editText);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri image_uri = Uri.parse(editText.getText().toString());
                if(pictureItems.contains(image_uri)) {
                    Toast toast = Toast.makeText(getApplicationContext(),"중복된 이미지입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    pictureItems.add(image_uri);
                    pictureAdapter = new PictureAdapter(MemoActivity.this,pictureItems);
                    gridview.setAdapter(pictureAdapter);
                }
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
