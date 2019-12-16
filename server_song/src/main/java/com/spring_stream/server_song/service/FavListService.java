package com.spring_stream.server_song.service;

import com.spring_stream.server_song.model.FavoriteList;
import com.spring_stream.server_song.repozitory.FavoriteListRepozitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavListService {

    @Autowired
    private FavoriteListRepozitory favoriteListRepozitory;

    public FavoriteList insertFavList(FavoriteList newFavList) {
        return favoriteListRepozitory.save(newFavList);
    }

    public List<FavoriteList> getFavListOfUser(Long idUser) {
        return favoriteListRepozitory.findAllByAccount_Id(idUser);
    }

    public List<FavoriteList> getAllFavoritList() {
        return favoriteListRepozitory.findAll();
    }
}
