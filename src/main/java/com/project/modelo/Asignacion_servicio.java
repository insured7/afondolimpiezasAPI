package com.project.modelo;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asignacion_servicios", schema = "core")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion_servicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_asignacion_servicio")
	private Long id_asignacion_servicio;

	// TODO: Comentarios de los FK

	@ManyToOne
	@JoinColumn(name = "empleado_id", nullable = false)
	private Empleado empleado;

	// Campo fecha que se asigna automáticamente cuando se crea el registro
	@CreationTimestamp
	@Column(name = "fecha_asignacion", nullable = false, updatable = false)
	private LocalDateTime fechaAsignacion;

	// Rol que desempeña el empleado
	private String rol;
}
