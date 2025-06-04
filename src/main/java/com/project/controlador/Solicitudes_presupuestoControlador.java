package com.project.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.Solicitudes_presupuestoDTO;
import com.project.modelo.Solicitud_presupuesto;
import com.project.modelo.Usuario;
import com.project.repositorio.UsuarioRepositorio;
import com.project.servicio.Solicitud_presupuestoServicio;
import com.project.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/solicitudes")
public class Solicitudes_presupuestoControlador {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private Solicitud_presupuestoServicio solicitudPresu;

	@GetMapping
	public ResponseEntity<List<Solicitud_presupuesto>> listarSolicitudes() {
		List<Solicitud_presupuesto> solicitudes = solicitudPresu.listarTodas();
		return new ResponseEntity<>(solicitudes, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Solicitud_presupuesto> crear(@RequestBody Solicitudes_presupuestoDTO solicitudDTO, HttpServletRequest request) {
	    // 1. Obtener token del encabezado Authorization
	    String authHeader = request.getHeader("Authorization");
	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    String token = authHeader.substring(7); // Elimina "Bearer "

	    // 2. Extraer el correo desde el token
	    Claims claims = JwtUtil.extractClaims(token);
	    String correo = claims.getSubject(); // Este es el correo que se usó al generar el token

	    // 3. Buscar el usuario autenticado por su correo
	    Usuario usuario = usuarioRepositorio.findByCorreo(correo)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));

	    // 4. Crear la solicitud con ese usuario
	    Solicitud_presupuesto solicitud = new Solicitud_presupuesto();
	    solicitud.setDetalles(solicitudDTO.getDetalles());
	    solicitud.setEstado(solicitudDTO.getEstado());
	    solicitud.setDireccion(solicitudDTO.getDireccion());
	    solicitud.setUsuario(usuario);

	    return ResponseEntity.ok(solicitudPresu.guardar(solicitud));
	}

	}

