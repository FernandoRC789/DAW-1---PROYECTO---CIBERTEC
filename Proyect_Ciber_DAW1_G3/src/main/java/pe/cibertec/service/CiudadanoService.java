package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.Ciudadano;
import pe.cibertec.repository.CiudadanoRepository;

@Service
public class CiudadanoService {

    private final CiudadanoRepository ciudadanoRepository;

    public CiudadanoService(CiudadanoRepository ciudadanoRepository) {
        this.ciudadanoRepository = ciudadanoRepository;
    }

    public Ciudadano registrarCiudadano(Ciudadano c) {
        if (ciudadanoRepository.existsByDni(c.getDni())) {
            throw new RuntimeException("El DNI ya se encuentra registrado");
        }
        return ciudadanoRepository.save(c);
    }

    public Ciudadano buscarPorDni(String dni) {
        return ciudadanoRepository.findByDni(dni)
                .orElse(null);
    }
}
