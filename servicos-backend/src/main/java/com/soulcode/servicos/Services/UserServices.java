package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.User;
import com.soulcode.servicos.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    @Autowired
    UserRepository userRepository;

    public List<User> buscarTodosOsUsers() {
        return userRepository.findAll();
    }

    public User cadastrarUser(User user) {
        return userRepository.save(user);
    }
}
