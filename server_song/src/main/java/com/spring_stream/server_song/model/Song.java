package com.spring_stream.server_song.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

import javax.annotation.processing.Generated;

@Entity
public class Song {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    String author;
    String songName;

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    Album album;

    String genre;
    String path;

    public Song(String author, String songName, Album album, String genre, String path) {
        this.author = author;
        this.songName = songName;
        this.album = album;
        this.genre = genre;
        this.path = path;
    }

    public Song(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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
                "id=" + id +
                ", author='" + author + '\'' +
                ", songName='" + songName + '\'' +
                ", album=" + album +
                ", genre='" + genre + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
