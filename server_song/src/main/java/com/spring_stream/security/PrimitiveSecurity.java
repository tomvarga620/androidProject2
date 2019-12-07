package com.spring_stream.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PrimitiveSecurity {

    public static HashMap<String, String> accessTokens;

    private final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private static PrimitiveSecurity primitiveSecurity = null;

    public static PrimitiveSecurity getInstance() {
        if (primitiveSecurity == null) {
            accessTokens = new HashMap<>();
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

        String newToken = generateNewToken();
        accessTokens.put(username,newToken);

        return newToken;
    }

    public void printTokens() {
        System.out.println("Start printing");
        for (Map.Entry<String, String> entry: accessTokens.entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }
}
