package pe.cibertec.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Ciudadano ciudadano;

    @ManyToOne
    @JoinColumn(name = "tipo_reclamo_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private TipoReclamo tipoReclamo;

    @ManyToOne
    @JoinColumn(name = "prioridad_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Prioridad prioridad;

    private String direccionAfectada;
    private String fotoUrl;

}
