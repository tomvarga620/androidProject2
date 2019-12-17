package com.tomvarga.androidproject2.RecycleViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomvarga.androidproject2.POJO.FavoritList;
import com.tomvarga.androidproject2.R;

import java.util.ArrayList;

public class RecycleViewAdapterChooseFavList extends RecyclerView.Adapter<RecycleViewAdapterChooseFavList.ViewHolder> {

    ArrayList<FavoritList> listOfFavLists;
    Context context;
    Long idSong;

    public RecycleViewAdapterChooseFavList(ArrayList<FavoritList> listOfFavLists, Context context, Long idSong) {
        this.listOfFavLists = listOfFavLists;
        this.context = context;
        this.idSong = idSong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favlist_choose_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkBox.setText(listOfFavLists.get(position).getTitle());
        if (listOfFavLists.get(position).getSongs().contains(idSong)){
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return listOfFavLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.ListItem);
        }
    }
}
