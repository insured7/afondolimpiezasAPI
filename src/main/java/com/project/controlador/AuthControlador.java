package com.project.controlador;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AuthRequest;
import com.project.dto.AuthResponse;
import com.project.dto.AuthResponseAdmin;
import com.project.dto.RegistroEmpleadoDTO;
import com.project.dto.RegistroUsuarioDTO;
import com.project.dto.SesionRequest;
import com.project.modelo.Empleado;
import com.project.modelo.Usuario;
import com.project.repositorio.UsuarioRepositorio;
import com.project.servicio.AuthServicio;
import com.project.servicio.UsuarioServicio;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Controlador de autenticación y registro
 */
@RestController
@RequestMapping("/auth")
public class AuthControlador {

	@Autowired
	AuthServicio authServicio;

	@Autowired
	UsuarioServicio usuarioServicio;
	
	@Autowired
	UsuarioRepositorio usuarioRepositorio;
	

	// ========== LOGIN ENDPOINTS ==========

	
	
	@PostMapping("/login-usuario")
	public ResponseEntity<?> loginUsuario(@RequestBody AuthRequest request) {
		try {
			String token = authServicio.loginUsuario(request.getCorreo(), request.getContrasenia());
			return ResponseEntity.ok(new AuthResponse(token));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: " + e.getMessage());
		}
	}

	@PostMapping("/login-empleado")
	public ResponseEntity<?> loginEmpleado(@RequestBody AuthRequest request) {
		try {
			// Obtengo token
			String token = authServicio.loginEmpleado(request.getCorreo(), request.getContrasenia());

			// Obtengo el empleado para saber si es admin
			Empleado empleado = authServicio.obtenerEmpleadoPorCorreo(request.getCorreo());

			boolean esAdmin = empleado.isAdmin(); // Método o campo booleano que indica si es admin

			System.out.println("DEBUG: esAdmin=" + esAdmin); // <<--- log
			return ResponseEntity.ok(new AuthResponseAdmin(token, esAdmin));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: " + e.getMessage());
		}
	}

	// ========== REGISTRO ENDPOINTS ==========

	@PostMapping("/registro-usuario")
	public ResponseEntity<?> registroUsuario(@RequestBody RegistroUsuarioDTO registroDTO) {
		try {
			Usuario nuevoUsuario = authServicio.registrarUsuario(registroDTO);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(Map.of("mensaje", "Usuario registrado exitosamente con ID: " + nuevoUsuario.getId_usuario(),
							"info", "Se ha enviado un email para activar la cuenta"));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(Map.of("mensaje", "Error de validación: " + e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("mensaje", "Error en el registro: " + e.getMessage()));
		}
	}

	@PostMapping("/registro-empleado")
	public ResponseEntity<?> registroEmpleado(@RequestBody RegistroEmpleadoDTO registroDTO) {
		try {
			Empleado nuevoEmpleado = authServicio.registrarEmpleado(registroDTO);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body("Empleado registrado exitosamente con ID: " + nuevoEmpleado.getId_empleado());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body("Error de validación: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error en el registro: " + e.getMessage());
		}
	}

	@GetMapping("/activar-cuenta")
	public ResponseEntity<Map<String, Object>> activarCuenta(@RequestParam String token) {
		
	    System.out.println("Token recibido en backend: " + token);
	    try {
	        // Verificar existencia del token
	        if (!usuarioRepositorio.existsByTokenactivacion(token)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
	                "mensaje", "Token no válido",
	                "tokenExpirado", false
	            ));
	        }
	        
	        
	        try {
	            usuarioServicio.activarUsuarioPorToken(token);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
	                "mensaje", e.getMessage(),
	                "tokenExpirado", e.getMessage().toLowerCase().contains("expirado")
	            ));
	        }
	        return ResponseEntity.ok(Map.of(
	            "mensaje", "Cuenta activada correctamente",
	            "redirect", "http://localhost:3000/frontAFondoLimpiezas/login.jsp"
	        ));
	    } catch (Exception e) {
	        
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
	            "mensaje", "Error al activar cuenta: " + e.getMessage(),
	            "tokenExpirado", e.getMessage().toLowerCase().contains("expirado")
	        ));
	    }
	}

}