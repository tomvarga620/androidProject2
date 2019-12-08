package com.spring_stream.server_song.service;

import com.spring_stream.server_song.model.Album;
import com.spring_stream.server_song.repozitory.AlbumRepozitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepozitory albumRepozitory;

    public String findImagePath(Long id) {
        Optional<Album> s = albumRepozitory.findById(id);
        if (s.isEmpty()){
            return "";
        }else {
            return s.get().getAlbumCover();
        }
    }

    public Album getAlbumByName(String albumName) {
        Optional<Album> send = albumRepozitory.findAlbumByAlbumName(albumName);
        return send.get();
    }

    public Album getAlbum(Long id) {
        return albumRepozitory.findAlbumById(id).get();
    }

    public Iterable<Album> getAllAlbums() {
        return albumRepozitory.findAll();
    }

    public Album insertAlbum(Album album) {
        return albumRepozitory.save(album);
    }

    public boolean needToCreatedAlbum(String albumName) {
        Optional<Album> isExist = albumRepozitory.findAlbumByAlbumName(albumName);
        if (isExist.isEmpty()) {
            return true;
        }
        return false;
    }

}
