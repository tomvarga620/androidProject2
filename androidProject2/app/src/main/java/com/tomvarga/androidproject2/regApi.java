package com.tomvarga.androidproject2;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface regApi {
/*
   @Headers("Content-Type:application/json")
   @FormUrlEncoded
   @POST("registration")
    Call<ResponseBody> regRequest(
           @Field("email") String email,
            @Field("password") String password,
            @Field("username") String username,
        @Field("typeAccount") int typeAccount

    );
*/

    @Headers("Content-Type:application/json")
    @POST("registration")
    Call<ResponseBody> regRequest(@Body RegistrationData data);

}
