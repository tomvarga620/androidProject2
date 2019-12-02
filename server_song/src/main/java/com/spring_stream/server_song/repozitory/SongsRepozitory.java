package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.Song;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongsRepozitory extends MongoRepository<Song, String> {

    @Query(value = "{ 'author' : ?0, 'songName' : ?2, 'album' : ?1 }")
    public Optional<Song> findByAuthorAndAlbumAndSongName(String author, String album, String songName);

}
