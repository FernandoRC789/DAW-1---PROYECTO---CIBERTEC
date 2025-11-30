package pe.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.model.Ciudadano;

public interface CiudadanoRepository extends JpaRepository<Ciudadano,Long> {
    boolean existsByDni(String dni);
}
