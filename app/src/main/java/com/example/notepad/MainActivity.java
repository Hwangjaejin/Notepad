package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.notepad.adapter.NotepadAdapter;
import com.example.notepad.data.NotepadItem;
import com.example.notepad.data.PictureItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private Realm realm;
    private FloatingActionButton floatingActionButton;
    private NotepadAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<NotepadItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        items = new ArrayList();

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        realm = Realm.getDefaultInstance();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showMemoList();
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
        realm.close();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                goMemoActivity();
                break;
        }
    }
    public void goMemoActivity(){
        Intent memoActivity = new Intent(this, MemoActivity.class);
        memoActivity.putExtra("id",-1);
        startActivity(memoActivity);
    }

    public void showMemoList(){
        RealmResults<NotepadItem> notepadItems;
        notepadItems = realm.where(NotepadItem.class).findAll();

        for(int i = notepadItems.size() - 1 ; i >= 0; i--){
            int id = notepadItems.get(i).getNotepadId();
            String title = notepadItems.get(i).getTitle_text();
            String detail = notepadItems.get(i).getDetail_text();
            String time = notepadItems.get(i).getDate();
            String img_path;
            long img_count = realm.where(PictureItem.class).equalTo("id",id).count();
            if(img_count > 0){
                PictureItem pictureItem = realm.where(PictureItem.class).equalTo("id",id).findFirst();
                img_path = pictureItem.getUri();
            }else{
                img_path = null;
            }

            NotepadItem item = new NotepadItem(
                    id,
                    title,
                    detail,
                    time,
                    img_path
            );
            items.add(item);
        }
        adapter = new NotepadAdapter(items,this);
        recyclerView.setAdapter(adapter);
    }
}
