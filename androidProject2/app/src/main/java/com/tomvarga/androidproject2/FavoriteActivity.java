package com.tomvarga.androidproject2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tomvarga.androidproject2.POJO.Album;
import com.tomvarga.androidproject2.POJO.FavoritList;
import com.tomvarga.androidproject2.POJO.Song;
import com.tomvarga.androidproject2.RecycleViewAdapters.RecycleViewAdapterNameFavLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    SharedPrefs modSharedPrefs;
    private RequestQueue queue;
    private RecycleViewAdapterNameFavLists adapter;
    private ArrayList<FavoritList> favoritLists = new ArrayList<>();

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modSharedPrefs = new SharedPrefs(this);
        if (modSharedPrefs.loadDarkModeState() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        Log.i("FavoriteActivity","onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        token = getSharedPreferences("user", MODE_PRIVATE).getString("token","default");

        initRecycleView();
        getData();

        bottomNav = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_action:
                        Intent homeIntent = new Intent(FavoriteActivity.this,MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                        break;

                    case R.id.favorite_action:
                        break;

                    case R.id.settings_action:
                        Intent settingsIntent = new Intent(FavoriteActivity.this, ProfileActivity.class);
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

    private void getData() {
        queue = Volley.newRequestQueue(this);

        String url = modSharedPrefs.getIP()+"/getUsersFavList?token="+token;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject favList = response.getJSONObject(i);

                                Long id = favList.getLong("id");
                                String name = favList.getString("title");

                                JSONArray songsJson = favList.getJSONArray("songSet");
                                ArrayList<Song> songsArray = new ArrayList<>();
                                for (int a = 0; a < songsJson.length(); a++) {
                                    JSONObject song = songsJson.getJSONObject(a);
                                    Song tempSong = new Song(
                                            song.getLong("id"),
                                            song.getString("author"),
                                            song.getString("songName"),
                                            song.getString("genre"),
                                            new Album((long) 0,"default"),
                                            song.getString("path")
                                    );
                                    songsArray.add(tempSong);
                                }

                                FavoritList favoritList = new FavoritList(id,name,songsArray);

                                favoritLists.add(favoritList);
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

        queue.add(request);

    }

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.favorit_list_RecycleView);
        adapter = new RecycleViewAdapterNameFavLists(favoritLists,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
