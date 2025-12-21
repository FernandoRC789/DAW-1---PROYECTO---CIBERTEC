package pe.cibertec.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.cibertec.model.HistorialEstado;
import pe.cibertec.repository.HistorialEstadoRepository;

import java.util.List;

@Service
public class HistorialEstadoService {

    private final HistorialEstadoRepository historialEstadoRepository;

    public HistorialEstadoService(HistorialEstadoRepository historialEstadoRepository) {
        this.historialEstadoRepository = historialEstadoRepository;
    }

    public HistorialEstado save(HistorialEstado h) {
        return historialEstadoRepository.save(h);
    }

    public List<HistorialEstado> findByReclamoId(Long reclamoId) {
        return historialEstadoRepository.findByReclamoId(reclamoId);
    }

    @Transactional
    public void deleteByReclamoId(Long reclamoId) {
        historialEstadoRepository.findByReclamoId(reclamoId)
                .forEach(historialEstadoRepository::delete);
    }
}