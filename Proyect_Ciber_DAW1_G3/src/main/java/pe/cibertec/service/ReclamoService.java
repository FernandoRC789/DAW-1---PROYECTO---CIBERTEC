package pe.cibertec.service;

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
        return reclamoRepository.findAll();
    }

    public Optional<Reclamo> findById(Long id) {
        return reclamoRepository.findById(id);
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
}