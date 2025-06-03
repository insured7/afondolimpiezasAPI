package com.project.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.modelo.Solicitud_presupuesto;
/**
 * Repositorio de la solicitud de presupuesto
 */
@Repository
public interface Solicitud_presupuestoRepositorio extends JpaRepository<Solicitud_presupuesto, Long> {

	List<Solicitud_presupuesto> findByUsuarioCorreo(String correo);
}
