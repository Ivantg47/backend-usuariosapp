package com.tagi.backend.usuariosapp.backend_usuariosapp.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;
import com.tagi.backend.usuariosapp.backend_usuariosapp.repositories.UsuarioRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = repository.findByUsuarioQuery(username);

        if (!usuarioOptional.isPresent()) {
            throw new UsernameNotFoundException("No se encontro al usuario: " + username);
        } else {
            Usuario usuario = usuarioOptional.orElseThrow();
            List<GrantedAuthority> authorities = usuario.getRoles()
                    .stream()
                    .map(r -> new SimpleGrantedAuthority(r.getNombre()))
                    .collect(Collectors.toList());

            return new User(usuario.getUsuario(), 
                    usuario.getPass(), 
                    true, 
                    true, 
                    true, 
                    true, 
                    authorities);
        }
    } 
    
}
