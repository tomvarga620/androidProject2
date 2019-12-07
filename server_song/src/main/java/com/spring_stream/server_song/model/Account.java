package com.spring_stream.server_song.model;

import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;
    String email;   //ohandlovat v jave nech je to unique
    String password;
    String username;
    int typeAccount;

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

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", typeAccount=" + typeAccount +
                '}';
    }
}
