package com.tagi.backend.usuariosapp.backend_usuariosapp.models.dto.mapper;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.dto.UsuarioDto;
import com.tagi.backend.usuariosapp.backend_usuariosapp.models.entities.Usuario;

public class DtoMapperUsuario {

    private Usuario usuario;
    
    private DtoMapperUsuario() {}
    
    public static DtoMapperUsuario getInstance() {
        return new DtoMapperUsuario();
    }

    public DtoMapperUsuario setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public UsuarioDto build() {

        if (usuario == null) {
            throw new RuntimeException("El usuario no a sido inicializado");
        }
        return new UsuarioDto(this.usuario.getId(), this.usuario.getUsuario(), this.usuario.getCorreo());
    }
}
