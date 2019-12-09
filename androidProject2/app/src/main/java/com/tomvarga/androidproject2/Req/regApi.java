package com.tomvarga.androidproject2.Req;

import com.tomvarga.androidproject2.UserData.RegistrationData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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
