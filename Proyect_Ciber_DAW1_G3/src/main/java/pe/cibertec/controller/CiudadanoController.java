package pe.cibertec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.model.Ciudadano;
import pe.cibertec.service.CiudadanoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ciudadanos")
public class CiudadanoController {

    @Autowired
    private CiudadanoService ciudadanoService;

    @GetMapping
    public List<Ciudadano> listar() {
        return ciudadanoService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Ciudadano> obtener(@PathVariable Long id) {
        return ciudadanoService.findById(id);
    }

    @PostMapping("/registrar")
    public Ciudadano crear(@RequestBody Ciudadano ciudadano) {
        return ciudadanoService.save(ciudadano);
    }

    @PutMapping("/{id}")
    public Ciudadano actualizar(@PathVariable Long id, @RequestBody Ciudadano ciudadano) {
        ciudadano.setId(id);
        return ciudadanoService.save(ciudadano);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        ciudadanoService.delete(id);
    }
}
