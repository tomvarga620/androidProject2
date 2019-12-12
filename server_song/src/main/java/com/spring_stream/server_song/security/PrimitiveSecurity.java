package com.spring_stream.security;

import com.spring_stream.server_song.model.ActiveTokens;
import com.spring_stream.server_song.service.ActiveTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.persistence.Access;
import java.security.SecureRandom;
import java.util.*;

@Controller
public class PrimitiveSecurity {

    public static HashMap<String, String> accessTokens;

    @Autowired
    @Qualifier("activeTokenService")
    ActiveTokenService activeTokenService;

    private final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private static PrimitiveSecurity primitiveSecurity = null;

    public static PrimitiveSecurity getInstance() {
        if (primitiveSecurity == null) {
            accessTokens = new HashMap<>();
//            restoreTokens();
            primitiveSecurity = new PrimitiveSecurity();
        }

        return primitiveSecurity;
    }

    private String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
    public String newLogged(String username) {
        System.out.println("activeTokenService "+activeTokenService);
        String newToken = generateNewToken();
        accessTokens.put(username,newToken);
//        if(activeTokenService.alreadyAdded(username,newToken)){
//            ActiveTokens update = activeTokenService.getActiveToken(username,newToken);
//            update.setUsername(username);
//            update.setToken(newToken);
//            activeTokenService.saveActiveUser(update);
//        }else{
//            activeTokenService.saveActiveUser(new ActiveTokens(username,newToken));
//        }

        return newToken;
    }

    public void printTokens() {
        System.out.println("Start printing");
        for (Map.Entry<String, String> entry: accessTokens.entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }

//    private static void restoreTokens() {
//            List<ActiveTokens> toRestore = activeTokenService.getActiveUsers();
//        if (!toRestore.isEmpty()) {
//            for(ActiveTokens current : toRestore) {
//                accessTokens.put(current.getUsername(),current.getToken());
//            }
//        }
//    }
}
