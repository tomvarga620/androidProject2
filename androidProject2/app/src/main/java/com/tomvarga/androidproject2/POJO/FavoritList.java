package com.tomvarga.androidproject2.POJO;

import java.util.ArrayList;

public class FavoritList {

    private Long id;
    private String title;
    private ArrayList<Long> songs;

    public FavoritList(Long id, String title, ArrayList<Long> songs) {
        this.id = id;
        this.title = title;
        this.songs = songs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Long> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Long> songs) {
        this.songs = songs;
    }
}
