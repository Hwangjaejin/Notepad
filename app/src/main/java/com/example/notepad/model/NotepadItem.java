package com.example.notepad.model;

import android.net.Uri;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class NotepadItem extends RealmObject{
    @PrimaryKey
    private int id;
    private String title_text;
    private String detail_text;
    @Ignore
    private ArrayList<String> imagePaths;

    public NotepadItem(){}

    public NotepadItem(int notepadId, String title_text) {
        this.id = notepadId;
        this.title_text = title_text;
    }

    public NotepadItem(int notepadId, String title_text, String detail_text) {
        this.id = notepadId;
        this.title_text = title_text;
        this.detail_text = detail_text;
    }

    public NotepadItem(int id, String title_text, String detail_text, ArrayList<String> imagePaths) {
        this.id = id;
        this.title_text = title_text;
        this.detail_text = detail_text;
        this.imagePaths = imagePaths;
    }

    public int getNotepadId() {
        return id;
    }

    public void setNotepadId(int notepadId) {
        this.id = notepadId;
    }

    public String getTitle_text() {
        return title_text;
    }

    public void setTitle_text(String title_text) {
        this.title_text = title_text;
    }

    public String getDetail_text() {
        return detail_text;
    }

    public void setDetail_text(String detail_text) {
        this.detail_text = detail_text;
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
