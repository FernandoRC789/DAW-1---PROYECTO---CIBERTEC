package pe.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.cibertec.model.Ciudadano;
import pe.cibertec.model.Reclamo;

import java.util.List;
import java.util.Optional;

@Repository
public interface CiudadanoRepository extends JpaRepository<Ciudadano, Long> {
    boolean existsByDni(String dni);
    Optional<Ciudadano> findByDni(String dni);
    List<Reclamo> findByCiudadanoId(Long ciudadanoId);

}