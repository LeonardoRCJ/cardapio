package com.leo.cardapio.services;

import com.leo.cardapio.model.user.User;
import com.leo.cardapio.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void deleteUserById(String id) {

    }
}
