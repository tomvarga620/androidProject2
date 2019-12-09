package com.tomvarga.androidproject2.Req;

import com.tomvarga.androidproject2.UserData.LoginData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface loginApi {

    @Headers("Content-Type:application/json")
    @POST("login")
    Call<ResponseBody> logRequest(@Body LoginData data);
}
