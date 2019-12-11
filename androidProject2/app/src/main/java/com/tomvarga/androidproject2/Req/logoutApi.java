package com.tomvarga.androidproject2.Req;

import com.tomvarga.androidproject2.UserData.LoginData;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface logoutApi {

    @POST("logout")
    Call<ResponseBody> logoutRequest(@Body RequestBody body);
}
