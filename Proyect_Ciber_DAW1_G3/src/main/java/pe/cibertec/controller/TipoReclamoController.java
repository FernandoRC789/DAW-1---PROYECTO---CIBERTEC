package pe.cibertec.controller;

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

    @GetMapping("/listar")
    public List<TipoReclamo> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public TipoReclamo obtener(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/registrar")
    public TipoReclamo registrar(@RequestBody TipoReclamo t) {
        return service.save(t);
    }

    @PutMapping("/{id}")
    public TipoReclamo actualizar(@PathVariable Long id, @RequestBody TipoReclamo t) {
        t.setId(id);
        return service.save(t);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/buscar")
    public List<TipoReclamo> buscar(@RequestParam String nombre) {
        return service.buscarPorNombre(nombre);
    }
}
