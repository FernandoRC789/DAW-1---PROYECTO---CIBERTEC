package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.EstadoReclamo;
import pe.cibertec.model.Reclamo;
import pe.cibertec.repository.ReclamoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;

    public ReclamoService(ReclamoRepository reclamoRepository) {
        this.reclamoRepository = reclamoRepository;
    }

    public Reclamo registrar(Reclamo r) {
        r.setFechaRegistro(LocalDateTime.now());
        r.setEstado(EstadoReclamo.REGISTRADO);
        return reclamoRepository.save(r);
    }

    public List<Reclamo> listarPorCiudadano(Long ciudadanoId) {
        return reclamoRepository.findByCiudadanoId(ciudadanoId);
    }

    public Reclamo buscarPorId(Long id) {
        return reclamoRepository.findById(id).orElse(null);
    }

    public Reclamo actualizar(Reclamo reclamo) {
        return reclamoRepository.save(reclamo);
    }
}