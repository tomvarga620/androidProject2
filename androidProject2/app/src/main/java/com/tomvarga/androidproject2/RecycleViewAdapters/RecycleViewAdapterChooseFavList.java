package com.tomvarga.androidproject2.RecycleViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomvarga.androidproject2.POJO.FavoritList;
import com.tomvarga.androidproject2.POJO.Song;
import com.tomvarga.androidproject2.R;

import java.util.ArrayList;

public class RecycleViewAdapterChooseFavList extends RecyclerView.Adapter<RecycleViewAdapterChooseFavList.ViewHolder> {

    ArrayList<FavoritList> listOfFavLists;
    ArrayList<Long> listWaitingForAdding;
    ArrayList<Long> listWaitingForRemoving;
    Context context;
    Long idSong;

    public RecycleViewAdapterChooseFavList(ArrayList<FavoritList> listOfFavLists, Context context, Long idSong) {
        this.listOfFavLists = listOfFavLists;
        this.context = context;
        this.idSong = idSong;
        listWaitingForAdding = new ArrayList<>();
        listWaitingForRemoving = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favlist_choose_item_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.checkBox.setText(listOfFavLists.get(position).getTitle());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {
                    listWaitingForRemoving.add(listOfFavLists.get(position).getId());
                    holder.checkBox.setChecked(false);
                }
                if (!holder.checkBox.isChecked()){
                    listWaitingForAdding.add(listOfFavLists.get(position).getId());
                    holder.checkBox.setChecked(true);
                }
            }
        });
        ArrayList<Song> songs =  listOfFavLists.get(position).getSongs();
        for(int a = 0; a<songs.size(); a++) {
            if (songs.get(a).getId() == idSong) {
                holder.checkBox.setChecked(true);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listOfFavLists.size();
    }

    public ArrayList<Long> getListsForAdding() {
        return listWaitingForAdding;
    }

    public ArrayList<Long> getListsForRemoving() {
        return listWaitingForRemoving;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.ListItem);
        }
    }
}
