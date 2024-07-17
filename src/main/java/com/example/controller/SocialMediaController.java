package com.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account){
        if(account.getUsername() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username cannot be empty");
        }
        if( account.getPassword().length() <4 ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must be at least 4 characters");
        }
        Optional<Account> existingAccount = accountService.findByUsername(account.getUsername());
        if(existingAccount.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exist");
        }
        
        return ResponseEntity.ok(accountService.creteAccount(account));

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account){
        Optional<Account> existingAccount = accountService.findByUsername(account.getUsername()); 
        if(!existingAccount.isPresent()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account does not exist");
        }
        if (existingAccount.get().getPassword()!=account.getPassword()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account does not exist");
        } 
        return ResponseEntity.ok(existingAccount.get());

    }
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message){
        if(message.getMessageText().length()>255||message.getMessageText() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error message creation");
        }
        if(accountService.findById(message.getPostedBy())==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized error");
        }
        return ResponseEntity.ok(messageService.createMessage(message));
    }
}
