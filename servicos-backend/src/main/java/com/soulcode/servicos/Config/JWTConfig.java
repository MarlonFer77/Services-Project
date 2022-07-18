package com.soulcode.servicos.Config;

import com.soulcode.servicos.Security.JWTAuthenticationFilter;
import com.soulcode.servicos.Security.JWTAutorizationFilter;
import com.soulcode.servicos.Services.AuthUserDetailServices;
import com.soulcode.servicos.Util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

// Agrega todas as informações de segurança http e gerência do user
@EnableWebSecurity
public class JWTConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    AuthUserDetailServices authUserDetailServices;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // AuthUserDetailServices -> carregar o usuário do banco
        // BCrypt -> garador de hash de senhas
        // usa passwordEncoder() para comparar senhas de login
        auth.userDetailsService(authUserDetailServices).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // habilita o cors e desabilita o csrf
        http.cors().and().csrf().disable();
        // JWTAuthenticationFilter -> é chamado quando usa o /login
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtils));
        http.addFilter(new JWTAutorizationFilter(authenticationManager(), jwtUtils));

        http.authorizeRequests() // autoriza as requisições
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                // .antMatchers(HttpMethod.GET, "/servicos/**").permitAll() // deixa alguns métodos públicos
                .anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean // CROSS ORIGIN RESOURCE SHARING
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // configurações padrões
        configuration.setAllowedMethods(List.of( // quais métodos estão liberados via CORS?
                HttpMethod.GET.name(),
                HttpMethod.PUT.name(),
                HttpMethod.POST.name(),
                HttpMethod.DELETE.name()
        )); // métodos permitidos para o front acessar
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // endpoints permitidos para o front acessar
        source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
        return source;
    }
    // */servicos/funcionarios" -> "/**"
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
