package com.tomvarga.androidproject2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterSongs extends RecyclerView.Adapter<RecyclerViewAdapterSongs.ViewHolder> {

    private static String TAG = "RecyclerViewAdapterSongs";
    private ArrayList<Song> listOfSongs;
    Context context;
    String albumName;
    Long albumId;

    public RecyclerViewAdapterSongs(ArrayList<Song> listOfSongs, Context context,String albumName,Long albumId) {
        this.listOfSongs = listOfSongs;
        this.context = context;
        this.albumName=albumName;
        this.albumId = albumId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called new item is added to list");
        holder.authorTXV.setText(listOfSongs.get(position).getAuthor());
      //  holder.albumTXV.setText(listOfSongs.get(position).getAlbum());
        holder.songTXV.setText(listOfSongs.get(position).getSongName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent player = new Intent(view.getContext(), MediaPlayerActivity.class);
                player.putExtra("id",listOfSongs.get(position).getId());
                player.putExtra("author",listOfSongs.get(position).getAuthor());
                player.putExtra("album",albumName);
                player.putExtra("albumId",albumId);
                player.putExtra("genre",listOfSongs.get(position).getGenre());
                player.putExtra("songName",listOfSongs.get(position).getSongName());
                view.getContext().startActivity(player);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listOfSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView authorTXV;
        TextView songTXV;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            authorTXV = itemView.findViewById(R.id.authorTXV);
            songTXV = itemView.findViewById(R.id.songTXV);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}