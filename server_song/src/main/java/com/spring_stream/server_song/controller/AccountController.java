package com.spring_stream.server_song.controller;

import com.spring_stream.security.LogoutObject;
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

    @GetMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> Login(@RequestBody Account account) {
        if (accountService.login(account.getEmail(),account.getPassword())) {
            String token = primitiveSecurity.newLogged(account.getEmail());
            primitiveSecurity.printTokens(); // for debugging
            return new ResponseEntity<String>(
                    token, // return token string
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<String>(
              "Incorrect email or password(authentication credentials)",
              HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(path = "/logout")
    public ResponseEntity Logout(@RequestBody LogoutObject logoutObject) {

        if (primitiveSecurity.accessTokens.get(logoutObject.getEmail()).equals(logoutObject.getToken())) {
            primitiveSecurity.accessTokens.remove(logoutObject.getEmail());
            System.out.println(logoutObject.getEmail()+" : Logout successful");
            return new ResponseEntity (HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

    }



}
