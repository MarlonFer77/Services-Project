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
import java.util.Date;

// essa classe entra em ação ao chamar o /login
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;

    public JWTAuthenticationFilter(AuthenticationManager manager, JWTUtils jwtUtils) {
        this.authenticationManager = manager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // tenta autenticar o usuário
        try{
            // {"login": "" , "password": ""}
            // extrair informações do user da request "bruta"
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate( // chama a autenticação do spring
                    new UsernamePasswordAuthenticationToken(
                            user.getLogin(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        }catch (IOException e) {
            // caso o json da requisição não bater om o user.class
            throw  new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // gerar o token e devolver para o usuário que se autenticou com sucesso
        AuthUserDetail user = (AuthUserDetail) authResult.getPrincipal();

        String token = jwtUtils.generateToken(user.getUsername());

        response.setHeader("Acess-Control-Allow-Origin", "*");
        response.setHeader("Acess-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
        response.setHeader("Acess-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        response.getWriter().write("{\"Authorization\": \"" + token + "\"}"); // escreve no body
        response.getWriter().flush(); // termina a escrita

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // customizar a reposta de erro do login que falhou
        response.setStatus(401); // unauthorized
        response.setContentType("application/json");
        response.getWriter().write(json()); // mensagem de erro no body
        response.getWriter().flush(); // termina a escrita
    }

    String json() { // formatar a mensagem de erro
        long date = new Date().getTime();
        return "{"
                + "\"timestamp\": " + date + ", "
                + "\"status\": 401,"
                + "\"error\" : \"Não autorizado\", "
                + "\"message\": \"Email/senha inválidos\","
                + "\"path\": \"/login\""
                + "}";

    }
}


//  FRONT MANDA {"login": "jr@gmail.com", "password": "12345"}
//  A partir do JSON -> User
//  Tenta realizar autenticação
//       Caso dê certo:
//          - Gera o token JWT
//           - Retorna o token para o FRONT

