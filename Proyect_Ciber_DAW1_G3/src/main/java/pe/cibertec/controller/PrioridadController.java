package pe.cibertec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.model.Prioridad;
import pe.cibertec.service.PrioridadService;

import java.util.List;

@RestController
@RequestMapping("/api/prioridades")
public class PrioridadController {

    private final PrioridadService service;

    public PrioridadController(PrioridadService service) {
        this.service = service;
    }

    // LISTAR TODOS
    @GetMapping("/listar")
    public ResponseEntity<List<Prioridad>> listar() {
        List<Prioridad> prioridades = service.findAll();
        if (prioridades.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(prioridades);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Prioridad p = service.findById(id);
        if (p == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Prioridad no encontrada con id: " + id);
        }
        return ResponseEntity.ok(p);
    }

    // REGISTRAR
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Prioridad p) {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El nombre de la prioridad es obligatorio");
        }
        if (p.getNombre().length() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El nombre no puede superar 100 caracteres");
        }

        // Validación duplicado
        List<Prioridad> existentes = service.buscarPorNombre(p.getNombre());
        if (!existentes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ya existe una prioridad con el nombre: " + p.getNombre());
        }

        Prioridad creado = service.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Prioridad p) {
        Prioridad existente = service.findById(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Prioridad no encontrada con id: " + id);
        }

        if (p.getNombre() != null) {
            if (p.getNombre().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El nombre de la prioridad no puede estar vacío");
            }
            if (p.getNombre().length() > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El nombre no puede superar 100 caracteres");
            }

            // Validación duplicado para actualizar
            List<Prioridad> duplicados = service.buscarPorNombre(p.getNombre());
            boolean existeOtro = duplicados.stream().anyMatch(d -> !d.getId().equals(id));
            if (existeOtro) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Ya existe otra prioridad con el nombre: " + p.getNombre());
            }

            existente.setNombre(p.getNombre());
        }

        if (p.getDescripcion() != null) {
            if (p.getDescripcion().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La descripción no puede estar vacía");
            }
            if (p.getDescripcion().length() > 500) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La descripción no puede superar 500 caracteres");
            }
            existente.setDescripcion(p.getDescripcion());
        }

        Prioridad actualizado = service.save(existente);
        return ResponseEntity.ok(actualizado);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Prioridad existente = service.findById(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Prioridad no encontrada con id: " + id);
        }

        try {
            service.delete(id);
            return ResponseEntity.ok("Prioridad eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar prioridad: " + e.getMessage());
        }
    }

    // BUSCAR POR NOMBRE
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El parámetro 'nombre' es obligatorio para la búsqueda");
        }

        List<Prioridad> resultados = service.buscarPorNombre(nombre);
        if (resultados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(resultados);
    }
}