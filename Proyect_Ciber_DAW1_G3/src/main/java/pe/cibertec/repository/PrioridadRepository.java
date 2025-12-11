package pe.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.cibertec.model.Prioridad;

import java.util.List;

@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {
    List<Prioridad> findByNombreContainingIgnoreCase(String nombre);

}