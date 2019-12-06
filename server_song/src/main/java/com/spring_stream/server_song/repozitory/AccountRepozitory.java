package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepozitory extends MongoRepository<Account, String> {

    public Optional<Account> findAccountByEmail(String email);
    public Optional<Account> findAccountByUsername(String userame);
    public Optional<Account> findAccountByEmailAndPassword(String email, String password);

}
