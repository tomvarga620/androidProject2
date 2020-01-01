package com.tomvarga.androidproject2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomvarga.androidproject2.POJO.Album;
import com.tomvarga.androidproject2.POJO.Song;
import com.tomvarga.androidproject2.RecycleViewAdapters.RecyclerViewAdapterSongs;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoriteSongsFromList extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    SharedPrefs modSharedPrefs;

    TextView nameOfList;
    Long idFavList;

    private RequestQueue queue;
    ArrayList<Song> currentFarSongList=new ArrayList<>();
    RecyclerViewAdapterSongs adapter;

    HashMap<Long,List<Long>> idListWithIdSongsFromRemoving;

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

        queue = Volley.newRequestQueue(this);

        Bundle savedData = getIntent().getExtras();
        String title = savedData.getString("title");
        idFavList = savedData.getLong("idFavList");

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

        for (int a=0;a<currentFarSongList.size();a++) {
            setAlbumRequest(currentFarSongList.get(a).getId(),a);
        }
    }

    private void initRecycleView() {
        loadCurrentSongList();
        RecyclerView recyclerView = findViewById(R.id.recycleViewAlbums);
        adapter = new RecyclerViewAdapterSongs(currentFarSongList,this,"favListCase", (long) -1);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setAlbumRequest(Long idSong, final int position){
        String url = modSharedPrefs.getIP()+"/getAlbumByIdSong?id="+idSong;

        JsonObjectRequest getAlbumInfo = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    currentFarSongList.get(position).setAlbum(
                            new Album(
                                    response.getLong("id"),
                                    response.getString("albumName")
                            )
                    );

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (position == currentFarSongList.size()-1){
                    adapter.notifyDataSetChanged();

                    SharedPreferences sharedPreferences = getSharedPreferences("songListPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor sharedPreferencesEditor2 = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String favoriteList = gson.toJson(currentFarSongList);
                    sharedPreferencesEditor2.putString("currentListSong",favoriteList);
                    sharedPreferencesEditor2.apply();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FavoriteSongsFromList.this,"Error: "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        queue.add(getAlbumInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("FavoriteSongsFromList onResume");
        get_RemoveIdSongfromIdList();
        removeSongsWhichWasRemoved();
        adapter.notifyDataSetChanged();
    }

    private void get_RemoveIdSongfromIdList() {
        Gson gson = new Gson();
        SharedPreferences share = getSharedPreferences("songFarListPreferences",MODE_PRIVATE);
        String getHashMap = share.getString("removeIdSongFromIdList",null);
        java.lang.reflect.Type type = new TypeToken<HashMap<Long, List<Long>>>(){}.getType();
        idListWithIdSongsFromRemoving = gson.fromJson(getHashMap,type);
        if (idListWithIdSongsFromRemoving == null){
            idListWithIdSongsFromRemoving = new HashMap<>();
            System.out.println("Initializing list");
        }
    }

    private void save_RemoveIdSongfromIdList() {
        Gson gson = new Gson();
        String hashMaptoSave = gson.toJson(idListWithIdSongsFromRemoving);

        SharedPreferences share = getSharedPreferences("songFarListPreferences",MODE_PRIVATE);
        share.edit().putString("removeIdSongFromIdList",hashMaptoSave).apply();
    }

    private void removeSongsWhichWasRemoved() {
        System.out.println("removeSongsWhichWasRemoved");

        if (idListWithIdSongsFromRemoving.containsKey(idFavList)){
            List<Long> idSongs = idListWithIdSongsFromRemoving.get(idFavList);

            System.out.println("IDSongs: "+idSongs.toString());
            System.out.println("currentFarSongList size "+currentFarSongList.size());

            int a = 0;
            int index = 0;
            int allSongs = currentFarSongList.size();

            while(index!=allSongs){
                System.out.println("Index: "+index);
                index++;
                if (idSongs.contains(currentFarSongList.get(a).getId())){
                    idListWithIdSongsFromRemoving.get(idFavList).remove(currentFarSongList.get(a).getId());
                    currentFarSongList.remove(currentFarSongList.get(a));

                    System.out.println("current_Far_Song_List: "+currentFarSongList.size());
                    System.out.println("id_SONGS_for_removing: "+idListWithIdSongsFromRemoving.get(idFavList).toString());
                }else {
                    a++;
                }
            }
            save_RemoveIdSongfromIdList();

        }
    }
}
