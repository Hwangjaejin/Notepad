package com.example.notepad.model;


import io.realm.RealmObject;

public class PictureItem extends RealmObject {
    private int id;
    private String uri;

    public PictureItem() {
    }

    public PictureItem(int id, String uri) {
        this.id = id;
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
