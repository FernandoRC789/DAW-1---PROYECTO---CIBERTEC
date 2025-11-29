package pe.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ciudadanos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ciudadano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;

    private String apellidos;

    @Column(unique = true, nullable = false)
    private String dni;

    private String direccion;
    private String telefono;
    private String email;
}