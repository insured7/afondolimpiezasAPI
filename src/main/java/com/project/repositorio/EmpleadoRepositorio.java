package com.project.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.modelo.Empleado;
import com.project.modelo.Usuario;

/**
 * Repositorio de empleado
 */
@Repository
public interface EmpleadoRepositorio extends JpaRepository<Empleado, Long> {
	
	Optional<Empleado> findByCorreo(String email);

}
