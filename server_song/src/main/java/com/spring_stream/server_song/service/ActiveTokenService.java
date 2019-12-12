package com.spring_stream.server_song.service;

import com.spring_stream.server_song.model.ActiveTokens;
import com.spring_stream.server_song.repozitory.ActiveTokensRepozitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActiveTokenService {

    @Autowired
    private ActiveTokensRepozitory activeTokensRepozitory;

    public List<ActiveTokens> getActiveUsers() {
        System.out.println("Sum of Records");
        System.out.println(activeTokensRepozitory);
//        System.out.println(activeTokensRepozitory.count());
        return new ArrayList<>();

    }

    public void saveActiveUser(ActiveTokens activeUSER) {
        activeTokensRepozitory.save(activeUSER);
    }

    public boolean alreadyAdded(String username, String token) {
        Optional<ActiveTokens> findOrNot = activeTokensRepozitory
                .findByUsernameAndToken(username,token);

        if (findOrNot.isEmpty()){
            return false;
        }

        return true;
    }

    public ActiveTokens getActiveToken(String username, String token) {
        Optional<ActiveTokens> findOrNot = activeTokensRepozitory
                .findByUsernameAndToken(username,token);

        return findOrNot.get();
    }

    public void deleteActiveToken(String username, String token) {
        activeTokensRepozitory.deleteActiveTokensByUsernameAndToken(username,token);
    }
}
