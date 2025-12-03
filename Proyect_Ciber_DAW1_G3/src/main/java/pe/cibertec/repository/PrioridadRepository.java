package pe.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.cibertec.model.Prioridad;

public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {
}