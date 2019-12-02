package com.spring_stream.server_song.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Song {

    @Id
    String id;
    String author;
    String songName;
    String album;
    String path;

    public Song(String author, String songName, String album, String path) {
        this.author = author;
        this.songName = songName;
        this.album = album;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", songName='" + songName + '\'' +
                ", album='" + album + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
