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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;

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

    private Realm realm;
    private TabLayout tabLayout;
    private GridView gridview;
    private EditText edit_title,edit_detail;
    private ScrollView scrollView_edit, scrollView_text;
    private Toolbar toolbar;
    private boolean menu_flag = false;
    private PictureAdapter pictureAdapter;
    private ArrayList<Uri> pictureItems = new ArrayList<>(); // 현재 저장되어 있는 모든 이미지 주소
    private ArrayList<Uri> imagePaths = new ArrayList<>(); // 새로 가져온 이미지 주소
    private InputMethodManager imm;
    private int delete_img_pos;
    private boolean isEditMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        Intent intent = getIntent();
        if(intent.getExtras().getInt("id") == -1) isEditMode = true;
        else isEditMode = false;

        realm = Realm.getDefaultInstance();

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        scrollView_edit = (ScrollView)findViewById(R.id.scrollView_edit);
        scrollView_text = (ScrollView)findViewById(R.id.scrollView_text);

        if(isEditMode){
            scrollView_edit.setVisibility(View.VISIBLE);
            scrollView_text.setVisibility(View.GONE);
        }else{
            scrollView_edit.setVisibility(View.GONE);
            scrollView_text.setVisibility(View.VISIBLE);
        }

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
        edit_title = (EditText)findViewById(R.id.memo_title_edit);
        edit_detail = (EditText)findViewById(R.id.memo_detail_edit);
        edit_title.setOnClickListener(this);
        edit_detail.setOnClickListener(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case Define.ALBUM_REQUEST_CODE:
                if(resultCode == RESULT_OK){

                    imagePaths = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    pictureItems.addAll(imagePaths);
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
                click_layout();
                break;
            case R.id.memo_detail_edit:
                click_layout();
                break;
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
            public void onClick(DialogInterface dialog, int which) {}
        });
        builder.show();
    }
    private void click_layout(){
        gridview.setVisibility(View.GONE);
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
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Number currentId, nextId;
                    currentId = realm.where(NotepadItem.class).max("id");

                    if(currentId == null) nextId = 0;
                    else nextId = currentId.intValue() + 1;

                    NotepadItem notepadItem = realm.createObject(NotepadItem.class, nextId.intValue());
                    notepadItem.setTitle_text(title);
                    notepadItem.setDetail_text(detail);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("jaejin","Realm Success");
                }
            }, new Realm.Transaction.OnError(){
                @Override
                public void onError(Throwable error) {
                    Log.d("jaejin","Realm Failed");
                }
            });

            for(final Uri img_path : pictureItems){
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Number currentId = realm.where(NotepadItem.class).max("id");

                        PictureItem pictureItem = realm.createObject(PictureItem.class);
                        pictureItem.setId(currentId.intValue());
                        pictureItem.setUri(img_path.toString());
                    }
                });
            }
            supportInvalidateOptionsMenu(); // onPrepareOptionsMenu 실행
            scrollView_edit = (ScrollView)findViewById(R.id.scrollView_edit);
            scrollView_text = (ScrollView)findViewById(R.id.scrollView_text);
            scrollView_edit.setVisibility(View.GONE);
            scrollView_text.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.INVISIBLE);
            edit_title.setText("");
            edit_detail.setText("");
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
            case android.R.id.home:
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<NotepadItem> notepadItems;
                        notepadItems = realm.where(NotepadItem.class).findAll();
                        RealmResults<PictureItem> pictureItems;
                        pictureItems = realm.where(PictureItem.class).findAll();
                        Log.d("jaejin",notepadItems +"");
                        Log.d("jaejin",pictureItems +"");
                        /* id가 notepadItems.get(1).getNotepadId()인 객체 줓 첫 번째 객체 update 방법
                        NotepadItem notepadItem;
                        notepadItem = realm.where(NotepadItem.class).equalTo("id",notepadItems.get(1).getNotepadId()).findFirst();
                        notepadItem.setTitle_text("jaejin");
                        */

                        /*  id가 notepadItems.get(1).getNotepadId()인 모든 객체 삭제방법
                        RealmResults<NotepadItem> notepadItems;
                        notepadItems = realm.where(NotepadItem.class).equalTo("id",notepadItems.get(1).getNotepadId()).findAll();
                        notepadItems.deleteAllFromRealm();
                        Log.d("jaejin",notepadItems +"");
                        */

                        /*  id가 notepadItems.get(1).getNotepadId()인 객체 중 첫 번째 객체만 삭제방법
                        NotepadItem notepadItem;
                        notepadItem = realm.where(NotepadItem.class).equalTo("id",notepadItems.get(1).getNotepadId()).findFirst();
                        notepadItem.deleteFromRealm();
                        Log.d("jaejin",notepadItems +"");
                         */
                    }
                });
                break;
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
                pictureItems.add(image_uri);
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
}
