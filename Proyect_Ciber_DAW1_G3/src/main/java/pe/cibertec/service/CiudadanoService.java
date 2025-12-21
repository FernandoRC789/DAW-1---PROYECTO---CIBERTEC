package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.Ciudadano;
import pe.cibertec.repository.CiudadanoRepository;

import java.util.List;

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

    public Ciudadano actualizarCiudadano(Long id, Ciudadano data) {
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

    public List<Ciudadano> listar() {
        return ciudadanoRepository.findAll();
    }

    public Ciudadano buscarPorId(Long id) {
        return ciudadanoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ciudadano no encontrado: " + id));
    }

    public void eliminar(Long id) {
        ciudadanoRepository.deleteById(id);
    }
}