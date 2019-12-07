package com.spring_stream.security;

public class Credencials {

    String username;
    String token;

    public Credencials(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getusername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
