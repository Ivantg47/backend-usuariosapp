package com.tagi.backend.usuariosapp.backend_usuariosapp.services;

import java.util.List;
import java.util.Optional;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.dto.UsuarioDto;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.request.UsuarioRequest;

public interface UsuarioService {

    List<UsuarioDto> findAll();

    Optional<UsuarioDto> findById(Long id);

    UsuarioDto save(Usuario usuario);

    Optional<UsuarioDto> update(UsuarioRequest usuario, Long id);

    void remove(Long id);
}
