package com.tagi.backend.usuariosapp.backend_usuariosapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Usuario> findAll() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        usuario.setPass(passwordEncoder.encode(usuario.getPass()));

        Optional<Role> roleOptional = roleRepository.findByNombre("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        if (roleOptional.isPresent()) {
            roles.add(roleOptional.orElseThrow());
        }

        usuario.setRoles(roles);

        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> update(UsuarioRequest usuario, Long id) {
        Optional<Usuario> usuarioOptional = findById(id);
        Usuario user = null;
        if (usuarioOptional.isPresent()) {
            Usuario usuarioActualizado = usuarioOptional.orElseThrow();
            usuarioActualizado.setUsuario(usuario.getUsuario());
            usuarioActualizado.setCorreo(usuario.getCorreo());
            user = save(usuarioActualizado);
        }

        return Optional.ofNullable(user);
    }

}
