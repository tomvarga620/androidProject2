package com.spring_stream.server_song.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FavoriteList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private Account account;

    @OneToMany
    @JoinColumn(name = "song_id", referencedColumnName="id")
    private Set<Song> songSet = new HashSet();

    public FavoriteList(String title, Account account) {
        this.title = title;
        this.account = account;
    }

    public FavoriteList() {

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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Song> getSongSet() {
        return songSet;
    }

    public void setSongSet(Set<Song> songSet) {
        this.songSet = songSet;
    }

    @Override
    public String toString() {
        return "FavoriteList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", account=" + account +
                ", songSet=" + songSet +
                '}';
    }
}
