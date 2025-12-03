package pe.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prioridad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prioridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;        // BAJA / MEDIA / ALTA
    private String descripcion;
}