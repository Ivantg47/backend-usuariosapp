package com.tagi.backend.usuariosapp.backend_usuariosapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.request.UsuarioRequest;
import com.tagi.backend.usuariosapp.backend_usuariosapp.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "${CORS_ORIGINS}")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> list() {
        return service.findAll();
    }

    // @GetMapping("/{id}")
    // public Usuario show(@PathVariable Long id) {
    //     return service.findById(id).orElseThrow();
    // }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Usuario> usuario = service.findById(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.orElseThrow());
        }

        return ResponseEntity.notFound().build();
        
    }

    // @PostMapping
    // @ResponseStatus(HttpStatus.CREATED)
    // public Usuario create(@RequestBody Usuario usuario) {
    //     return service.save(usuario);
    // }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return validacion(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuario));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UsuarioRequest usuario, BindingResult result, @PathVariable Long id) {
        
        if (result.hasErrors()) {
            return validacion(result);
        }
        
        Optional<Usuario> usuarioOptional = service.update(usuario, id);
        
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOptional.orElseThrow());
        }
        
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Usuario> usuario = service.findById(id);
        
        if (usuario.isPresent()) {
            service.remove(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
    
        private ResponseEntity<?> validacion(BindingResult result) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> {
                errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errors);
        }
}
