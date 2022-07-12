package com.soulcode.servicos.Controllers;

import com.soulcode.servicos.Models.User;
import com.soulcode.servicos.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class UserController {

    @Autowired
    UserServices userServices;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public List<User> bucarTodosOsUsers () {
        return userServices.buscarTodosOsUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<User> cadastrarUser(@RequestBody User user) {
        String senhaCodificada = passwordEncoder.encode(user.getSenha());
        user.setSenha(senhaCodificada);

        user = userServices.cadastrarUser(user);
        return ResponseEntity.ok().body(user);
    }
}
