package com.tomvarga.androidproject2.remote;

public class ApiUtils {
    public static final String BASE_URL = "http://localhost:8080/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

}
