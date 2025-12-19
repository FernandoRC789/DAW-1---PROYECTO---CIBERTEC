package pe.cibertec.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.model.EstadoReclamo;
import pe.cibertec.model.HistorialEstado;
import pe.cibertec.model.Reclamo;
import pe.cibertec.service.ReclamoService;
import java.util.List;

@RestController
@RequestMapping("/api/reclamos")
public class ReclamoController {

    private final ReclamoService reclamoService;

    public ReclamoController(ReclamoService reclamoService) {
        this.reclamoService = reclamoService;
    }

    // LISTAR TODO
    @GetMapping("/listar")
    public List<Reclamo> listar() {
        return reclamoService.findAll();
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Reclamo> obtener(@PathVariable Long id) {
        return reclamoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREAR
    @PostMapping("/registrar")
    public Reclamo crear(@RequestBody Reclamo reclamo) {
        return reclamoService.save(reclamo);
    }

    // ACTUALIZAR
    @PutMapping("/id/{id}")
    public ResponseEntity<Reclamo> actualizar(
            @PathVariable Long id,
            @RequestBody Reclamo datos) {

        return reclamoService.findById(id).map(r -> {
            datos.setId(id);
            return ResponseEntity.ok(reclamoService.save(datos));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reclamoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // CAMBIAR ESTADO
    @PutMapping("/{id}/estado")
    public Reclamo cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            @RequestParam Long usuarioId
    ) {
        EstadoReclamo estado = EstadoReclamo.valueOf(nuevoEstado);
        return reclamoService.cambiarEstado(id, estado, usuarioId);
    }

    // HISTORIAL
    @GetMapping("/{id}/historial")
    public List<HistorialEstado> historial(@PathVariable Long id) {
        return reclamoService.getHistorialByReclamo(id);
    }
}