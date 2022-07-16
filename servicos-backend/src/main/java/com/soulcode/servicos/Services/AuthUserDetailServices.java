package com.soulcode.servicos.Services;

import com.soulcode.servicos.Models.User;
import com.soulcode.servicos.Repositories.UserRepository;
import com.soulcode.servicos.Security.AuthUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserDetailServices implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username); // filtro por email

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return new AuthUserDetail(user.get().getLogin(), user.get().getPassword());

    }
}


 // O propósito do UserDetailService é carregar de alguma fonte de dados
 // o usuário e criar uma instância de AuthUserDetail, conhecida pelo Spring.

