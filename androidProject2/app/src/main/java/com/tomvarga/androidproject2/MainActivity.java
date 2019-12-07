package com.tomvarga.androidproject2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    SharedPrefs modSharedPrefs;

    RecyclerViewAdapter adapter;
    ArrayList<Song> list_songs = new ArrayList<>();
    private RequestQueue myQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modSharedPrefs = new SharedPrefs(this);
        if (modSharedPrefs.loadDarkModeState() == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                        break;

                    case R.id.favorite_action:
                        Intent favoriteIntent = new Intent(MainActivity.this,FavoriteActivity.class);
                        startActivity(favoriteIntent);
                        finish();
                        break;

                    case R.id.settings_action:
                        Intent settingsIntent = new Intent(MainActivity.this, ProfileActivity.class);
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

        String url = "http://192.168.43.89:8080/getAllSongs";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = response.getJSONObject(i);

                                String id = person.getString("id");
                                String author = person.getString("author");
                                String songName = person.getString("songName");
                                String album = person.getString("album");

                                Song songObject = new Song(id,author,songName,album);
                                System.out.println(songName.toString());

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
        RecyclerView recyclerView = findViewById(R.id.songRecyclerViewOnMain);
        adapter = new RecyclerViewAdapter(list_songs,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
