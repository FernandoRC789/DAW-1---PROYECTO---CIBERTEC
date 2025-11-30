package pe.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.model.Reclamo;

import java.util.List;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
    List<Reclamo> finByCiudadanoId(Long CiudadanoId);
}
