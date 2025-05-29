package com.tagi.backend.usuariosapp.backend_usuariosapp.auth.filtro;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties.Simple;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tagi.backend.usuariosapp.backend_usuariosapp.auth.SimpleGrantedAuthorityJson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.tagi.backend.usuariosapp.backend_usuariosapp.auth.TokenJwtConfig.*;

public class JwtFiltroValidacion extends BasicAuthenticationFilter{

    public JwtFiltroValidacion(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String header = request.getHeader(HEADER);

        if (header == null || !header.startsWith(PREFIJO_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        // byte[] tokenByte = Base64.getDecoder().decode(token);
        // String tokenString = new String(tokenByte);
        // String[] tokenParts = tokenString.split(":");
        // String secret = tokenParts[0];
        // String usuario = tokenParts[1];
        
        try {
            Claims claims = Jwts.parser()
                .verifyWith((SecretKey) CLAVE_SECRETA)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            Object authoritiesClaims = claims.get("authorities");
            String usuario = claims.getSubject();

            Collection<? extends GrantedAuthority> authorities = Arrays
                        .asList(new ObjectMapper()
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJson.class)
                        .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);
        } catch(JwtException e) {
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            // return;
            Map<String, String> body = new HashMap<>();
            body.put("mensaje", "Token inválido");
            body.put("error", e.getMessage());

            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
        }
    }
    
}
