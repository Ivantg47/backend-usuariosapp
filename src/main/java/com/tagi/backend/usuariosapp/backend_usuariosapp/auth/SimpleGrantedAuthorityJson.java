package com.tagi.backend.usuariosapp.backend_usuariosapp.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJson {

    @JsonCreator
    public SimpleGrantedAuthorityJson(@JsonProperty("authority") String rol){}
}
