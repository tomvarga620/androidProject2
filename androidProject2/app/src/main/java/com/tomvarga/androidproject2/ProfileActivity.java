package com.tomvarga.androidproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.slide_out_left);
    }
}
