package com.tomvarga.androidproject2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.tomvarga.androidproject2.POJO.FavoritList;
import com.tomvarga.androidproject2.POJO.Song;
import com.tomvarga.androidproject2.RecycleViewAdapters.RecycleViewAdapterChooseFavList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddToFavListDialog extends AppCompatDialogFragment {

    private EditText typingNewList;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.layout_favllist_dialog,null);

        modSharedPrefs = new SharedPrefs(view.getContext());
        initRecycleView();
        getData();

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        typingNewList = view.findViewById(R.id.editTextNewFavList);

        return builder.create();
    }

    private void initRecycleView() {
        RecyclerView recyclerView = view.findViewById(R.id.recycleChoseFavList);
        adapter = new RecycleViewAdapterChooseFavList(list_favList,view.getContext(),idSong);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void getData() {
        myQueue = Volley.newRequestQueue(view.getContext());

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
                                    Song tempSong = new Song(song.getLong("id"),"","","","");
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
}
