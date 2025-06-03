package com.project.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.modelo.Usuario;

/**
 * Repositorio de usuario
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByTokenactivacion(String tokenactivacion);
    boolean existsByTokenactivacion(String tokenactivacion);
}


