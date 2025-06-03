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

	private String detalles;
    private String estado;
    private String direccion;
    private Long usuarioId;
    

}
