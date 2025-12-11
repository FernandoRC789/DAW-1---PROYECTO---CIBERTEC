package pe.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.cibertec.model.TipoReclamo;

import java.util.List;

@Repository
public interface TipoReclamoRepository extends JpaRepository<TipoReclamo, Long> {
    List<TipoReclamo> findByNombreContainingIgnoreCase(String nombre);

}
