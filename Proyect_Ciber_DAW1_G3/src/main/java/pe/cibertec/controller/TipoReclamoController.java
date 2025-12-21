package pe.cibertec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.model.TipoReclamo;
import pe.cibertec.service.TipoReclamoService;

import java.util.List;

@RestController
@RequestMapping("/api/tipos")
public class TipoReclamoController {

    private final TipoReclamoService service;

    public TipoReclamoController(TipoReclamoService service) {
        this.service = service;
    }

    // LISTAR TODOS
    @GetMapping("/listar")
    public List<TipoReclamo> listar() {
        return service.findAll();
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        TipoReclamo t = service.findById(id);
        if (t == null) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Tipo de reclamo no encontrado con id: " + id);
        return ResponseEntity.ok(t);
    }

    // CREAR
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody TipoReclamo t) {
        if (t.getNombre() == null || t.getNombre().isBlank()) {
            return ResponseEntity.badRequest().body("El nombre del tipo de reclamo es obligatorio");
        }
        if (t.getNombre().length() > 100) {
            return ResponseEntity.badRequest().body("El nombre no puede superar 100 caracteres");
        }

        // Validación duplicado
        List<TipoReclamo> existentes = service.buscarPorNombre(t.getNombre());
        if (!existentes.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Ya existe un tipo de reclamo con el nombre: " + t.getNombre());
        }

        TipoReclamo creado = service.save(t);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody TipoReclamo t) {
        TipoReclamo existente = service.findById(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tipo de reclamo no encontrado con id: " + id);
        }

        if (t.getNombre() != null) {
            if (t.getNombre().isBlank()) {
                return ResponseEntity.badRequest().body("El nombre no puede estar vacío");
            }
            if (t.getNombre().length() > 100) {
                return ResponseEntity.badRequest().body("El nombre no puede superar 100 caracteres");
            }
            // Validación duplicado para actualizar
            List<TipoReclamo> duplicados = service.buscarPorNombre(t.getNombre());
            boolean existeOtro = duplicados.stream().anyMatch(d -> !d.getId().equals(id));
            if (existeOtro) {
                return ResponseEntity.badRequest()
                        .body("Ya existe otro tipo de reclamo con el nombre: " + t.getNombre());
            }
            existente.setNombre(t.getNombre());
        }

        if (t.getDescripcion() != null) {
            if (t.getDescripcion().isBlank()) {
                return ResponseEntity.badRequest().body("La descripción no puede estar vacía");
            }
            if (t.getDescripcion().length() > 500) {
                return ResponseEntity.badRequest().body("La descripción no puede superar 500 caracteres");
            }
            existente.setDescripcion(t.getDescripcion());
        }

        TipoReclamo actualizado = service.save(existente);
        return ResponseEntity.ok(actualizado);
    }



    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        TipoReclamo existente = service.findById(id);
        if (existente == null) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Tipo de reclamo no encontrado con id: " + id);

        service.delete(id);
        return ResponseEntity.ok("Tipo de reclamo eliminado correctamente");
    }

    // BUSCAR POR NOMBRE
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String nombre) {
        List<TipoReclamo> resultados = service.buscarPorNombre(nombre);
        if (resultados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontraron tipos de reclamo con el nombre: " + nombre);
        }
        return ResponseEntity.ok(resultados);
    }
}