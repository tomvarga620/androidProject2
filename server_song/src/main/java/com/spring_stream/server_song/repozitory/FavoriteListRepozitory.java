package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.FavoriteList;
import com.spring_stream.server_song.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteListRepozitory extends JpaRepository<FavoriteList, Long> {
    public List<FavoriteList> findAllByAccount_Id(Long id);
    public Optional<FavoriteList> findById(Long id);
}
