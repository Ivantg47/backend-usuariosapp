package com.tagi.backend.usuariosapp.backend_usuariosapp.auth.filtro;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.tagi.backend.usuariosapp.backend_usuariosapp.auth.TokenJwtConfig.*;

public class JwtFiltroAutenticacion extends UsernamePasswordAuthenticationFilter {

    // Aquí puedes agregar la lógica para el filtro de autenticación JWT
    // Por ejemplo, puedes sobrescribir el método attemptAuthentication para validar el token JWT
    // y establecer la autenticación en el contexto de seguridad de Spring Security.

    private AuthenticationManager authenticationManager;
    
    public JwtFiltroAutenticacion(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        Usuario usuario = null;
        String username = null;
        String password = null;

        try {
            usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
            username = usuario.getUsuario();
            password = usuario.getPass();

        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        String usuario = ((User) authResult.getPrincipal()).getUsername();
        System.out.println("Usuario autenticado: " + usuario);
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
        boolean isAdmin = roles.stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        // String ficha = (codigoSecreto + ":" + usuario);
        // String token = Base64.getEncoder().encodeToString(ficha.getBytes()); // Aquí deberías generar un token JWT real
        String token = Jwts.builder()
                .claims(Map.of(
                    "authorities", new ObjectMapper().writeValueAsString(roles),
                    "isAdmin", isAdmin
                ))
                .subject(usuario)
                .signWith(CLAVE_SECRETA)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 1 día de expiración
                .compact();

        response.addHeader(HEADER, PREFIJO_TOKEN + token);

        Map<String, Object> body = new HashMap<>();
        body.put("usuario", usuario);
        body.put("token", token);
        body.put("mensaje", "Autenticación exitosa");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Error de autenticación");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
        
    }

    
    
}
