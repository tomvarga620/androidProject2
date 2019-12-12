package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.ActiveTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActiveTokensRepozitory extends JpaRepository<ActiveTokens, Long> {
    public  Optional<ActiveTokens> findByUsernameAndToken(String username, String token);
    public  void deleteActiveTokensByUsernameAndToken(String username, String token);
}
