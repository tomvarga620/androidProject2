package com.spring_stream.server_song.repozitory;

import com.spring_stream.server_song.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepozitory extends CrudRepository<Account, Long> {

    public Optional<Account> findAccountByEmail(String email);
    public Optional<Account> findAccountByUsername(String userame);
<<<<<<< HEAD
    public Optional<Account> findAccountByUsernameAndPassword(String username, String password);
=======
    public Optional<Account> findAccountByEmailAndPassword(String email, String password);

>>>>>>> 59fe82612588ecb2c13489d1aef25acc86095832
}
