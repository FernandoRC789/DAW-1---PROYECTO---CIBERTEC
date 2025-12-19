package pe.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reclamo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asunto;

    @Column(nullable = false)
    private String descripcion;

    @Column(name="fecha_registro")
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    private EstadoReclamo estado;

    @ManyToOne
    @JoinColumn(name = "ciudadano_id", nullable = false)
    private Ciudadano ciudadano;

    @ManyToOne
    @JoinColumn(name = "tipo_reclamo_id", nullable = false)
    private TipoReclamo tipoReclamo;

    @ManyToOne
    @JoinColumn(name = "prioridad_id", nullable = false)
    private Prioridad prioridad;

    private String direccionAfectada;
    private String fotoUrl;

}
