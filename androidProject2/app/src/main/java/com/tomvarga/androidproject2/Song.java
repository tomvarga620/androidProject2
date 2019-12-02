package com.tomvarga.androidproject2;

public class Song {
    private String id;
    private String author;
    private String songName;
    private String album;

    public Song(String id, String author, String songName, String album) {
        this.id = id;
        this.author = author;
        this.songName = songName;
        this.album = album;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", songName='" + songName + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
