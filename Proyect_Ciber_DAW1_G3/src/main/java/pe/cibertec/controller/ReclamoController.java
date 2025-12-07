package pe.cibertec.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.model.EstadoReclamo;
import pe.cibertec.model.HistorialEstado;
import pe.cibertec.model.Reclamo;
import pe.cibertec.service.HistorialEstadoService;
import pe.cibertec.service.ReclamoService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reclamos")
public class ReclamoController {

    private final ReclamoService reclamoService;
    private final HistorialEstadoService historialService;

    public ReclamoController(ReclamoService reclamoService, HistorialEstadoService historialService) {
        this.reclamoService = reclamoService;
        this.historialService = historialService;
    }

    @GetMapping
    public ResponseEntity<List<Reclamo>> listar() {
        return ResponseEntity.ok(reclamoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reclamo> obtener(@PathVariable Long id) {
        Optional<Reclamo> opt = reclamoService.findById(id);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reclamo> crear(@RequestBody Reclamo reclamo) {
        Reclamo saved = reclamoService.create(reclamo);
        return ResponseEntity.created(URI.create("/api/reclamos/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Reclamo reclamo) {
        try {
            Reclamo updated = reclamoService.update(id, reclamo);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reclamoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cambiar estado del reclamo.
     * Recibe: nuevoEstado (String, debe coincidir con EstadoReclamo enum) y usuarioId (Long)
     * Ejemplo: PUT /api/reclamos/5/estado?nuevoEstado=EN_PROCESO&usuarioId=2
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            @RequestParam Long usuarioId
    ) {
        try {
            EstadoReclamo estadoEnum = EstadoReclamo.valueOf(nuevoEstado);
            Reclamo updated = reclamoService.cambiarEstado(id, estadoEnum, usuarioId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            // puede ser por valor enum inválido o reclamo/usuario no encontrado según implementación del service
            return ResponseEntity.badRequest().body("Estado inválido o recurso no encontrado: " + ex.getMessage());
        }
    }

    /**
     * Obtener historial de cambios de estado para un reclamo (usa tu HistorialEstadoService).
     * Ejemplo: GET /api/reclamos/5/historial
     */
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEstado>> historial(@PathVariable Long id) {
        List<HistorialEstado> list = historialService.findByReclamoId(id);
        return ResponseEntity.ok(list);
    }
}
