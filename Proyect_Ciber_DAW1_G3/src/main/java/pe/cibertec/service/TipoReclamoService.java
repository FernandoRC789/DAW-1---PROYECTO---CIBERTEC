package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.TipoReclamo;
import pe.cibertec.repository.TipoReclamoRepository;

import java.util.List;

@Service
public class TipoReclamoService {

    private final TipoReclamoRepository repo;

    public TipoReclamoService(TipoReclamoRepository repo) {
        this.repo = repo;
    }

    public List<TipoReclamo> findAll() {
        return repo.findAll();
    }

    public TipoReclamo findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public TipoReclamo save(TipoReclamo t) {
        return repo.save(t);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<TipoReclamo> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCase(nombre);
    }
}
