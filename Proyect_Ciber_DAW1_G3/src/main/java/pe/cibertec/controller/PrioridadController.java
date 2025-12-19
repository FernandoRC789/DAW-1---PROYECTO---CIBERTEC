package pe.cibertec.controller;

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

    @GetMapping("/listar")
    public List<Prioridad> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Prioridad obtener(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/registrar")
    public Prioridad registrar(@RequestBody Prioridad p) {
        return service.save(p);
    }

    @PutMapping("/{id}")
    public Prioridad actualizar(@PathVariable Long id, @RequestBody Prioridad p) {
        p.setId(id);
        return service.save(p);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/buscar")
    public List<Prioridad> buscar(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
    }
}
