package com.project.controlador;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.modelo.Usuario;
import com.project.servicio.UsuarioServicio;

/**
 * Controlador Usuarios
 */

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

	@Autowired
	private UsuarioServicio usuarioservicio;

	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
		Usuario nuevoUsuario = usuarioservicio.guardarUsuario(usuario);
		return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		List<Usuario> usuarios = usuarioservicio.listarTodosUsuarios();
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public Usuario getById(@PathVariable Long id) {
		return usuarioservicio.findById(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletebyId(@PathVariable Long id) {
		return usuarioservicio.deletebyId(id);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Usuario> UpdateparcialUsuario(@PathVariable Long id,
			@RequestBody Map<String, Object> updates) {

		Usuario usuario = usuarioservicio.modificaParcialUsuario(id, updates);
		return ResponseEntity.ok(usuario);
	}

	@PostMapping("/enviar-token")
	public ResponseEntity<String> enviarToken(@RequestParam String correo) {
		usuarioservicio.enviarTokenActivacion(correo);
		return ResponseEntity.ok("Token de activación enviado al correo.");
	}
}
