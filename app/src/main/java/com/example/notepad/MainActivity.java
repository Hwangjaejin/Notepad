package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.notepad.adapter.NotepadAdapter;
import com.example.notepad.data.SampleData;
import com.example.notepad.model.NotepadItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
    protected void onDestroy(){
        super.onDestroy();
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
        startActivity(memoActivity);
    }

    public void showMemoList(){
        RealmResults<NotepadItem> notepadItems;
        notepadItems = realm.where(NotepadItem.class).findAll();

        for(int i = 0 ; i < notepadItems.size(); i++){
            int id = notepadItems.get(i).getNotepadId();
            String title = notepadItems.get(i).getTitle_text();
            String detail = notepadItems.get(i).getDetail_text();
            NotepadItem item = new NotepadItem(
                    id,
                    title,
                    detail
            );
            items.add(item);
        }
        adapter = new NotepadAdapter(items,this);
        recyclerView.setAdapter(adapter);
    }
}
