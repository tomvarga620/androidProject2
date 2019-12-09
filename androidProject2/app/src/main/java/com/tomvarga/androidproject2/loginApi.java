package com.tomvarga.androidproject2;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface loginApi {

    @Headers("Content-Type:application/json")
    @GET("login")
    Call<ResponseBody> logRequest(@Body HashMap<String, String> data);
}
