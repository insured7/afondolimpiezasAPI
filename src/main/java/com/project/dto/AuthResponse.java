package com.project.dto;

/**
 * Constructor del token.
 * Clase que dará respuesta a cualquier login
 */
public class AuthResponse {

	private String token;

	public AuthResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
