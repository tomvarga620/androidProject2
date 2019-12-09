package com.tomvarga.androidproject2;

import java.util.Arrays;

public class Album {

    private Long id;
    private String albumName;

    public Album(Long id,String albumName) {
        this.id = id;
        this.albumName = albumName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", albumName='" + albumName + '\'' +
                '}';
    }
}
