package pe.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.model.TipoReclamo;

public interface TipoReclamoRepository extends JpaRepository<TipoReclamo, Long> {
}
