package com.tomvarga.androidproject2.POJO.UserData;

public class RegistrationData {
    private String email;
    private String password;
    private String username;
    private int typeAccount;

    public RegistrationData(String email, String password, String username, int typeAccount) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.typeAccount = typeAccount;
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
}
