package com.soulcode.servicos.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulcode.servicos.Models.User;
import com.soulcode.servicos.Util.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;

    public JWTAuthenticationFilter(AuthenticationManager manager, JWTUtils jwtUtils) {
        this.authenticationManager = manager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getLogin(),
                            user.getSenha(),
                            new ArrayList<>())
            );
        }catch (IOException e) {
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        AuthUserDetail user = (AuthUserDetail) authResult.getPrincipal();

        String token = jwtUtils.generateToken(user.getUsername());

        response.setHeader("Acess-Control-Allow-Origin", "*");
        response.setHeader("Acess-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
        response.setHeader("Acess-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
        response.getWriter().flush();

    }
}

/**
 * FRONT MANDA {"login": "jr@gmail.com", "password": "12345"}
 * A partir do JSON -> User
 * Tenta realizar autenticação
 *      Caso dê certo:
 *          - Gera o token JWT
 *          - Retorna o token para o FRONT
 */