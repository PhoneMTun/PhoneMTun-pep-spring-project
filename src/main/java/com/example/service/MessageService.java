package com.example.service;
import java.util.List;
import java.util.Optional;

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
    public List<Message> allMessages(){
        return messageRepository.findAll();
    }
    public Optional<Message> getMessageByid(Integer id){
        return messageRepository.findById(id);
    }
    public void deleteMessage(Message message){
        messageRepository.delete(message);
    }
    public Message updatMessage(Message message){
        return messageRepository.save(message);
    }
}
