package com.tomvarga.androidproject2.remote;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {

    @GET("registration/{email}/{password}/{username}/{typeAccount}")
    Call registration(@Path("email") String email, @Path("password") String password,
               @Path("username") String username, @Path("typeAccount") int typeAccount);

}
