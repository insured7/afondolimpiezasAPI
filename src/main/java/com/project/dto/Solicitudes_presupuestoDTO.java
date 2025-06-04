package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitudes_presupuestoDTO {

	private Long solicitudId;
	private String detalles;
    private String estado = "pendiente";
    private String direccion;
    private Long usuarioId;
    private String correo;
    
    

}
