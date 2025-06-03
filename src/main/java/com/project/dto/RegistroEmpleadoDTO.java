package com.project.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroEmpleadoDTO {
    private String nombre;
    private String apellidos;
    private String correo;
    private String direccion;
    private String telefono;
    private String contrasenia;
    private boolean admin = false; // Por defecto no es admin
    private LocalDate fechaNac;
    private String dni;
}
