package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepozitory extends JpaRepository<Account, Long> {

    public Optional<Account> findAccountByEmail(String email);
    public Optional<Account> findAccountByUsername(String userame);
    public Optional<Account> findAccountByUsernameAndPassword(String username, String password);
}
