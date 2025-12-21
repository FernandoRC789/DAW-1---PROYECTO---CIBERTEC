package pe.cibertec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.model.Usuario;
import pe.cibertec.service.UsuarioService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioService.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(usuarios);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el usuario con id: " + id);
        }
    }

    // CREAR USUARIO
    @PostMapping("/registrar")
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        try {
            if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El nombre de usuario es obligatorio");
            }
            if (usuarioService.existsByUsername(usuario.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El nombre de usuario ya existe");
            }
            Usuario u = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(u);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al registrar usuario: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    // ACTUALIZAR USUARIO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> existente = usuarioService.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el usuario con id: " + id);
        }
        Usuario u = existente.get();
        u.setUsername(usuario.getUsername() != null ? usuario.getUsername() : u.getUsername());
        u.setPassword(usuario.getPassword() != null ? usuario.getPassword() : u.getPassword());
        u.setRol(usuario.getRol() != null ? usuario.getRol() : u.getRol());

        try {
            Usuario actualizado = usuarioService.save(u);
            return ResponseEntity.ok(actualizado);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El nombre de usuario ya existe");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    // ELIMINAR USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> existente = usuarioService.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró el usuario con id: " + id);
        }
        try {
            usuarioService.delete(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar usuario: " + e.getMessage());
        }
    }
}