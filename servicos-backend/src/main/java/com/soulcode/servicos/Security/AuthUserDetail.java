package com.soulcode.servicos.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


// Abstrai o user do banco para que o security conheça seus dados
public class AuthUserDetail implements UserDetails {

    private String login;
    private String password;

    public AuthUserDetail(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() { //  a conta não expirou
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // a conta não bloqueou
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // as credenciais não expiraram
        return true;
    }

    @Override
    public boolean isEnabled() { // o usuário está habilitado
        return true;
    }
}

// O spring security não se comunica diretamente com o nosso MODEL USER
// Então devemos criar uma classe que ele começa para fazer essa comunicação
// UserDetails => Guarda informações do contexto de autenticação do usuário (autorizações, habilitado, etc)
