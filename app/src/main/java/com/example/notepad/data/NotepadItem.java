package com.example.notepad.data;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class NotepadItem extends RealmObject{
    @PrimaryKey
    private int id;
    private String title_text;
    private String detail_text;
    @Ignore
    private String imagePath;

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

    public NotepadItem(int id, String title_text, String detail_text, String imagePath) {
        this.id = id;
        this.title_text = title_text;
        this.detail_text = detail_text;
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
