package com.tomvarga.androidproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bottomNav = findViewById(R.id.bottom_navigation);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_action:
                        Intent homeIntent = new Intent(SettingsActivity.this,MainActivity.class);
                        startActivity(homeIntent);
                        break;

                    case R.id.favorite_action:
                        Intent favoriteIntent = new Intent(SettingsActivity.this,FavoriteActivity.class);
                        startActivity(favoriteIntent);
                        break;

                    case R.id.settings_action:
                        startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
                        finish();
                        break;
                }return true;
            }
        });
    }
}
