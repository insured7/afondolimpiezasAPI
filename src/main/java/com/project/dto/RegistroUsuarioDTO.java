package com.project.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {
    private String nombre;
    private String apellidos;
    private String correo;
    private String direccion;
    private String telefono;
    private String contrasenia;
}
