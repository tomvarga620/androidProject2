package com.tomvarga.androidproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavoriteActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_action:
                        startActivity(new Intent(FavoriteActivity.this,MainActivity.class));
                        finish();
                        break;

                    case R.id.favorite_action:
                        startActivity(new Intent(FavoriteActivity.this,FavoriteActivity.class));
                        finish();
                        break;

                    case R.id.settings_action:
                        Intent settingsIntent = new Intent(FavoriteActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);
                        break;
                }return true;
            }
        });
    }
}
