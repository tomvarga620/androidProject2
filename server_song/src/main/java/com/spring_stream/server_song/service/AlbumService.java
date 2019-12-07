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

}
