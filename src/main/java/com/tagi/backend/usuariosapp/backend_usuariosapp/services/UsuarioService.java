package com.tagi.backend.usuariosapp.backend_usuariosapp.services;

import java.util.List;
import java.util.Optional;

import com.tagi.backend.usuariosapp.backend_usuariosapp.entities.Usuario;

public interface UsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Usuario save(Usuario usuario);

    Optional<Usuario> update(Usuario usuario, Long id);

    void remove(Long id);
}
