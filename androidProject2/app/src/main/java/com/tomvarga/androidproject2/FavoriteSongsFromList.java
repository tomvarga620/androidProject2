package com.tomvarga.androidproject2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomvarga.androidproject2.POJO.Song;
import com.tomvarga.androidproject2.RecycleViewAdapters.RecyclerViewAdapterSongs;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteSongsFromList extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    SharedPrefs modSharedPrefs;

    TextView nameOfList;

    ArrayList<Song> currentFarSongList=new ArrayList<>();
    RecyclerViewAdapterSongs adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modSharedPrefs = new SharedPrefs(this);
        if (modSharedPrefs.loadDarkModeState() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_album_songs);

        Bundle savedData = getIntent().getExtras();
        String title = savedData.getString("title");

        nameOfList = findViewById(R.id.albumText);
        nameOfList.setText(title);

        initRecycleView();

        bottomNav = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_action:
                        Intent homeIntent = new Intent(FavoriteSongsFromList.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;

                    case R.id.favorite_action:
                        Intent favoriteIntent = new Intent(FavoriteSongsFromList.this,FavoriteActivity.class);
                        startActivity(favoriteIntent);
                        finish();
                        break;

                    case R.id.settings_action:
                        Intent settingsIntent = new Intent(FavoriteSongsFromList.this, ProfileActivity.class);
                        startActivity(settingsIntent);
                        finish();
                        break;
                }return true;
            }
        });
    }

    public void loadCurrentSongList() {
        SharedPreferences sharedPreferences = getSharedPreferences("songFarListPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("currentFarListSong", null);
        Type type = new TypeToken<ArrayList<Song>>() {
        }.getType();
        currentFarSongList = gson.fromJson(json, type);
    }

    private void initRecycleView() {
        loadCurrentSongList();
        RecyclerView recyclerView = findViewById(R.id.recycleViewAlbums);
        adapter = new RecyclerViewAdapterSongs(currentFarSongList,this,"some name", (long) 1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
