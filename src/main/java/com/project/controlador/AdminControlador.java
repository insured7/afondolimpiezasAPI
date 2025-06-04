package com.project.controlador;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AsignacionEmpleadoDTO;
import com.project.dto.Solicitudes_presupuestoDTO;
import com.project.modelo.Asignacion_servicio;
import com.project.modelo.Empleado;
import com.project.modelo.Solicitud_presupuesto;
import com.project.modelo.Usuario;
import com.project.servicio.AsignacionServicioServicio;
import com.project.servicio.EmpleadoServicio;
import com.project.servicio.Solicitud_presupuestoServicio;
import com.project.servicio.UsuarioServicio;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin/solicitudes")
public class AdminControlador {

	@Autowired
	private Solicitud_presupuestoServicio solicitudServicio;

	@Autowired
	private UsuarioServicio usuarioservicio;

	@Autowired
	private AsignacionServicioServicio asignacionServicio;

	@Autowired
	private EmpleadoServicio empleadoServicio;

	@PostMapping
	public ResponseEntity<Solicitud_presupuesto> crearSolicitud(@RequestBody Solicitud_presupuesto solicitud,
			Principal principal) {
		String correo = principal.getName();

		Usuario usuario = usuarioservicio.findByCorreo(correo);
		if (usuario == null) {
			return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).build();
		}

		solicitud.setUsuario(usuario);

		Solicitud_presupuesto creada = solicitudServicio.guardar(solicitud);

		return ResponseEntity.status(HttpStatus.SC_CREATED).body(creada);
	}

	// Listar todas las solicitudes
	@GetMapping
	public List<Solicitudes_presupuestoDTO> listarSolicitudes() {
		List<Solicitud_presupuesto> solicitudes = solicitudServicio.listarTodas();
		List<Solicitudes_presupuestoDTO> dtos = new ArrayList<>();

		for (Solicitud_presupuesto s : solicitudes) {
			Solicitudes_presupuestoDTO dto = new Solicitudes_presupuestoDTO();
			dto.setSolicitudId(s.getId_solicitudes_presupuesto());
			dto.setEstado(s.getEstado());
			dto.setDetalles(s.getDetalles());
			dto.setDireccion(s.getDireccion());
			if (s.getUsuario() != null) {
				dto.setCorreo(s.getUsuario().getCorreo());
			} else {
				dto.setCorreo("Desconocido");
			}
			dtos.add(dto);
		}

		return dtos;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Solicitud_presupuesto> obtenerSolicitud(@PathVariable Long id) {
		Optional<Solicitud_presupuesto> solicitud = solicitudServicio.buscarPorId(id);

		if (solicitud.isPresent()) {
			return ResponseEntity.ok(solicitud.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Solicitud_presupuesto> actualizarSolicitud(@PathVariable Long id,
			@RequestBody Solicitud_presupuesto solicitud) {
		Optional<Solicitud_presupuesto> solicitudExistente = solicitudServicio.buscarPorId(id);

		if (solicitudExistente.isPresent()) {
			Solicitud_presupuesto s = solicitudExistente.get();
			s.setEstado(solicitud.getEstado());
			s.setDetalles(solicitud.getDetalles());
			s.setDireccion(solicitud.getDireccion());
			solicitudServicio.guardar(s);
			return ResponseEntity.ok(s);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Eliminar solicitud
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
		if (solicitudServicio.buscarPorId(id).isPresent()) {

			List<Asignacion_servicio> asignaciones = asignacionServicio.findBySolicitudId(id);

			if (asignaciones != null && !asignaciones.isEmpty()) {
				asignacionServicio.eliminarTodas(asignaciones);
			}
			solicitudServicio.eliminar(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/asignar-empleado")
	public ResponseEntity<?> asignarEmpleado(@RequestBody AsignacionEmpleadoDTO dto) {
		Optional<Solicitud_presupuesto> solicitud = solicitudServicio.buscarPorId(dto.getSolicitudId());
		Optional<Empleado> empleado = empleadoServicio.findById(dto.getEmpleadoId());

		if (solicitud.isEmpty() || empleado.isEmpty()) {
			return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("Solicitud o Empleado no encontrados");
		}

		Asignacion_servicio asignacion = new Asignacion_servicio();
		asignacion.setSolicitud(solicitud.get());
		asignacion.setEmpleado(empleado.get());
		asignacion.setRol(dto.getRol());

		asignacionServicio.asignarEmpleadoASolicitud(asignacion);

		return ResponseEntity.ok("Empleado asignado correctamente a la solicitud");
	}
}
