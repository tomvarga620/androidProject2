package com.tomvarga.androidproject2.POJO;

public class Song {
    private Long id;
    private String author;
    private String songName;
    private String genre;
    private String album;


    public Song(Long id, String author, String songName,String genre, String album) {
        this.id = id;
        this.author = author;
        this.songName = songName;
        this.genre = genre;
        this.album = album;
    }


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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", songName='" + songName + '\'' +
                ", genre='" + genre + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
