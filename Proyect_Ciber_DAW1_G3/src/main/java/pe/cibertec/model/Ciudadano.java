package pe.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ciudadano")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ciudadano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(unique = true, nullable = false)
    private String dni;

    private String direccion;
    private String telefono;
    private String correo;
}