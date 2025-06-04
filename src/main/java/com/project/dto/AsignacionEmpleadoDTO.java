package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionEmpleadoDTO {
	
    private Long solicitudId;
    private Long empleadoId;
    private String rol;
}
