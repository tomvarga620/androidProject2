package com.spring_stream.server_song.service;

import com.spring_stream.server_song.model.ActiveTokens;
import com.spring_stream.server_song.repozitory.ActiveTokensRepozitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActiveTokenService {

    @Autowired
    ActiveTokensRepozitory activeTokensRepozitory;

    public List<ActiveTokens> getActiveUsers() {
        if (activeTokensRepozitory.count()==0){
            return new ArrayList<>();
        }else {
           return activeTokensRepozitory.findAll();
        }
    }

    public void saveActiveUser(ActiveTokens activeUSER) {
        activeTokensRepozitory.save(activeUSER);
    }

    public boolean alreadyAdded(String username) {
        Optional<ActiveTokens> findOrNot = activeTokensRepozitory
                .findByUsername(username);

        if (findOrNot.isEmpty()){
            return false;
        }

        return true;
    }

    public ActiveTokens getActiveToken(String username) {
        Optional<ActiveTokens> findOrNot = activeTokensRepozitory
                .findByUsername(username);

        return findOrNot.get();
    }

    @Transactional
    public void deleteActiveToken(String username) {
        activeTokensRepozitory.deleteActiveTokensByUsername(username);
    }
}
