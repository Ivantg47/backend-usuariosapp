package com.tagi.backend.usuariosapp.backend_usuariosapp.auth;

import java.security.Key;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {

    public static final String PREFIJO_TOKEN = "Bearer ";
    public static final String HEADER = "Authorization";
    public static final Key CLAVE_SECRETA = Jwts.SIG.HS512.key().build();;

}
