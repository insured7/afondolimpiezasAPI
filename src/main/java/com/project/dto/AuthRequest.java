package com.project.dto;

/**
 * DTO que contiene correo y contraseña extraidas del claim.
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

	private String correo;
    private String contrasenia;
    
}
