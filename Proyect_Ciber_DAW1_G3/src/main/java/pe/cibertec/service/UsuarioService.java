package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.Usuario;
import pe.cibertec.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario u) {
        if (usuarioRepository.existsByUsername(u.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        return usuarioRepository.save(u);
    }

    public Usuario login(String username) {
        return usuarioRepository.findByUsername(username)
                .orElse(null);
    }
}