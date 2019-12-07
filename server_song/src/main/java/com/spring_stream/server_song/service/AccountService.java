package com.spring_stream.server_song.service;

import com.spring_stream.server_song.model.Account;
import com.spring_stream.server_song.repozitory.AccountRepozitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepozitory accountRepozitory;

    public Account insertNewAccount(Account newAccount) {
        return accountRepozitory.save(newAccount);
    }

    public boolean isEmailAlreadyUsed(String email) {
        Optional<Account> findAccount = accountRepozitory.findAccountByEmail(email);

        if (!findAccount.isEmpty()){
            return true;
        }
        return false;
    }

    public boolean isUserNameAlreadyUsed(String username) {
        Optional<Account> findAccount = accountRepozitory.findAccountByUsername(username);

        if (!findAccount.isEmpty()){
            return true;
        }
        return false;
    }

    public boolean login(String username, String password) {
        Optional<Account> loginAccount = accountRepozitory.findAccountByUsernameAndPassword(username,password);

        if (loginAccount.isEmpty()){
            return false;
        }
        return true;
    }
}
