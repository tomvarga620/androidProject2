package com.tomvarga.androidproject2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tomvarga.androidproject2.POJO.Album;
import com.tomvarga.androidproject2.POJO.FavoritList;
import com.tomvarga.androidproject2.POJO.Song;
import com.tomvarga.androidproject2.RecycleViewAdapters.RecycleViewAdapterChooseFavList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddToFavListDialog extends AppCompatDialogFragment {

    private FloatingActionButton favSong;
    private EditText typingNewList;
    private Button createNewList;
    HashMap<Long, List<Long>> removeIdSongfromIdList;


    RecycleViewAdapterChooseFavList adapter;
    ArrayList<FavoritList> list_favList = new ArrayList<>();
    private RequestQueue myQueue;
    SharedPrefs modSharedPrefs;

    View view;
    Long idSong;
    String token;

    public AddToFavListDialog(Long idSong, String token) {
        this.idSong = idSong;
        this.token = token;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        modSharedPrefs = new SharedPrefs(getActivity());
        AlertDialog.Builder builder;
        if (modSharedPrefs.loadDarkModeState() == true) {
            builder = new AlertDialog.Builder(getActivity(),R.style.DialogeThemeDark);
        } else {
            builder = new AlertDialog.Builder(getActivity(),R.style.DialogeTheme);
        }


        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.layout_favllist_dialog,null);
        myQueue = Volley.newRequestQueue(view.getContext());

        initRecycleView();
        getData();
        get_RemoveIdSongfromIdList();

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ArrayList<Long> forAdding = adapter.getListsForAdding();
                        ArrayList<Long> forRemoving = adapter.getListsForRemoving();

                        for (Long idList: forAdding) {

                            if (removeIdSongfromIdList.containsKey(idSong)){
                                if (removeIdSongfromIdList.get(idSong).contains(idList)){
                                    removeIdSongfromIdList.get(idSong).remove(idList);
                                }
                            }
                            sendPostRequestAddToList(idList);
                        }

                        for (Long idList: forRemoving) {
                            if (removeIdSongfromIdList.containsKey(idSong)){
                                removeIdSongfromIdList.get(idSong).add(idList);
                            }else {
                                List<Long> listOfLists = new ArrayList<>();
                                listOfLists.add(idList);
                                removeIdSongfromIdList.put(idSong,listOfLists);
                            }
                            sendPostRequestRemoveFromList(idList);
                        }
                        save_RemoveIdSongfromIdList();

                        try {
                            ((MediaPlayerActivity) getActivity()).isSongLiked();
                        }catch (Exception e) {
                            e.getMessage();
                        }
                    }
                });

        typingNewList = view.findViewById(R.id.editTextNewFavList);
        createNewList = view.findViewById(R.id.createList);

        createNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(typingNewList.getText());
                if (name.length() >= 3) {
                    typingNewList.setText("");
                    sendNewListPostRequest(name);
                }else {
                    Toast.makeText(view.getContext(),"Name of new created list must be at least 3 length long",Toast.LENGTH_LONG).show();
                }
            }
        });

        return builder.create();
    }



    private void initRecycleView() {
        RecyclerView recyclerView = view.findViewById(R.id.recycleChoseFavList);
        adapter = new RecycleViewAdapterChooseFavList(list_favList,view.getContext(),idSong);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void getData() {
        String url = modSharedPrefs.getIP()+"/getUsersFavList?token="+token;
        list_favList.clear();
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
                                    Song tempSong = new Song(song.getLong("id"),"","","",new Album((long) 0,""),"");
                                    songsArray.add(tempSong);
                                }

                                FavoritList favoritList = new FavoritList(id,name,songsArray);

                                list_favList.add(favoritList);
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

    private void sendNewListPostRequest(final String title) {

            String URL = modSharedPrefs.getIP()+"/insertFavoriteList?token="+token+"&title="+title;

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(view.getContext(), "Favorit list: "+title+" was created", Toast.LENGTH_SHORT).show();
                    getData();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(view.getContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
            };
           myQueue.add(jsonOblect);
    }

    private void sendPostRequestAddToList(Long idList) {

        String URL = modSharedPrefs.getIP()+"/addSongToFavList?idList="+idList+"&idSong="+idSong;

        JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(view.getContext(), "Successfully added to favorit list", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
        };
        myQueue.add(jsonOblect);
    }

    private void sendPostRequestRemoveFromList(Long idList) {

        String URL = modSharedPrefs.getIP()+"/removeSongFromFavList?idList="+idList+"&idSong="+idSong;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(view.getContext(), "Successfully removed from favorit list", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
        };
        myQueue.add(jsonObjectRequest);
    }

    private void get_RemoveIdSongfromIdList() {
        System.out.println("get_RemoveIdSongfromIdList called");
        Gson gson = new Gson();

        SharedPreferences share = view.getContext().getSharedPreferences("songFarListPreferences",view.getContext().MODE_PRIVATE);
        String getHashMap = share.getString("removeIdSongFromIdList",null);
        java.lang.reflect.Type type = new TypeToken<HashMap<Long,List<Long>>>(){}.getType();
        removeIdSongfromIdList = gson.fromJson(getHashMap,type);
        if (removeIdSongfromIdList == null){
            removeIdSongfromIdList = new HashMap<>();
            System.out.println("Initializing list");
        }else{
            System.out.println("removeIdSongFromList none null");
        }
        printHashMap();
    }

    private void save_RemoveIdSongfromIdList() {
        System.out.println("save_RemoveIdSongfromIdList called");

        Gson gson = new Gson();
        String hashMaptoSave = gson.toJson(removeIdSongfromIdList);

        SharedPreferences share = view.getContext().getSharedPreferences("songFarListPreferences",view.getContext().MODE_PRIVATE);
        share.edit().putString("removeIdSongFromIdList",hashMaptoSave).apply();

        printHashMap();

    }


    private void printHashMap() {
        System.out.println("printHashMap called");
            Iterator iterator = removeIdSongfromIdList.entrySet().iterator();
        Log.i("PAIR OF SONG AND LIST","starting printing");
        while (iterator.hasNext()) {
                Map.Entry pair = (Map.Entry)iterator.next();
                Log.i("SongId",pair.getKey().toString());
                List<Long> tempArray = (List<Long>) pair.getValue();
                for (Long idList:tempArray) {
                    Log.i("ListId",idList.toString());
                }
                Log.i("PAIR OF SONG AND LIST","End of this id");
//                iterator.remove();
            }
    }
}
