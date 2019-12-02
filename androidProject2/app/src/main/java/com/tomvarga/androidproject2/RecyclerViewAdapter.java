package com.tomvarga.androidproject2;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static String TAG = "RecyclerViewAdapter";
    private ArrayList<Song> listOfSongs;
    Context context;

    public RecyclerViewAdapter(ArrayList<Song> listOfSongs, Context context) {
        this.listOfSongs = listOfSongs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder: called new item is added to list");
        holder.authorTXV.setText(listOfSongs.get(position).getAuthor());
        holder.albumTXV.setText(listOfSongs.get(position).getAlbum());
        holder.songTXV.setText(listOfSongs.get(position).getSongName());
//        holder.playAndPause.setOnClickListener(new View);
    }


    @Override
    public int getItemCount() {
        return listOfSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView authorTXV;
        TextView albumTXV;
        TextView songTXV;
        FloatingActionButton playAndPause;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            authorTXV = itemView.findViewById(R.id.authorTXV);
            albumTXV = itemView.findViewById(R.id.albumTXV);
            songTXV = itemView.findViewById(R.id.songTXV);
            playAndPause = itemView.findViewById(R.id.play_pause);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    

}