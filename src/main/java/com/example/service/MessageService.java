package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service    
public class MessageService{
    @Autowired
    MessageRepository messageRepository;

    public Message createMessage(Message message){
        return messageRepository.save(message);
    }
}
