package com.example.notepad.model;

import com.example.notepad.adapter.NotepadAdapter;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NotepadItem extends RealmObject {
    @PrimaryKey
    private int notepadId;
    private String title_text;
    private String detail_text;
    private String thumnail_src;

    public NotepadItem(){}

    public NotepadItem(int notepadId, String title_text) {
        this.notepadId = notepadId;
        this.title_text = title_text;
    }

    public NotepadItem(int notepadId, String title_text, String detail_text) {
        this.notepadId = notepadId;
        this.title_text = title_text;
        this.detail_text = detail_text;
    }

    public NotepadItem(int notepadId, String title_text, String detail_text, String thumnail_src) {
        this.notepadId = notepadId;
        this.title_text = title_text;
        this.detail_text = detail_text;
        this.thumnail_src = thumnail_src;
    }

    public int getNotepadId() {
        return notepadId;
    }

    public void setNotepadId(int notepadId) {
        this.notepadId = notepadId;
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

    public String getThumnail_src() {
        return thumnail_src;
    }

    public void setThumnail_src(String thumnail_src) {
        this.thumnail_src = thumnail_src;
    }
}
