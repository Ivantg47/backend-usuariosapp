package com.tagi.backend.usuariosapp.backend_usuariosapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByNombre(String nombre); // Método para buscar por nombre de role

}
