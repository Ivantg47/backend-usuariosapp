package com.tagi.backend.usuariosapp.backend_usuariosapp.services;

import java.util.List;
import java.util.Optional;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.request.UsuarioRequest;

public interface UsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(Long id);

    Usuario save(Usuario usuario);

    Optional<Usuario> update(UsuarioRequest usuario, Long id);

    void remove(Long id);
}
