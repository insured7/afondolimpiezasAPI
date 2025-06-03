package com.project.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.modelo.Empleado;
import com.project.servicio.EmpleadoServicio;

@RestController
@RequestMapping("/empleados")
public class EmpleadoControlador {

	@Autowired
	private EmpleadoServicio empleadoServicio;

	@PostMapping
	public ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
		Empleado nuevoEmpleado = empleadoServicio.guardarEmpleado(empleado);
		return ResponseEntity.ok(nuevoEmpleado);
	}

	@GetMapping
	public ResponseEntity<List<Empleado>> listarEmpleados() {
		return ResponseEntity.ok(empleadoServicio.listarTodosEmpleados());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Empleado> obtenerEmpleado(@PathVariable Long id) {
		return ResponseEntity.ok(empleadoServicio.findbyId(id));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id) {
		empleadoServicio.deletebyId(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Empleado> actualizarEmpleadoParcial(@PathVariable Long id,
			@RequestBody Map<String, Object> updates) {
		return ResponseEntity.ok(empleadoServicio.modificarParcialEmpleado(id, updates));
	}
}