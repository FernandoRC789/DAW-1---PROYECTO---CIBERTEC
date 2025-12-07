package pe.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.cibertec.model.TipoReclamo;

@Repository
public interface TipoReclamoRepository extends JpaRepository<TipoReclamo, Long> {
}
