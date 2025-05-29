package com.tagi.backend.usuariosapp.backend_usuariosapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.IUsuario;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.dto.UsuarioDto;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.dto.mapper.DtoMapperUsuario;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Role;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.request.UsuarioRequest;
import com.tagi.backend.usuariosapp.backend_usuariosapp.repositories.RoleRepository;
import com.tagi.backend.usuariosapp.backend_usuariosapp.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDto> findAll() {
        List<Usuario> usuarios = (List<Usuario>) repository.findAll();

        return usuarios.stream()
            .map(usuario -> DtoMapperUsuario.getInstance()
                .setUsuario(usuario)
                .build())
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDto> findById(Long id) {
        return repository.findById(id).map(usuario -> DtoMapperUsuario.getInstance()
            .setUsuario(usuario)
            .build());
    }

    @Override
    @Transactional
    public UsuarioDto save(Usuario usuario) {
        usuario.setPass(passwordEncoder.encode(usuario.getPass()));

        usuario.setRoles(getRoles(usuario));

        return DtoMapperUsuario.getInstance()
            .setUsuario(repository.save(usuario))
            .build();
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<UsuarioDto> update(UsuarioRequest usuario, Long id) {
        Optional<Usuario> usuarioOptional = repository.findById(id);
        Usuario user = null;
        System.out.println("Actualizando usuario con ID: " + id);
        if (usuarioOptional.isPresent()) {
            Usuario usuarioActualizado = usuarioOptional.orElseThrow();
            usuarioActualizado.setUsuario(usuario.getUsuario());
            usuarioActualizado.setCorreo(usuario.getCorreo());
            usuarioActualizado.setRoles(getRoles(usuario));
            user = repository.save(usuarioActualizado);
        }

        return Optional.ofNullable(DtoMapperUsuario.getInstance()
            .setUsuario(user)
            .build());
    }

    private List<Role> getRoles(IUsuario usuario) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> userRole = roleRepository.findByNombre("ROLE_USER");
        userRole.ifPresent(roles::add);

        if (usuario.isAdmin()) {
            Optional<Role> adminRole = roleRepository.findByNombre("ROLE_ADMIN");
            adminRole.ifPresent(roles::add);
        }

        return roles;
    }

}
