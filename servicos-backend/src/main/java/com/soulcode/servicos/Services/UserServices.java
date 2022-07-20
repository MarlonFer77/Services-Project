package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.User;
import com.soulcode.servicos.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    @Autowired
    UserRepository userRepository;

    @Cacheable("userCache")
    public List<User> buscarTodosOsUsers() {
        return userRepository.findAll();
    }

    @CachePut(value = "userCache", key = "#user.idUser")
    public User cadastrarUser(User user) {
        return userRepository.save(user);
    }
}
