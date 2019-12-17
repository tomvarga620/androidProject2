package com.spring_stream.server_song.controller;

import com.spring_stream.server_song.model.FavoriteList;
import com.spring_stream.server_song.model.Song;
import com.spring_stream.server_song.service.FavListService;
import com.spring_stream.server_song.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RestController
public class FavListController {

    @Autowired
    private FavListService favListService;
    @Autowired
    private SongService songService;

    @PostMapping(value = "/insertFavoriteList", consumes = MediaType.APPLICATION_JSON_VALUE)
    public FavoriteList insertFavList(@RequestBody FavoriteList favoriteList) {
        return favListService.insertFavList(favoriteList);
    }

    @GetMapping(value = "/getAllFavoriteLists", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FavoriteList> getAllFavoriteLists() {
        return favListService.getAllFavoritList();
    }

    @GetMapping(value = "/getUsersFavList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FavoriteList> getUsersFavList(@RequestParam Long userId) {
        return favListService.getFavListOfUser(userId);
    }

    @PostMapping(value = "/addSongToFavList",  produces = MediaType.APPLICATION_JSON_VALUE)
    public FavoriteList addSongToList(@RequestParam Long idList,@RequestParam Long idSong)  {
        FavoriteList favoriteList = favListService.getListById(idList);
        Song song = songService.findById(idSong);

        favoriteList.getSongSet().add(song);
        return favListService.insertFavList(favoriteList);
    }

    @GetMapping(value = "/isSongLiked")
    public ResponseEntity isSongLiked(@RequestParam Long idSong, @RequestParam Long idAccount) {
        List<FavoriteList> allUserList = favListService.getFavListOfUser(idAccount);

        boolean find = false;

        for(FavoriteList current: allUserList) {
            if (find) {
                break;
            }
            for (Song song: current.getSongSet()) {
                if (song.getId() == idSong) {
                    find = true;
                    break;
                }
            }
        }

        if (find) {
            return new ResponseEntity(HttpStatus.FOUND);
        }else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
