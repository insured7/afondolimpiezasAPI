package com.project.servicio;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.modelo.Usuario;
import com.project.repositorio.UsuarioRepositorio;

import jakarta.transaction.Transactional;

/**
 * Servicio que se encarga de la logica de los usuarios
 */

@Service
public class UsuarioServicio {

	@Autowired
	private UsuarioRepositorio usuariorepositorio;

	@Autowired
	private EmailServicio emailServicio;

	// Guarda usuario
	public Usuario guardarUsuario(Usuario usuario) {
		if (usuario == null) {
			throw new IllegalArgumentException("El usuario no puede ser nulo");
		}
		return usuariorepositorio.save(usuario);
	}

	// Lista todos los usuarios
	public List<Usuario> listarTodosUsuarios() {
		List<Usuario> usuarios = usuariorepositorio.findAll();
		if (usuarios.isEmpty()) {
			throw new RuntimeException("No se encontraron usuarios");
		}
		return usuarios;
	}

	// Encuentra al usuario por el Id
	public Usuario findById(Long id) {
		return usuariorepositorio.findById(id)
				.orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));
	}

	// Encuentra al usuario por el correo
	public Usuario findByCorreo(String correo) {
		return usuariorepositorio.findByCorreo(correo).orElse(null);
	}

	// Elimina al usuario por el id
	public ResponseEntity<String> deletebyId(Long id) {

		if (usuariorepositorio.existsById(id)) {
			System.out.println("Usuario eliminado correctamente.");
			usuariorepositorio.deleteById(id);
			return ResponseEntity.ok("Usuario eliminado correctamente.");
		} else {
			System.out.println("Usuario no encontrado.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
		}
	}

	// Modifica parcialmente al usuario
	@Transactional
	public Usuario modificaParcialUsuario(Long id, Map<String, Object> updates) {
		if (updates == null || updates.isEmpty()) {
			throw new IllegalArgumentException("Los campos a actualizar no pueden estar vacíos");
		}

		Usuario usuarioExistente = usuariorepositorio.findById(id)
				.orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id));

		updates.forEach((campo, valor) -> {
			try {
				switch (campo) {
				case "nombre":
					if (valor != null) {
						usuarioExistente.setNombre(valor.toString());
					}
					break;
				case "apellidos":
					if (valor != null) {
						usuarioExistente.setApellidos(valor.toString());
					}
					break;
				case "correo":
					if (valor != null) {
						String correo = valor.toString();
						if (!correo.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
							throw new IllegalArgumentException("Formato de correo inválido");
						}
						usuarioExistente.setCorreo(correo);
					}
					break;
				case "direccion":
					if (valor != null) {
						usuarioExistente.setDireccion(valor.toString());
					}
					break;
				case "telefono":
					if (valor != null) {
						usuarioExistente.setTelefono(valor.toString());
					}
					break;
				default:
					throw new IllegalArgumentException("Campo no permitido para actualización: " + campo);
				}
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + campo);
			}
		});

		return usuariorepositorio.save(usuarioExistente);
	}

	public void enviarTokenActivacion(String correo) {
		Usuario usuario = usuariorepositorio.findByCorreo(correo)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		// Generar token y asignarlo
		String token = UUID.randomUUID().toString();
		usuario.setTokenactivacion(token);

		// Guardar usuario con token en base de datos
		usuario = usuariorepositorio.save(usuario); // Guarda y actualiza el objeto usuario

		System.out.println("Token guardado en BD: " + usuario.getTokenactivacion());

		// Codificar el token para la URL
		//String tokenCodificado = URLEncoder.encode(token, StandardCharsets.UTF_8);

		// Construir URL para activación
		String urlActivacion = "http://localhost:3000/frontAFondoLimpiezas/activarcuenta.jsp?token=" + token;


		// Preparar email
		String asunto = "Activa tu cuenta - A Fondo Limpiezas";
		String mensaje = "Hola " + usuario.getNombre() + ",\n\n"
				+ "Para activar tu cuenta, haz clic en el siguiente enlace:\n" + urlActivacion + "\n\n"
				+ "Este enlace expirará en 24 horas.\n\n" + "Gracias,\nEl equipo de A Fondo Limpiezas.";

		// Enviar email
		try {
			emailServicio.enviarEmail(correo, asunto, mensaje);
		} catch (IOException e) {
			throw new RuntimeException("Error al enviar correo de activación", e);
		}
	}

	@Transactional
	public void activarUsuarioPorToken(String token) {

		// Verificar token no nulo
		if (token == null || token.trim().isEmpty()) {
			throw new IllegalArgumentException("Token no proporcionado");
		}

		// Buscar usuario con token
		Usuario usuario = usuariorepositorio.findByTokenactivacion(token)
				.orElseThrow(() -> new IllegalArgumentException("Token no válido"));

		// Verificar si ya está activo
		if (usuario.getActivo() == true) {
			throw new IllegalArgumentException("La cuenta ya está activada");
		}

		// 5. Activar cuenta
		usuario.setActivo(true);
		usuario.setTokenactivacion(null);
		usuario.setExpiraciontoken(Instant.now().plus(24, ChronoUnit.HOURS));
		usuariorepositorio.save(usuario);
	}

// Excepcion personalizada
	class UsuarioNoEncontradoException extends RuntimeException {
		public UsuarioNoEncontradoException(String mensaje) {
			super(mensaje);
			System.out.println(mensaje);
		}
	}

}
