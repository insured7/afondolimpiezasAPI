package com.project.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.modelo.Asignacion_servicio;
import com.project.servicio.AsignacionServicioServicio;

@RestController
@RequestMapping("/asignaciones")
public class AsignacionServicioControlador {

	@Autowired
	private AsignacionServicioServicio asignacionServicio;

	// Crear una nueva asignación de empleado a servicio con rol
	@PostMapping("/asignar")
	public ResponseEntity<Asignacion_servicio> asignarEmpleado(	@RequestParam Long empleadoId, @RequestParam String rol) {
		Asignacion_servicio nuevaAsignacion = asignacionServicio.asignarEmpleado(empleadoId, rol);
		return ResponseEntity.ok(nuevaAsignacion);
	}

	// Obtener todas las asignaciones
	@GetMapping
	public ResponseEntity<List<Asignacion_servicio>> obtenerTodasAsignaciones() {
		List<Asignacion_servicio> asignaciones = asignacionServicio.obtenerTodasAsignaciones();
		return ResponseEntity.ok(asignaciones);
	}

	// Obtener asignaciones por empleado
	@GetMapping("/empleado/{empleadoId}")
	public ResponseEntity<List<Asignacion_servicio>> obtenerAsignacionesPorEmpleado(@PathVariable Long empleadoId) {
		List<Asignacion_servicio> asignaciones = asignacionServicio.obtenerAsignacionesPorEmpleado(empleadoId);
		return ResponseEntity.ok(asignaciones);
	}

	// Eliminar una asignación por id
	@DeleteMapping("/{idAsignacion}")
	public ResponseEntity<Void> eliminarAsignacion(@PathVariable Long idAsignacion) {
		asignacionServicio.eliminarAsignacion(idAsignacion);
		return ResponseEntity.noContent().build();
	}

	// Actualizar el rol de una asignación
	@PutMapping("/{idAsignacion}")
	public ResponseEntity<Asignacion_servicio> actualizarRolAsignacion(@PathVariable Long idAsignacion,
			@RequestParam String nuevoRol) {
		Asignacion_servicio asignacionActualizada = asignacionServicio.actualizarRolAsignacion(idAsignacion, nuevoRol);
		return ResponseEntity.ok(asignacionActualizada);
	}
}
