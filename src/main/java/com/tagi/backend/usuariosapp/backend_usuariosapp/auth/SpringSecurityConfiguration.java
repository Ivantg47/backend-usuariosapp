package com.tagi.backend.usuariosapp.backend_usuariosapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.tagi.backend.usuariosapp.backend_usuariosapp.auth.filtro.JwtFiltroAutenticacion;
import com.tagi.backend.usuariosapp.backend_usuariosapp.auth.filtro.JwtFiltroValidacion;

@Configuration
public class SpringSecurityConfiguration {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, @Value("${auth.codigo_secreto}") String codigoSecreto) throws Exception {
        return http.authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.GET, "/usuarios").permitAll()
                .anyRequest().authenticated())
            .addFilter(new JwtFiltroAutenticacion(authenticationConfiguration.getAuthenticationManager(), codigoSecreto))
            .addFilter(new JwtFiltroValidacion(authenticationConfiguration.getAuthenticationManager(), codigoSecreto))
            .csrf(config -> config.disable())
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();

    }
}
