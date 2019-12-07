package com.tomvarga.androidproject2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called new item is added to list");
        holder.authorTXV.setText(listOfSongs.get(position).getAuthor());
      //  holder.albumTXV.setText(listOfSongs.get(position).getAlbum());
        holder.songTXV.setText(listOfSongs.get(position).getSongName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent player = new Intent(view.getContext(),MediaPlayer.class);
                player.putExtra("id",listOfSongs.get(position).getId());
                player.putExtra("author",listOfSongs.get(position).getAuthor());
                player.putExtra("album",listOfSongs.get(position).getAlbum());
                player.putExtra("songName",listOfSongs.get(position).getSongName());
                view.getContext().startActivity(player);
            }
        });
        new SendHttpRequestTask(listOfSongs.get(position).getId(),holder.coverSong).execute();

    }


    @Override
    public int getItemCount() {
        return listOfSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView authorTXV;
        TextView albumTXV;
        TextView songTXV;
        ImageView coverSong;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            authorTXV = itemView.findViewById(R.id.authorTXV);
            albumTXV = itemView.findViewById(R.id.albumTXV);
            songTXV = itemView.findViewById(R.id.songTXV);
            coverSong = itemView.findViewById(R.id.coverSong);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }


    private class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {

        String redId;
        ImageView imageView;

        SendHttpRequestTask(String resId, ImageView imageView) {
            this.redId =resId;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                //ipconfig
                URL url = new URL("http://192.168.43.89:8080/getSongCover?id="+redId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            }catch (Exception e){
                Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}