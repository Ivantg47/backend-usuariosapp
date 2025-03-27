package com.tagi.backend.usuariosapp.backend_usuariosapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tagi.backend.usuariosapp.backend_usuariosapp.entities.Usuario;
import com.tagi.backend.usuariosapp.backend_usuariosapp.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

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
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> update(Usuario usuario, Long id) {
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
