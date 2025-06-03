package com.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Constructor del token.
 * Clase que dará respuesta a cualquier login
 */
public class AuthResponseAdmin {
    private String token;
    private boolean esAdmin;

    public AuthResponseAdmin(String token, boolean esAdmin) {
        this.token = token;
        this.esAdmin = esAdmin;
    }

    public String getToken() {
        return token;
    }

    public boolean getEsAdmin() {
        return esAdmin;
    }
}

