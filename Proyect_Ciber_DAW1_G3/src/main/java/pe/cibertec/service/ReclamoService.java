package pe.cibertec.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pe.cibertec.model.EstadoReclamo;
import pe.cibertec.model.HistorialEstado;
import pe.cibertec.model.Reclamo;
import pe.cibertec.model.Usuario;
import pe.cibertec.repository.ReclamoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final HistorialEstadoService historialService;
    private final UsuarioService usuarioService;

    @PersistenceContext
    private EntityManager entityManager;

    public ReclamoService(ReclamoRepository reclamoRepository,
                          HistorialEstadoService historialService,
                          UsuarioService usuarioService) {
        this.reclamoRepository = reclamoRepository;
        this.historialService = historialService;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public Reclamo save(Reclamo reclamo) {
        if (reclamo.getId() == null) {
            reclamo.setFechaRegistro(LocalDateTime.now());
            reclamo.setEstado(EstadoReclamo.REGISTRADO);
        }
        Reclamo saved = reclamoRepository.save(reclamo);

        // Recargar relaciones
        if (saved.getCiudadano() != null) {
            saved.setCiudadano(entityManager.find(saved.getCiudadano().getClass(), saved.getCiudadano().getId()));
        }
        if (saved.getTipoReclamo() != null) {
            saved.setTipoReclamo(entityManager.find(saved.getTipoReclamo().getClass(), saved.getTipoReclamo().getId()));
        }
        if (saved.getPrioridad() != null) {
            saved.setPrioridad(entityManager.find(saved.getPrioridad().getClass(), saved.getPrioridad().getId()));
        }
        return saved;
    }

    public Optional<Reclamo> findById(Long id) {
        return reclamoRepository.findById(id);
    }

    public List<Reclamo> findAll() {
        List<Reclamo> reclamos = reclamoRepository.findAll();
        reclamos.forEach(r -> {
            r.getCiudadano().getNombres();
            r.getTipoReclamo().getNombre();
            r.getPrioridad().getNombre();
        });
        return reclamos;
    }

    @Transactional
    public Reclamo update(Long id, Reclamo updated) {
        return reclamoRepository.findById(id).map(r -> {
            r.setAsunto(updated.getAsunto());
            r.setDescripcion(updated.getDescripcion());
            r.setTipoReclamo(updated.getTipoReclamo());
            r.setPrioridad(updated.getPrioridad());
            return reclamoRepository.save(r);
        }).orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado: " + id));
    }

    @Transactional
    public void delete(Long id) {
        // borrar historial primero
        historialService.deleteByReclamoId(id);

        // luego borrar el reclamo
        reclamoRepository.deleteById(id);
    }

    @Transactional
    public Reclamo cambiarEstado(Long id, EstadoReclamo nuevoEstado, Long usuarioId) {
        Reclamo r = reclamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado: " + id));
        EstadoReclamo anterior = r.getEstado();
        r.setEstado(nuevoEstado);
        Reclamo saved = reclamoRepository.save(r);

        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        HistorialEstado h = HistorialEstado.builder()
                .reclamo(saved)
                .estadoAnterior(anterior != null ? anterior.name() : null)
                .estadoNuevo(nuevoEstado.name())
                .usuarioResponsable(usuario)
                .fechaCambio(LocalDateTime.now())
                .build();
        historialService.save(h);
        return saved;
    }

    public List<HistorialEstado> getHistorialByReclamo(Long reclamoId) {
        return historialService.findByReclamoId(reclamoId);
    }

    @Transactional
    public boolean existeReclamo(Long ciudadanoId, Long tipoReclamoId, Long prioridadId, Long idExcluir) {
        return reclamoRepository.existsByCiudadanoTipoPrioridad(ciudadanoId, tipoReclamoId, prioridadId, idExcluir);
    }
}