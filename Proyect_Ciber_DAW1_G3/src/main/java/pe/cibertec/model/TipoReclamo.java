package pe.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_reclamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoReclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
}