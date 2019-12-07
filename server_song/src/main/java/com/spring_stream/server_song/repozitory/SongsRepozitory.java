package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.Album;
import com.spring_stream.server_song.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongsRepozitory extends JpaRepository<Song, Long> {

    public Optional<Song> findById(Long id);
}
