package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.FavoriteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteListRepozitory extends JpaRepository<FavoriteList, Long> {
    public List<FavoriteList> findAllByAccount_Id(Long id);
}
