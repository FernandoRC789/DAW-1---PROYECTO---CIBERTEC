package pe.cibertec.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.model.EstadoReclamo;
import pe.cibertec.model.HistorialEstado;
import pe.cibertec.model.Reclamo;
import pe.cibertec.service.ReclamoService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reclamos")
public class ReclamoController {

    private final ReclamoService reclamoService;

    public ReclamoController(ReclamoService reclamoService) {
        this.reclamoService = reclamoService;
    }

    // LISTAR
    @GetMapping("/listar")
    public List<Reclamo> listar() {
        return reclamoService.findAll();
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Reclamo> obtener(@PathVariable Long id) {
        Optional<Reclamo> reclamo = reclamoService.findById(id);
        return reclamo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR
    @PostMapping("/registrar")
    public ResponseEntity<?> crear(@RequestBody Reclamo reclamo) {
        // === VALIDACIONES EXISTENTES ===
        if (reclamo.getAsunto() == null || reclamo.getAsunto().isBlank())
            return ResponseEntity.badRequest().body("El asunto es obligatorio");
        if (reclamo.getAsunto().length() > 100)
            return ResponseEntity.badRequest().body("El asunto no puede superar 100 caracteres");
        if (reclamo.getDescripcion() == null || reclamo.getDescripcion().isBlank())
            return ResponseEntity.badRequest().body("La descripción es obligatoria");
        if (reclamo.getDescripcion().length() > 500)
            return ResponseEntity.badRequest().body("La descripción no puede superar 500 caracteres");
        if (reclamo.getDireccionAfectada() == null || reclamo.getDireccionAfectada().isBlank())
            return ResponseEntity.badRequest().body("La direccion afectada es obligatorio");
        if (reclamo.getDireccionAfectada().length() > 100)
            return ResponseEntity.badRequest().body("La direccion afectada no puede superar 100 caracteres");
        if (reclamo.getCiudadano() == null || reclamo.getCiudadano().getId() == null)
            return ResponseEntity.badRequest().body("El ciudadano es obligatorio");
        if (reclamo.getTipoReclamo() == null || reclamo.getTipoReclamo().getId() == null)
            return ResponseEntity.badRequest().body("El tipo de reclamo es obligatorio");
        if (reclamo.getPrioridad() == null || reclamo.getPrioridad().getId() == null)
            return ResponseEntity.badRequest().body("La prioridad es obligatoria");
        if (reclamo.getFotoUrl() != null && !reclamo.getFotoUrl().isBlank()) {
            try {
                new URL(reclamo.getFotoUrl());
            } catch (MalformedURLException e) {
                return ResponseEntity.badRequest().body("La URL de la foto no es válida");
            }
        }
        if (reclamo.getDireccionAfectada() != null && reclamo.getDireccionAfectada().length() > 200)
            return ResponseEntity.badRequest().body("La dirección afectada no puede superar 200 caracteres");

        // === VALIDACIÓN DE DUPLICADO (REGLA DE NEGOCIO) ===
        if (reclamoService.existeReclamo(
                reclamo.getCiudadano().getId(),
                reclamo.getTipoReclamo().getId(),
                reclamo.getPrioridad().getId(),
                null)) { // null porque es nuevo
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un reclamo para este ciudadano con el mismo tipo y prioridad");
        }

        Reclamo creado = reclamoService.save(reclamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ACTUALIZAR
    @PutMapping("/id/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Reclamo datos) {
        return reclamoService.findById(id).map(r -> {
            // === VALIDACIONES EXISTENTES ===
            if (datos.getAsunto() != null) {
                if (datos.getAsunto().isBlank())
                    return ResponseEntity.badRequest().body("El asunto no puede estar vacío");
                if (datos.getAsunto().length() > 100)
                    return ResponseEntity.badRequest().body("El asunto no puede superar 100 caracteres");
                r.setAsunto(datos.getAsunto());
            }
            if (datos.getDescripcion() != null) {
                if (datos.getDescripcion().isBlank())
                    return ResponseEntity.badRequest().body("La descripción no puede estar vacía");
                if (datos.getDescripcion().length() > 500)
                    return ResponseEntity.badRequest().body("La descripción no puede superar 500 caracteres");
                r.setDescripcion(datos.getDescripcion());
            }
            if (datos.getDireccionAfectada() != null) {
                if (datos.getDireccionAfectada().length() > 200)
                    return ResponseEntity.badRequest().body("La dirección afectada no puede superar 200 caracteres");
                r.setDireccionAfectada(datos.getDireccionAfectada());
            }
            if (datos.getFotoUrl() != null) {
                try {
                    new URL(datos.getFotoUrl());
                    r.setFotoUrl(datos.getFotoUrl());
                } catch (MalformedURLException e) {
                    return ResponseEntity.badRequest().body("La URL de la foto no es válida");
                }
            }
            if (datos.getCiudadano() != null && datos.getCiudadano().getId() != null)
                r.setCiudadano(datos.getCiudadano());
            if (datos.getTipoReclamo() != null && datos.getTipoReclamo().getId() != null)
                r.setTipoReclamo(datos.getTipoReclamo());
            if (datos.getPrioridad() != null && datos.getPrioridad().getId() != null)
                r.setPrioridad(datos.getPrioridad());

            // === VALIDACIÓN DE DUPLICADO ===
            if (r.getCiudadano() != null && r.getTipoReclamo() != null && r.getPrioridad() != null) {
                if (reclamoService.existeReclamo(
                        r.getCiudadano().getId(),
                        r.getTipoReclamo().getId(),
                        r.getPrioridad().getId(),
                        id)) { // Excluye este reclamo
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Ya existe un reclamo para este ciudadano con el mismo tipo y prioridad");
                }
            }

            return ResponseEntity.ok(reclamoService.save(r));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reclamo no encontrado con id: " + id));
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Reclamo> r = reclamoService.findById(id);
        if (r.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reclamo no encontrado con id: " + id);
        }

        reclamoService.delete(id);
        return ResponseEntity.ok("Reclamo eliminado correctamente");
    }

    // CAMBIAR ESTADO
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,
                                           @RequestParam String nuevoEstado,
                                           @RequestParam Long usuarioId) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest().body("El nuevo estado es obligatorio");
        }
        try {
            EstadoReclamo estado = EstadoReclamo.valueOf(nuevoEstado);
            Reclamo actualizado = reclamoService.cambiarEstado(id, estado, usuarioId);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al cambiar estado: " + e.getMessage());
        }
    }

    // HISTORIAL
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEstado>> historial(@PathVariable Long id) {
        List<HistorialEstado> historial = reclamoService.getHistorialByReclamo(id);
        if (historial.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(historial);
    }
}