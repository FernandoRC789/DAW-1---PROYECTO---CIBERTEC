package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.Prioridad;
import pe.cibertec.repository.PrioridadRepository;

import java.util.List;

@Service
public class PrioridadService {

    private final PrioridadRepository repo;

    public PrioridadService(PrioridadRepository repo) {
        this.repo = repo;
    }

    public List<Prioridad> findAll() {
        return repo.findAll();
    }

    public Prioridad findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public Prioridad save(Prioridad p) {
        return repo.save(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<Prioridad> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}
