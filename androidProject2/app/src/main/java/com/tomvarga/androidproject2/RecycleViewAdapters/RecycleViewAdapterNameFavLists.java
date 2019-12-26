package com.tomvarga.androidproject2.RecycleViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.tomvarga.androidproject2.FavoriteSongsFromList;
import com.tomvarga.androidproject2.POJO.FavoritList;
import com.tomvarga.androidproject2.R;
import com.tomvarga.androidproject2.SharedPrefs;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RecycleViewAdapterNameFavLists extends RecyclerView.Adapter<RecycleViewAdapterNameFavLists.ViewHolder> {


    ArrayList<FavoritList> favoritLists;
    SharedPrefs sharedPrefs;

    public RecycleViewAdapterNameFavLists(ArrayList<FavoritList> favoritLists, Context context) {
        this.favoritLists = favoritLists;
        sharedPrefs = new SharedPrefs(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FavoritList favoritList = favoritLists.get(position);
        holder.name.setText(favoritList.getTitle());

        int size = favoritList.getSongs().size();
        ArrayList<ImageView> covers = new ArrayList<>();
        covers.add(holder.first);
        covers.add(holder.second);
        covers.add(holder.third);
        covers.add(holder.fourth);

        if (size > 4){
            for (int a = 0; a<4; a++) {
                new RecycleViewAdapterNameFavLists
                        .SendHttpRequestTask(
                        favoritList.getSongs().get(a).getId(),
                        covers.get(a))
                        .execute();
            }
        }else {
            for (int a=0; a<favoritList.getSongs().size();a++) {
                new RecycleViewAdapterNameFavLists
                        .SendHttpRequestTask(
                        favoritList.getSongs().get(a).getId(),
                        covers.get(a))
                        .execute();
            }
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent player = new Intent(view.getContext(), FavoriteSongsFromList.class);

                player.putExtra("title",favoritList.getTitle());

                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("songFarListPreferences", MODE_PRIVATE);
                SharedPreferences.Editor sharedPreferencesEditor1 = sharedPreferences.edit();
                Gson gson = new Gson();
                String favoriteList = gson.toJson(favoritList.getSongs());
                sharedPreferencesEditor1.putString("currentFarListSong",favoriteList);


                sharedPreferences = view.getContext().getSharedPreferences("songListPreferences", MODE_PRIVATE);
                SharedPreferences.Editor sharedPreferencesEditor2 = sharedPreferences.edit();
                sharedPreferencesEditor2.putString("currentListSong",favoriteList);

                sharedPreferencesEditor1.apply();
                sharedPreferencesEditor2.apply();


                view.getContext().startActivity(player);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView first;
        ImageView second;
        ImageView third;
        ImageView fourth;
        LinearLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameOfList);
            first = itemView.findViewById(R.id.firstImg);
            second = itemView.findViewById(R.id.secondImg);
            third = itemView.findViewById(R.id.thirdImg);
            fourth = itemView.findViewById(R.id.fourImg);
            parent = itemView.findViewById(R.id.nameOfListParent);
        }
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, Bitmap> {

        Long songId;
        ImageView albumCover;

        SendHttpRequestTask(Long albumId, ImageView albumCover) {
            this.songId =albumId;
            this.albumCover = albumCover;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                //ipconfig
                URL url = new URL(sharedPrefs.getIP()+"/getSongImageThumbnail?id="+songId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                final int THUMBSIZE = 200;

                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(myBitmap,
                        THUMBSIZE, THUMBSIZE);

                return ThumbImage;
            }catch (Exception e){
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            albumCover.setImageBitmap(result);
        }
    }
}
