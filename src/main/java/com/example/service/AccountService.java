package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> findByUsername(String userName){
        return accountRepository.findByUsername(userName);
    }
    public Account creteAccount(Account account){
        return accountRepository.save(account);
    }
    public Account findById(Integer id){
        Optional<Account> potentialUser = accountRepository.findById(id);
        if(potentialUser.isPresent()){
            return potentialUser.get();
        }
        return null;
    }


}
