package pe.cibertec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.model.Ciudadano;
import pe.cibertec.service.CiudadanoService;

import java.util.List;

@RestController
@RequestMapping("/api/ciudadanos")
public class CiudadanoController {

    private final CiudadanoService ciudadanoService;

    public CiudadanoController(CiudadanoService ciudadanoService) {
        this.ciudadanoService = ciudadanoService;
    }

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Ciudadano>> listar() {
        List<Ciudadano> ciudadanos = ciudadanoService.listar();
        if (ciudadanos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(ciudadanos);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            Ciudadano c = ciudadanoService.buscarPorId(id);
            return ResponseEntity.ok(c);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // REGISTRAR CIUDADANO
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Ciudadano ciudadano) {
        // Validaciones básicas
        if (ciudadano.getNombres() == null || ciudadano.getNombres().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre es obligatorio");
        }
        if (ciudadano.getApellidos() == null || ciudadano.getApellidos().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Los apellidos son obligatorios");
        }
        if (ciudadano.getDni() == null || ciudadano.getDni().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El DNI es obligatorio");
        }
        if (ciudadano.getCorreo() != null && !ciudadano.getCorreo().contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo no es válido");
        }

        try {
            Ciudadano c = ciudadanoService.registrarCiudadano(ciudadano);
            return ResponseEntity.status(HttpStatus.CREATED).body(c);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    // ACTUALIZAR CIUDADANO
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Ciudadano ciudadano) {
        // Validaciones opcionales
        if (ciudadano.getDni() != null && ciudadano.getDni().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El DNI no puede estar vacío");
        }
        if (ciudadano.getCorreo() != null && !ciudadano.getCorreo().contains("@")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo no es válido");
        }

        try {
            Ciudadano c = ciudadanoService.actualizarCiudadano(id, ciudadano);
            return ResponseEntity.ok(c);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }

    // ELIMINAR CIUDADANO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            ciudadanoService.eliminar(id);
            return ResponseEntity.ok("Ciudadano eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar ciudadano: " + e.getMessage());
        }
    }
}
