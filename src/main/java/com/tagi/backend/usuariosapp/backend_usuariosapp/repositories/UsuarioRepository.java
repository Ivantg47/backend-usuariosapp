package com.tagi.backend.usuariosapp.backend_usuariosapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
}
