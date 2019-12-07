package com.spring_stream.server_song.model;

import javax.persistence.*;
import java.util.List;


@Entity
public class Album {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String albumName;
    String albumCover;

    @OneToMany(mappedBy = "album")
    List<Song> songs;

    public Album(String albumName, String albumCover) {
        this.albumName = albumName;
        this.albumCover = albumCover;
    }

    public Album(){}

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

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }
}
