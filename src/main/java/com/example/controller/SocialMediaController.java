package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<?> login(@RequestBody Account account) {
        Optional<Account> existingAccount = accountService.findByUsername(account.getUsername());
        if (!existingAccount.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account does not exist");
        }
        if (!existingAccount.get().getPassword().equals(account.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
        return ResponseEntity.ok(existingAccount.get());
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message){
        if(message.getMessageText().length()>255||message.getMessageText() == null|| message.getMessageText().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error message creation");
        }
        if(accountService.findById(message.getPostedBy())==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized error");
        }
        return ResponseEntity.ok(messageService.createMessage(message));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok(messageService.allMessages());
    }
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessagesById(@PathVariable Integer messageId){
        Optional<Message> message = messageService.getMessageByid(messageId);
        return message.isPresent()? ResponseEntity.ok(message.get()): ResponseEntity.ok(null);
    }
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessagesById(@PathVariable Integer messageId){
        Optional<Message> message = messageService.getMessageByid(messageId);
        if(message.isPresent()){
            messageService.deleteMessage(message.get());
            return ResponseEntity.ok(1);
        }else{
            return ResponseEntity.ok().build();
        }
    }
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessagesById(@PathVariable Integer messageId, @RequestBody Message newMessage){
        Optional<Message> potentialMessage = messageService.getMessageByid(messageId);
        if(!potentialMessage.isPresent() ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message does not exist");
        }
        if(newMessage.getMessageText().length()>255||newMessage.getMessageText() == null || newMessage.getMessageText().isBlank()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating message");
        }
        messageService.updateMessage(newMessage);
        return ResponseEntity.ok(1);
    }
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable Integer accountId){
        return ResponseEntity.ok(messageService.allMessagesByAccount(accountId));
    }
}
