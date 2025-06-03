package com.project.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.dto.RegistroEmpleadoDTO;
import com.project.dto.RegistroUsuarioDTO;
import com.project.modelo.Empleado;
import com.project.modelo.Usuario;
import com.project.repositorio.EmpleadoRepositorio;
import com.project.repositorio.UsuarioRepositorio;
import com.project.util.JwtUtil;

/**
 * Servicio de autenticación y registro Maneja login, registro y validaciones de
 * seguridad
 */
@Service
public class AuthServicio {

	@Autowired
	UsuarioRepositorio usuRep;

	@Autowired
	EmpleadoRepositorio empRep;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UsuarioServicio usuarioServicio;

	// ========== MÉTODOS DE LOGIN ==========

	public String loginUsuario(String correo, String contrasenia) {
		Usuario u = usuRep.findByCorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		if (!passwordEncoder.matches(contrasenia, u.getContrasenia())) {
			throw new BadCredentialsException("Contraseña incorrecta");
		}

		return JwtUtil.generateToken(correo, "USUARIO");
	}

	public String loginEmpleado(String correo, String contrasenia) {
		Empleado e = empRep.findByCorreo(correo)
				.orElseThrow(() -> new UsernameNotFoundException("Empleado no encontrado"));

		if (!passwordEncoder.matches(contrasenia, e.getContrasenia())) {
			throw new BadCredentialsException("Contraseña incorrecta");
		}

		String rol = e.isAdmin() ? "ADMIN" : "EMPLEADO";
		return JwtUtil.generateToken(correo, rol);
	}

	// ========== MÉTODOS DE REGISTRO ==========

	public Usuario registrarUsuario(RegistroUsuarioDTO registroDTO) {
		// Validaciones
		validarDatosUsuario(registroDTO);

		// Verificar si el correo ya existe
		if (usuRep.findByCorreo(registroDTO.getCorreo()).isPresent()) {
			throw new IllegalArgumentException("El correo ya está registrado");
		}

		// Crear nuevo usuario
		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setNombre(registroDTO.getNombre());
		nuevoUsuario.setApellidos(registroDTO.getApellidos());
		nuevoUsuario.setCorreo(registroDTO.getCorreo());
		nuevoUsuario.setDireccion(registroDTO.getDireccion());
		nuevoUsuario.setTelefono(registroDTO.getTelefono());

		// Encriptar la contraseña
		nuevoUsuario.setContrasenia(passwordEncoder.encode(registroDTO.getContrasenia()));

		Usuario usuarioGuardado = usuRep.save(nuevoUsuario);

		// Enviar email de activación inmediatamente después de guardar
		usuarioServicio.enviarTokenActivacion(usuarioGuardado.getCorreo());

		return usuarioGuardado;
	}

	public Empleado registrarEmpleado(RegistroEmpleadoDTO registroDTO) {
		// Validaciones
		validarDatosEmpleado(registroDTO);

		// Verificar si el correo ya existe
		if (empRep.findByCorreo(registroDTO.getCorreo()).isPresent()) {
			throw new IllegalArgumentException("El correo ya está registrado");
		}

		// Crear nuevo empleado
		Empleado nuevoEmpleado = new Empleado();
		nuevoEmpleado.setNombre(registroDTO.getNombre());
		nuevoEmpleado.setApellidos(registroDTO.getApellidos());
		nuevoEmpleado.setCorreo(registroDTO.getCorreo());
		nuevoEmpleado.setDireccion(registroDTO.getDireccion());
		nuevoEmpleado.setTelefono(registroDTO.getTelefono());
		nuevoEmpleado.setAdmin(registroDTO.isAdmin());
		nuevoEmpleado.setFecha_nac(registroDTO.getFechaNac());
		nuevoEmpleado.setDni(registroDTO.getDni());

		// Encriptar la contraseña
		nuevoEmpleado.setContrasenia(passwordEncoder.encode(registroDTO.getContrasenia()));

		return empRep.save(nuevoEmpleado);
	}

	// ========== MÉTODOS DE VALIDACIÓN ==========

	private void validarDatosUsuario(RegistroUsuarioDTO dto) {
		if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
			throw new IllegalArgumentException("El nombre es obligatorio");
		}
		if (dto.getApellidos() == null || dto.getApellidos().trim().isEmpty()) {
			throw new IllegalArgumentException("Los apellidos son obligatorios");
		}
		if (dto.getCorreo() == null || !dto.getCorreo().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			throw new IllegalArgumentException("El correo no tiene un formato válido");
		}
		if (dto.getContrasenia() == null || dto.getContrasenia().length() < 6) {
			throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
		}
		if (dto.getTelefono() == null || !dto.getTelefono().matches("\\d{9}")) {
			throw new IllegalArgumentException("El teléfono debe tener 9 dígitos");
		}
	}

	private void validarDatosEmpleado(RegistroEmpleadoDTO dto) {
		validarDatosUsuario(new RegistroUsuarioDTO(dto.getNombre(), dto.getApellidos(), dto.getCorreo(),
				dto.getDireccion(), dto.getTelefono(), dto.getContrasenia()));

		if (dto.getDni() == null || !dto.getDni().matches("\\d{8}[A-Za-z]")) {
			throw new IllegalArgumentException("El DNI debe tener el formato correcto (8 dígitos + letra)");
		}
		if (dto.getFechaNac() == null) {
			throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
		}
	}

	// ========== OTROS MÉTODOS ==========

	public Empleado obtenerEmpleadoPorCorreo(String correo) throws Exception {
		return empRep.findByCorreo(correo)
				.orElseThrow(() -> new Exception("Empleado no encontrado con correo: " + correo));
	}

}