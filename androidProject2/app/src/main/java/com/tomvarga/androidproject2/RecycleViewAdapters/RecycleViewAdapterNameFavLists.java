package com.tomvarga.androidproject2.RecycleViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomvarga.androidproject2.POJO.FavoritList;
import com.tomvarga.androidproject2.R;

import java.util.ArrayList;

public class RecycleViewAdapterNameFavLists extends RecyclerView.Adapter<RecycleViewAdapterNameFavLists.ViewHolder> {


    ArrayList<FavoritList> favoritLists;

    public RecycleViewAdapterNameFavLists(ArrayList<FavoritList> favoritLists, Context context) {
        this.favoritLists = favoritLists;
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
        holder.name.setText(favoritLists.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return favoritLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        LinearLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameOfList);
            parent = itemView.findViewById(R.id.nameOfListParent);
        }
    }
}
