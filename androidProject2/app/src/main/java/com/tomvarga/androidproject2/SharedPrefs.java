package com.tomvarga.androidproject2;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    SharedPreferences modeSharedPrefs;

    public SharedPrefs(Context context){
        modeSharedPrefs = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }

    public void setDarkModeState(Boolean state){
        SharedPreferences.Editor editor = modeSharedPrefs.edit();
        editor.putBoolean("darkmode",state);
        editor.commit();
    }
    public Boolean loadDarkModeState(){
        Boolean state = modeSharedPrefs.getBoolean("darkmode",false);
        return state;
    }

    public String getIP() {
        String ip = "http://192.168.43.123:8080";
        return ip;
    }
}

