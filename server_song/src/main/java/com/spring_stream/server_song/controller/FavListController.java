package com.spring_stream.server_song.controller;

import com.spring_stream.server_song.model.FavoriteList;
import com.spring_stream.server_song.service.FavListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FavListController {

    @Autowired
    private FavListService favListService;

    @PostMapping(value = "/insertFavoriteList", consumes = MediaType.APPLICATION_JSON_VALUE)
    public FavoriteList insertFavList(@RequestBody FavoriteList favoriteList) {
        return favListService.insertFavList(favoriteList);
    }

    @GetMapping(value = "/getAllFavoriteLists", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FavoriteList> getAllFavoriteLists() {
        return favListService.getAllFavoritList();
    }

    @GetMapping(value = "/getUsersFavList", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<FavoriteList> getUsersFavList(@RequestParam Long userId) {
        return favListService.getFavListOfUser(userId);
    }
}
