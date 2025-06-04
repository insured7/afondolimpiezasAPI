package com.project.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.modelo.Asignacion_servicio;
import com.project.modelo.Empleado;
import com.project.modelo.Solicitud_presupuesto;
import com.project.repositorio.AsignacionServicioRepositorio;
import com.project.repositorio.EmpleadoRepositorio;
import com.project.repositorio.Solicitud_presupuestoRepositorio;

@Service
public class AsignacionServicioServicio {

	@Autowired
	private AsignacionServicioRepositorio asignacionRepo;

	@Autowired
	private EmpleadoRepositorio empleadoRepo;
	
	@Autowired
	private Solicitud_presupuestoServicio solicitudServicio;

	// Asigna un empleado a un servicio con un rol determinado
	public Asignacion_servicio asignarEmpleadoASolicitud(Asignacion_servicio asignacion) {
	    Empleado empleado = empleadoRepo.findById(asignacion.getEmpleado().getId_empleado())
	        .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

	    Solicitud_presupuesto solicitud = solicitudServicio.buscarPorId(asignacion.getSolicitud().getId_solicitudes_presupuesto())
	        .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

	    asignacion.setEmpleado(empleado);
	    asignacion.setSolicitud(solicitud);

	    return asignacionRepo.save(asignacion);
	}


	// Devuelve todas las asignaciones
	public List<Asignacion_servicio> obtenerTodasAsignaciones() {
		return asignacionRepo.findAll();
	}

	// Obtiene las asignaciones de un empleado específico
	public List<Asignacion_servicio> obtenerAsignacionesPorEmpleado(Long empleadoId) {
		Empleado empleado = empleadoRepo.findById(empleadoId)
				.orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
		return asignacionRepo.findByEmpleado(empleado);
	}

	// Elimina una asignación por su ID
	public void eliminarAsignacion(Long idAsignacion) {
		if (!asignacionRepo.existsById(idAsignacion)) {
			throw new RuntimeException("Asignación no encontrada");
		}
		asignacionRepo.deleteById(idAsignacion);
	}

	// Actualiza el rol de una asignación existente
	public Asignacion_servicio actualizarRolAsignacion(Long idAsignacion, String nuevoRol) {
		Asignacion_servicio asignacion = asignacionRepo.findById(idAsignacion)
				.orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
		asignacion.setRol(nuevoRol);
		return asignacionRepo.save(asignacion);
	}
	
	public List<Asignacion_servicio> findBySolicitudId(Long solicitudId) {
	    // Primero buscas la solicitud (puedes usar solicitudServicio)
	    Solicitud_presupuesto solicitud = solicitudServicio.buscarPorId(solicitudId)
	            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
	    // Luego buscas las asignaciones por solicitud
	    return asignacionRepo.findBySolicitud(solicitud);
	}
	
	public void eliminarTodas(List<Asignacion_servicio> asignaciones) {
	    asignacionRepo.deleteAll(asignaciones);
	}



}
