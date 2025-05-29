package com.tagi.backend.usuariosapp.backend_usuariosapp.models.dto;

public class UsuarioDto {

    private Long id;
    private String usuario;
    private String correo;
    private boolean admin;

    public UsuarioDto() {
    }
    
    public UsuarioDto(Long id, String usuario, String correo, boolean admin) {
        this.id = id;
        this.usuario = usuario;
        this.correo = correo;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    
}
