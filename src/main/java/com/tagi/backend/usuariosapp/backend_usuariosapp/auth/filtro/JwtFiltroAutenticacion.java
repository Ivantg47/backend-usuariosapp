package com.tagi.backend.usuariosapp.backend_usuariosapp.auth.filtro;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

            logger.info("Usuario: " + username + ", Pass: " + password);
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        String usuario = ((User) authResult.getPrincipal()).getUsername();
        String ficha = ("codigo_secreto." + usuario);
        String token = Base64.getEncoder().encodeToString(ficha.getBytes()); // Aquí deberías generar un token JWT real

        response.addHeader("Authorization", "Bearer " + token);

        Map<String, Object> body = HashMap<>();
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
        Map<String, Object> body = HashMap<>();
        body.put("mensaje", "Error de autenticación");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
        
    }

    
    
}
