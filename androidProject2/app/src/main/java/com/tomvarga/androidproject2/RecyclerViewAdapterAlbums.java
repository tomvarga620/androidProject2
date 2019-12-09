package com.tomvarga.androidproject2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerViewAdapterAlbums extends RecyclerView.Adapter<RecyclerViewAdapterAlbums.ViewHolder> {

    private static String TAG = "RecyclerViewAdapterAlbums";
    private ArrayList<Album> listOfALbums;
    Context context;

    public RecyclerViewAdapterAlbums(ArrayList<Album> listOfALbums, Context context) {
        this.listOfALbums = listOfALbums;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called new item is added to list");
        holder.albumName.setText(listOfALbums.get(position).getAlbumName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent listOfAlbumSongs = new Intent(view.getContext(),MediaPlayer.class);
                listOfAlbumSongs.putExtra("idAlbum",listOfALbums.get(position).getId());
                view.getContext().startActivity(listOfAlbumSongs);
            }
        });
        new SendHttpRequestTask(listOfALbums.get(position).getId(),holder.albumCover).execute();

    }


    @Override
    public int getItemCount() {
        return listOfALbums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView albumName;
        ImageView albumCover;
        ConstraintLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            albumName = itemView.findViewById(R.id.albumName);
            albumCover = itemView.findViewById(R.id.albumCover);
            parentLayout = itemView.findViewById(R.id.albumParent);
        }
    }


    private class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {

        Long albumId;
        ImageView albumCover;

        SendHttpRequestTask(Long albumId, ImageView albumCover) {
            this.albumId =albumId;
            this.albumCover = albumCover;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                //ipconfig
                URL url = new URL("http://192.168.43.123:8080/getAlbumCover?id="+albumId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                final int THUMBSIZE = 254;

                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(myBitmap,
                        THUMBSIZE, THUMBSIZE);


                return ThumbImage;
            }catch (Exception e){
                Log.d(TAG,e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            albumCover.setImageBitmap(result);
        }
    }
}