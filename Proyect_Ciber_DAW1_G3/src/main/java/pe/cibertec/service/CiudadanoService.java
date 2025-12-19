package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.Ciudadano;
import pe.cibertec.repository.CiudadanoRepository;

import java.util.List;
import java.util.Optional;

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

    public List<Ciudadano> findAll() {
        return ciudadanoRepository.findAll();
    }

    public Optional<Ciudadano> findById(Long id) {
        return ciudadanoRepository.findById(id);
    }

    public Optional<Ciudadano> findByDni(String dni) {
        return ciudadanoRepository.findByDni(dni);
    }

    public Ciudadano save(Ciudadano c) {
        return ciudadanoRepository.save(c);
    }

    public Ciudadano update(Long id, Ciudadano data) {
        return ciudadanoRepository.findById(id).map(c -> {
            c.setNombres(data.getNombres());
            c.setApellidos(data.getApellidos());
            c.setDni(data.getDni());
            c.setDireccion(data.getDireccion());
            c.setTelefono(data.getTelefono());
            c.setCorreo(data.getCorreo());
            return ciudadanoRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Ciudadano no encontrado: " + id));
    }

    public void delete(Long id) {
        ciudadanoRepository.deleteById(id);
    }

    public boolean existsByDni(String dni) {
        return ciudadanoRepository.existsByDni(dni);
    }
}
