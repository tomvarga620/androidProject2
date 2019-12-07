package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepozitory extends JpaRepository<Album, Long> {

    public Optional<Album> findAlbumById(Long id);
    public Optional<Album> findAlbumByAlbumName(String albumName);
}
