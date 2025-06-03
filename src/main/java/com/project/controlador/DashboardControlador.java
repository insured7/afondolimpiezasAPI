package com.project.controlador;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.EmpleadoDTO;
import com.project.dto.Solicitudes_presupuestoDTO;
import com.project.dto.UsuarioDTO;
import com.project.modelo.Empleado;
import com.project.modelo.Usuario;
import com.project.servicio.EmpleadoServicio;
import com.project.servicio.Solicitud_presupuestoServicio;
import com.project.servicio.UsuarioServicio;

import io.jsonwebtoken.io.IOException;

/**
 * Se encarga de mostrar los perfiles tanto para usuario como para empleado El
 * admin tiene su propio controller
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardControlador {

	@Autowired
    EmpleadoServicio empleadoServicio;
	
	@Autowired
	UsuarioServicio usuarioServicio;
	
	@Autowired
	Solicitud_presupuestoServicio solicitudServicio;

	@GetMapping("/empleado")
    public ResponseEntity<EmpleadoDTO> perfilEmpleado(Authentication authentication) {
		
        String correo = (String) authentication.getPrincipal();

        Empleado empleado = empleadoServicio.findbyCorreo(correo);

        if (empleado == null) {
            return ResponseEntity.notFound().build();
        }

        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setNombre(empleado.getNombre());
        dto.setApellidos(empleado.getApellidos());
        dto.setCorreo(empleado.getCorreo());
        dto.setTelefono(empleado.getTelefono());
        dto.setDireccion(empleado.getDireccion());

        return ResponseEntity.ok(dto);
    }
	
	@GetMapping("/usuario")
	public ResponseEntity<UsuarioDTO> perfilAutenticado(Authentication authentication) {
	    String correo = (String) authentication.getPrincipal();

	    Usuario usuario = usuarioServicio.findByCorreo(correo);

	    UsuarioDTO dto = new UsuarioDTO();
	    dto.setNombre(usuario.getNombre());
	    dto.setCorreo(usuario.getCorreo());
	    
	    if (usuario.getFotoperfil() != null) {
	        // Construimos la URL pública para la imagen
	    	String urlFoto = "http://localhost:8080/uploads/" + usuario.getFotoperfil();
	    	dto.setFotoPerfilUrl(urlFoto);
	    } else {
	        dto.setFotoPerfilUrl(null); // o una URL de imagen por defecto si quieres
	    }
	    
	    return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/usuario/solicitudes")
	public ResponseEntity<List<Solicitudes_presupuestoDTO>> solicitudesDelUsuario(Authentication authentication) {
	    String correo = (String) authentication.getPrincipal();

	    List<Solicitudes_presupuestoDTO> solicitudes = solicitudServicio.obtenerSolicitudesPorCorreo(correo);
	    return ResponseEntity.ok(solicitudes);
	}
	
	@PostMapping("/usuario/foto")
	public ResponseEntity<String> subirFotoPerfil(Authentication authentication, @RequestParam("file") MultipartFile archivo) {
	    if (archivo.isEmpty()) {
	        return ResponseEntity.badRequest().body("Archivo vacío");
	    }

	    try {
	        Path carpetaUploads = Paths.get("uploads");
	        if (!Files.exists(carpetaUploads)) {
	            try {
					Files.createDirectories(carpetaUploads);
				} catch (java.io.IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }

	        String correo = (String) authentication.getPrincipal();
	        Usuario usuario = usuarioServicio.findByCorreo(correo);

	        String nombreArchivo = "usuario_" + usuario.getId_usuario() + ".jpg";
	        Path rutaArchivo = carpetaUploads.resolve(nombreArchivo);

	        try {
				Files.write(rutaArchivo, archivo.getBytes());
			} catch (java.io.IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        usuario.setFotoperfil(nombreArchivo);
	        usuarioServicio.guardarUsuario(usuario);

	        return ResponseEntity.ok("Foto subida correctamente");
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Error al guardar archivo");
	    }
	}





}
