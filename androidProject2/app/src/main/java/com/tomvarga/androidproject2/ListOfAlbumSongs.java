package com.tomvarga.androidproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListOfAlbumSongs extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    SharedPrefs modSharedPrefs;

    RecyclerViewAdapterSongs adapter;
    ArrayList<Song> list_songs = new ArrayList<>();
    private RequestQueue myQueue;

    Long idAlbum;
    String albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modSharedPrefs = new SharedPrefs(this);
        if (modSharedPrefs.loadDarkModeState() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        Bundle b = getIntent().getExtras();
        idAlbum = b.getLong("idAlbum");
        albumName = b.getString("albumName");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_album_songs);

        initRecycleView();
        // Button getRequest = findViewById(R.id.getReuqest);
        myQueue = Volley.newRequestQueue(this);

        jsonParse();


        bottomNav = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_action:
                        Intent homeIntent = new Intent(ListOfAlbumSongs.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;

                    case R.id.favorite_action:
                        Intent favoriteIntent = new Intent(ListOfAlbumSongs.this,FavoriteActivity.class);
                        startActivity(favoriteIntent);
                        finish();
                        break;

                    case R.id.settings_action:
                        Intent settingsIntent = new Intent(ListOfAlbumSongs.this, ProfileActivity.class);
                        startActivity(settingsIntent);
                        finish();
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


    private void jsonParse() {

        String url = "http://192.168.137.1:8080/getSongsFromAlbum?IdAlbum="+idAlbum;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject song = response.getJSONObject(i);


                                Long id=song.getLong("id");
                                String author=song.getString("author");
                                String songName=song.getString("songName");
                                String genre=song.getString("genre");

                                Song songObject = new Song(id,author,songName,genre,albumName);

                                list_songs.add(songObject);

                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        myQueue.add(request);
    }

    private RecyclerView.LayoutManager layoutManager;

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recycleviewonlistsongs);
        adapter = new RecyclerViewAdapterSongs(list_songs,this,albumName,idAlbum);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
