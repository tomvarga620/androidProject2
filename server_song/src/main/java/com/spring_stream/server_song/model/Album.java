package com.spring_stream.server_song.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(uniqueConstraints= @UniqueConstraint(columnNames={"albumName", "albumCover"}))
public class Album {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String albumName;
    String albumCover;

    @OneToMany(mappedBy = "album",cascade = {CascadeType.ALL})
    @JsonManagedReference//cause infinity json
    List<Song> songs = new ArrayList<>();

    public Album(String albumName, String albumCover) {
        this.albumName = albumName;
        this.albumCover = albumCover;
    }

    public Album(Long id) {
        this.id=id;
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

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", albumName='" + albumName + '\'' +
                ", albumCover='" + albumCover + '\'' +
                ", songs=" + songs +
                '}';
    }
}
