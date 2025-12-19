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

    public List<Reclamo> findAll() {
        List<Reclamo> reclamos = reclamoRepository.findAll();
        reclamos.forEach(r -> {
            r.getCiudadano().getNombres();
            r.getTipoReclamo().getNombre();
            r.getPrioridad().getNombre();
        });
        return reclamos;
    }

    public Optional<Reclamo> findById(Long id) {
        Optional<Reclamo> optional = reclamoRepository.findById(id);
        optional.ifPresent(r -> {
            r.getCiudadano().getId();
            r.getTipoReclamo().getId();
            r.getPrioridad().getId();
        });
        return optional;
    }

    @Transactional
    public Reclamo save(Reclamo reclamo) {

        if (reclamo.getId() == null) {
            reclamo.setFechaRegistro(LocalDateTime.now());
            reclamo.setEstado(EstadoReclamo.REGISTRADO);
        }

        // Guardar reclamo
        Reclamo saved = reclamoRepository.save(reclamo);

        // Recargar relaciones completas
        if (saved.getCiudadano() != null) {
            saved.setCiudadano(entityManager
                    .find(saved.getCiudadano().getClass(), saved.getCiudadano().getId()));
        }
        if (saved.getTipoReclamo() != null) {
            saved.setTipoReclamo(entityManager
                    .find(saved.getTipoReclamo().getClass(), saved.getTipoReclamo().getId()));
        }
        if (saved.getPrioridad() != null) {
            saved.setPrioridad(entityManager
                    .find(saved.getPrioridad().getClass(), saved.getPrioridad().getId()));
        }

        return saved;
    }

    public List<Reclamo> findByCiudadanoId(Long ciudadanoId) {
        return reclamoRepository.findByCiudadanoId(ciudadanoId);
    }

    // =============================
    // Crear reclamo con historial
    // =============================
    @Transactional
    public Reclamo create(Reclamo r) {

        r.setFechaRegistro(LocalDateTime.now());

        if (r.getEstado() == null) {
            r.setEstado(EstadoReclamo.REGISTRADO);
        }

        Reclamo saved = reclamoRepository.save(r);

        // crear historial inicial
        HistorialEstado h = HistorialEstado.builder()
                .reclamo(saved)
                .estadoAnterior(null)
                .estadoNuevo(saved.getEstado().name())
                .fechaCambio(LocalDateTime.now())
                .usuarioResponsable(null) // aún no hay usuario
                .build();

        historialService.save(h);

        return saved;
    }

    // =============================
    // Actualizar reclamo
    // =============================
    @Transactional
    public Reclamo update(Long id, Reclamo updated) {
        return reclamoRepository.findById(id).map(r -> {

            r.setAsunto(updated.getAsunto());
            r.setDescripcion(updated.getDescripcion());
            r.setTipoReclamo(updated.getTipoReclamo());
            r.setPrioridad(updated.getPrioridad());
            // NO se cambia fechaRegistro

            return reclamoRepository.save(r);

        }).orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado: " + id));
    }

    // =============================
    // Eliminar reclamo
    // =============================
    @Transactional
    public void delete(Long id) {
        // borrar historial primero
        historialService.deleteByReclamoId(id);

        // luego borrar el reclamo
        reclamoRepository.deleteById(id);
    }


    // =============================
    // Cambiar el estado del reclamo
    // =============================
    @Transactional
    public Reclamo cambiarEstado(Long id, EstadoReclamo nuevoEstado, Long usuarioId) {

        Reclamo r = reclamoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado: " + id));

        EstadoReclamo anterior = r.getEstado();
        r.setEstado(nuevoEstado);

        Reclamo saved = reclamoRepository.save(r);

        Usuario usuario = usuarioService.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        // Guardar historial
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
}