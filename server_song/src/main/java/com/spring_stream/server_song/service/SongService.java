package com.spring_stream.server_song.service;

import com.spring_stream.server_song.model.Song;
import com.spring_stream.server_song.repozitory.SongsRepozitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongService {

    @Autowired
    private SongsRepozitory songsRepozitory;

    public Song insert(Song song) {
        return songsRepozitory.save(song);
    }

    public Iterable<Song> getAllSongs() {
        return songsRepozitory.findAll();
    }

    public String findPath(String id) {
        Optional<Song> s = songsRepozitory.findById(id);
        if (s.isEmpty()){
            return "";
        }else {
            return s.get().getPath();
        }
    }

    public String findImagePath(String id) {
        Optional<Song> s = songsRepozitory.findById(id);
        if (s.isEmpty()){
            return "";
        }else {
            System.out.println(s.get().getPath());
            System.out.println(s.get().getImgPath());
            return s.get().getImgPath();
        }
    }
}
