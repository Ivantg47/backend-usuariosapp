package com.tagi.backend.usuariosapp.backend_usuariosapp.models.request;

import com.tagi.backend.usuariosapp.backend_usuariosapp.models.IUsuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioRequest implements IUsuario {

    @Size(min = 3, max = 10)
    @NotBlank
    private String usuario;

    @NotBlank
    @Email
    private String correo;

    private boolean admin;
    
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    @Override
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
}
