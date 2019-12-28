package com.spring_stream.server_song.controller;

import com.spring_stream.security.PrimitiveSecurity;
import com.spring_stream.server_song.model.Account;
import com.spring_stream.server_song.model.FavoriteList;
import com.spring_stream.server_song.model.Song;
import com.spring_stream.server_song.service.AccountService;
import com.spring_stream.server_song.service.FavListService;
import com.spring_stream.server_song.service.SongService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

@RestController
public class FavListController {

    @Autowired
    private FavListService favListService;
    @Autowired
    private SongService songService;
    private PrimitiveSecurity primitiveSecurity = PrimitiveSecurity.getInstance();
    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/insertFavoriteList")
    public FavoriteList insertFavList(@RequestParam String token, @RequestParam String title) {
        FavoriteList favoriteList = new FavoriteList(title,getAccountByToken(token));
        return favListService.insertFavList(favoriteList);
    }

    @DeleteMapping(value = "/deleteFavoriteList")
    public ResponseEntity deleteFavList(@RequestParam String token, @RequestParam Long id) {
        Account account = getAccountByToken(token);
        if (primitiveSecurity.accessTokens.get(account.getUsername()).equals(token)) {
            FavoriteList favoriteList = favListService.getListById(id);
            favoriteList.getSongSet().clear();
            favListService.insertFavList(favoriteList);

            favListService.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/getAllFavoriteLists", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FavoriteList> getAllFavoriteLists() {
        return favListService.getAllFavoritList();
    }

    @GetMapping(value = "/getUsersFavList", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<FavoriteList> getUsersFavList(@RequestParam String token) {

        Long userId =getIdUserByToken(token);

        return favListService.getFavListOfUser(userId);
    }

    @PostMapping(value = "/addSongToFavList",  produces = MediaType.APPLICATION_JSON_VALUE)
    public FavoriteList addSongToList(@RequestParam Long idList,@RequestParam Long idSong)  {
        FavoriteList favoriteList = favListService.getListById(idList);
        Song song = songService.findById(idSong);

        favoriteList.getSongSet().add(song);
        return favListService.insertFavList(favoriteList);
    }

    @PostMapping(value = "/removeSongFromFavList",  produces = MediaType.APPLICATION_JSON_VALUE)
    public FavoriteList removeSongFromList(@RequestParam Long idList,@RequestParam Long idSong)  {
        FavoriteList favoriteList = favListService.getListById(idList);
        Song song = songService.findById(idSong);

        favoriteList.getSongSet().remove(song);
        return favListService.insertFavList(favoriteList);
    }

    @GetMapping(value = "/isSongLiked")
    public ResponseEntity<String> isSongLiked(@RequestParam Long idSong, @RequestParam String token) {
        Long idAccount = getIdUserByToken(token);
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
            return new ResponseEntity("liked",HttpStatus.OK);
        }else {
            return new ResponseEntity("notliked",HttpStatus.OK);
        }
    }

    public Long getIdUserByToken(String token) {
        for (Map.Entry<String, String> entry: primitiveSecurity.accessTokens.entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
            if (entry.getValue().equals(token)) {
                Account account = accountService.findByUsername(entry.getKey());
                return account.getId();
            }
        }
        return Long.valueOf(0);
    }

    public Account getAccountByToken(String token) {
        for (Map.Entry<String, String> entry: primitiveSecurity.accessTokens.entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
            if (entry.getValue().equals(token)) {
                return accountService.findByUsername(entry.getKey());
            }
        }
        return null;
    }
}
