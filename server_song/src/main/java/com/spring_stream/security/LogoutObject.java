package com.spring_stream.security;

public class LogoutObject {

    String email;
    String token;

    public LogoutObject(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
