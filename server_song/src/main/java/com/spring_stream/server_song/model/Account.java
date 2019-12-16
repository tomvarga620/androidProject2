package com.spring_stream.server_song.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String email;   //ohandlovat v jave nech je to unique
    String password;
    String username;
    int typeAccount;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonManagedReference//cause infinity json
    private Set<FavoriteList> favoriteLists;

    public Account(String email, String password, String username, int typeAccount) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.typeAccount = typeAccount;
    }

    public Account() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(int typeAccount) {
        this.typeAccount = typeAccount;
    }

    public Set<FavoriteList> getFavoriteLists() {
        return favoriteLists;
    }

    public void setFavoriteLists(Set<FavoriteList> favoriteLists) {
        this.favoriteLists = favoriteLists;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", typeAccount=" + typeAccount +
                ", favoriteLists=" + favoriteLists +
                '}';
    }
}
