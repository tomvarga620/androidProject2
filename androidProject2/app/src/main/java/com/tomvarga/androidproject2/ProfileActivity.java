package com.tomvarga.androidproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    SharedPrefs modSharedPrefs;

    TextView userText;
    TextView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modSharedPrefs = new SharedPrefs(this);
        if (modSharedPrefs.loadDarkModeState() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userText = findViewById(R.id.userText);
        userText.setText(getUserName());

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser(getUserName(),getUserToken());
            }
        });

        final Switch switchDarkMode = findViewById(R.id.switchDarkMode);
        if(modSharedPrefs.loadDarkModeState() == true){
            switchDarkMode.setChecked(true);
        }
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    modSharedPrefs.setDarkModeState(true);
                    restart();
                }else{
                    modSharedPrefs.setDarkModeState(false);
                    restart();
                }
            }
        });

        bottomNav = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_action:
                        Intent homeIntent = new Intent(ProfileActivity.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;

                    case R.id.favorite_action:
                        Intent favoriteIntent = new Intent(ProfileActivity.this,FavoriteActivity.class);
                        startActivity(favoriteIntent);
                        finish();
                        break;

                    case R.id.settings_action:
                        break;
                }return true;
            }
        });
    }

    private void restart() {
        Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(i);
        //finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_left);
    }

    private String getUserToken(){
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String token = prefs.getString("token",null);
        return token;
    }

    private String getUserName(){
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String username = prefs.getString("username",null);
        return username;
    }

    private void logoutUser(String name,String token){

        HashMap<String, String> params = new HashMap<>();
        params.put("username", name);
        params.put("token", token);
        String strRequestBody = new Gson().toJson(params);

        final RequestBody requestBody = RequestBody.create(MediaType.
                parse("application/json"),strRequestBody);

        Call<ResponseBody> call = RetroFitClient
                .getInstance()
                .getLogoutApi()
                .logoutRequest(requestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Intent i = new Intent(ProfileActivity.this,LoginActivity.class);
                    SharedPreferences.Editor editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                    editor.clear();
                    editor.commit();
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
