package pe.cibertec.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.cibertec.model.Reclamo;

import java.util.List;

@Repository
public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
        FROM Reclamo r
        WHERE r.ciudadano.id = :ciudadanoId
          AND r.tipoReclamo.id = :tipoReclamoId
          AND r.prioridad.id = :prioridadId
          AND (:idExcluir IS NULL OR r.id <> :idExcluir)
    """)
    boolean existsByCiudadanoTipoPrioridad(
            @Param("ciudadanoId") Long ciudadanoId,
            @Param("tipoReclamoId") Long tipoReclamoId,
            @Param("prioridadId") Long prioridadId,
            @Param("idExcluir") Long idExcluir);

    List<Reclamo> findByCiudadanoId(Long ciudadanoId);
}
