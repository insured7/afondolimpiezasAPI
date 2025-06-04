package com.project.servicio;

import com.project.modelo.Empleado;
import com.project.repositorio.EmpleadoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmpleadoServicio {

	@Autowired
    private EmpleadoRepositorio empleadoRepositorio;

    // Create
    public Empleado guardarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }
        return empleadoRepositorio.save(empleado);
    }

    // Read All
    public List<Empleado> listarTodosEmpleados() {
        List<Empleado> empleados = empleadoRepositorio.findAll();
        if (empleados.isEmpty()) {
            throw new EmpleadoNoEncontradoException("No se encontraron empleados");
        }
        return empleados;
    }
    
    public Empleado findbyCorreo(String correo) {
    	return empleadoRepositorio.findByCorreo(correo)
    			.orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado con correo: " + correo));
    }

    // Read by Id
    public Optional<Empleado> findById(Long id) {
        return empleadoRepositorio.findById(id);
    }


    // Delete
    @Transactional
    public void deletebyId(Long id) {
        if (!empleadoRepositorio.existsById(id)) {
            throw new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + id);
        }
        empleadoRepositorio.deleteById(id);
    }

    // Update Parcial (PATCH)
    @Transactional
    public Empleado modificarParcialEmpleado(Long id, Map<String, Object> updates) {
        Empleado empleadoExistente = empleadoRepositorio.findById(id)
            .orElseThrow(() -> new EmpleadoNoEncontradoException("Empleado no encontrado con ID: " + id));

        updates.forEach((campo, valor) -> {
            switch (campo) {
                case "nombre":
                    empleadoExistente.setNombre((String) valor);
                    break;
                case "apellidos":
                    empleadoExistente.setApellidos((String) valor);
                    break;
                case "correo":
                    empleadoExistente.setCorreo((String) valor);
                    break;
                case "direccion":
                    empleadoExistente.setDireccion((String) valor);
                    break;
                case "telefono":
                    empleadoExistente.setTelefono((String) valor);
                    break;
                case "admin":
                    empleadoExistente.setAdmin((Boolean) valor);
                    break;
                case "fechaNac":
                    empleadoExistente.setFecha_nac(LocalDate.parse((String) valor));
                    break;
                case "dni":
                    empleadoExistente.setDni((String) valor);
                    break;
                default:
                    throw new IllegalArgumentException("Campo no permitido: " + campo);
            }
        });

        return empleadoRepositorio.save(empleadoExistente);
    }
    
  //Excepcion personalizada
  	class EmpleadoNoEncontradoException extends RuntimeException {
  	    public EmpleadoNoEncontradoException(String mensaje) {
  	        super(mensaje);
  	        System.out.println(mensaje);
  	    }
  	}
}
  