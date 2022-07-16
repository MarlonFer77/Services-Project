package com.soulcode.servicos.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtils { // gerenciar e gerar token

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email) // email do usuário
                .withExpiresAt(
                    new Date(System.currentTimeMillis() + expiration)
                ).sign(Algorithm.HMAC512(secret));
    }

    public String getLogin(String token) {
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(token)
                .getSubject();
    }
}
