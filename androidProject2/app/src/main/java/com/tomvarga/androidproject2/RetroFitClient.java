package com.tomvarga.androidproject2;

import com.tomvarga.androidproject2.Req.loginApi;
import com.tomvarga.androidproject2.Req.logoutApi;
import com.tomvarga.androidproject2.Req.regApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitClient {


    private static final String BASE_URL = "http://192.168.137.1:8080/";
    private static RetroFitClient clientInstance;
    private Retrofit retrofit;

    private RetroFitClient(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetroFitClient getInstance(){
        if(clientInstance == null){
            clientInstance = new RetroFitClient();
        }
        return clientInstance;
    }

    public regApi getRegApi(){
        return retrofit.create(regApi.class);
    }

    public loginApi getLoginApi(){
        return retrofit.create(loginApi.class);
    }

    public logoutApi getLogoutApi(){
        return retrofit.create(logoutApi.class);
    }

}
