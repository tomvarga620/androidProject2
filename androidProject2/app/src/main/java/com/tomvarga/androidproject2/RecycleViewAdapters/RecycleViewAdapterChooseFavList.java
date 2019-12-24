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

    ArrayList<Long> inListAlreadyAdded;

    public RecycleViewAdapterChooseFavList(ArrayList<FavoritList> listOfFavLists, Context context, Long idSong) {
        this.listOfFavLists = listOfFavLists;
        this.context = context;
        this.idSong = idSong;
        listWaitingForAdding = new ArrayList<>();
        listWaitingForRemoving = new ArrayList<>();
        inListAlreadyAdded = new ArrayList<>();
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
            //
            //  Alert toto nefunguje korektne ma to robit to ze ak chce clovek pridat tak to da do listu na pridavanie a checkne box
            //                              ak je to checknute a je to v liste na pridavanie odstrani to
            //                              pozor nepridavaj do listov na odstranenie a pridanie id ktore tam uz su lebo to ide do requestov
            //
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {

                    System.out.println("Clicked to check");

                    if (!listWaitingForAdding.contains(listOfFavLists.get(position).getId())){
                        if (!inListAlreadyAdded.contains(listOfFavLists.get(position).getId())){
                            listWaitingForAdding.add(listOfFavLists.get(position).getId());
                        }
                    }


                    if (listWaitingForRemoving.contains(listOfFavLists.get(position).getId())){
                        listWaitingForRemoving.remove(listOfFavLists.get(position).getId());
                    }

                    holder.checkBox.setChecked(true);

                }else {

                    System.out.println("Clicked to uncheck");

                    if (!listWaitingForRemoving.contains(listOfFavLists.get(position).getId())){
                        if (inListAlreadyAdded.contains(listOfFavLists.get(position).getId())) {
                            listWaitingForRemoving.add(listOfFavLists.get(position).getId());
                        }
                    }

                    if (listWaitingForAdding.contains(listOfFavLists.get(position).getId())){
                        listWaitingForAdding.remove(listOfFavLists.get(position).getId());
                    }

                    holder.checkBox.setChecked(false);

                }

                System.out.println("listWaitingForAdding");
                for (Long id: listWaitingForAdding){
                    System.out.println(id);
                }
                System.out.println();
                System.out.println("listWaitingForRemoving");
                for (Long id: listWaitingForRemoving){
                    System.out.println(id);
                }
                System.out.println();
            }
            //
            //  Alert toto nefunguje korektne ma to robit to ze ak chce clovek pridat tak to da do listu na pridavanie a checkne box
            //                              ak je to checknute a je to v liste na pridavanie odstrani to
            //                              pozor nepridavaj do listov na odstranenie a pridanie id ktore tam uz su lebo to ide do requestov
            //
            //funguje ale problem je ze song ktory je uz na servery a sa odskrtne a zaskrtne tak je v liste ze sa chce pridet co je zle lebo ho este raz prida na servery
            // hint zapamataj si list nejakeho stavu zo servera ako to co uz treba zaskrtnut pri vytvoreni dialogu
        });
        ArrayList<Song> songs =  listOfFavLists.get(position).getSongs();
        for(int a = 0; a<songs.size(); a++) {
            if (songs.get(a).getId() == idSong) {
                holder.checkBox.setChecked(true);
                inListAlreadyAdded.add(listOfFavLists.get(position).getId());
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
