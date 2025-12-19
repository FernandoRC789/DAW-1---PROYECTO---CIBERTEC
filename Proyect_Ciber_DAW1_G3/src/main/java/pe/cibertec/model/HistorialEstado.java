package pe.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reclamo_id", nullable = false)
    private Reclamo reclamo;

    private String estadoAnterior;
    private String estadoNuevo;

    @ManyToOne
    @JoinColumn(name = "usuario_responsable")
    private Usuario usuarioResponsable;

    @Column(name = "fecha_cambio")
    private LocalDateTime fechaCambio;
}