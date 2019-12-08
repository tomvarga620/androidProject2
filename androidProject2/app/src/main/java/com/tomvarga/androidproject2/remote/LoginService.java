package com.tomvarga.androidproject2.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LoginService {

    @GET("login/{username}/{password}")
    Call registration(@Path("username") String email, @Path("password") String password);
}
