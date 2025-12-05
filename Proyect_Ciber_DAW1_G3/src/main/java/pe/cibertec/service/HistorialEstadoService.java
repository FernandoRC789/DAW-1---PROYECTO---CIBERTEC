package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.HistorialEstado;
import pe.cibertec.repository.HistorialEstadoRepository;

@Service
public class HistorialEstadoService {

    private final HistorialEstadoRepository historialEstadoRepository;

    public HistorialEstadoService(HistorialEstadoRepository historialEstadoRepository) {
        this.historialEstadoRepository = historialEstadoRepository;
    }

    public void registrarCambio(HistorialEstado h) {
        historialEstadoRepository.save(h);
    }

    public HistorialEstado save(HistorialEstado h) {
        return repo.save(h);
    }

    public List<HistorialEstado> findByReclamoId(Long reclamoId) {
        return repo.findByReclamoId(reclamoId);
    }
}