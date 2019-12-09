package com.spring_stream.server_song.controller;

import com.spring_stream.security.Credencials;
import com.spring_stream.security.PrimitiveSecurity;
import com.spring_stream.server_song.model.Account;
import com.spring_stream.server_song.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    private PrimitiveSecurity primitiveSecurity = PrimitiveSecurity.getInstance();

    @PostMapping(path = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registration(@RequestBody Account account) {

        if (accountService.isEmailAlreadyUsed(account.getEmail())){
            return new ResponseEntity<String>(
                    account.getEmail()+" : Email already used",
                    HttpStatus.IM_USED);
        }else if (accountService.isUserNameAlreadyUsed(account.getUsername())) {
            return new ResponseEntity<String>(
                    account.getUsername()+" : Username already used",
                    HttpStatus.IM_USED);
        }else {
            Account account1= accountService.insertNewAccount(account);
            return new ResponseEntity<String>(
                    account1.toString(),
                    HttpStatus.OK);
        }
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> Login(@RequestBody Account account) {
        if (accountService.login(account.getUsername(),account.getPassword())) {
            String token = primitiveSecurity.newLogged(account.getUsername());
            primitiveSecurity.printTokens(); // for debugging
            return new ResponseEntity<String>(
                    token, // return token string
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<String>(
              "Incorrect username or password(authentication credentials)",
              HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(path = "/logout")
    public ResponseEntity Logout(@RequestBody Credencials credencials) {
        System.out.println("username: "+credencials.getusername());
        if (primitiveSecurity.accessTokens.containsKey(credencials.getusername()) && primitiveSecurity.accessTokens.get(credencials.getusername()).equals(credencials.getToken())) {
            primitiveSecurity.accessTokens.remove(credencials.getusername());
            System.out.println(credencials.getusername()+" : Logout successful");
            return new ResponseEntity (HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

    }
}
