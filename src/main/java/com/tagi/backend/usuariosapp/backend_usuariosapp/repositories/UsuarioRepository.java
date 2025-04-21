package com.tagi.backend.usuariosapp.backend_usuariosapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String usuario); // Método para buscar por nombre de usuario

    @Query("SELECT u FROM Usuario u WHERE u.usuario = ?1")
    Optional<Usuario> findByUsuarioQuery(String usuario); // Método para buscar por nombre de usuario usando JPQL
}
