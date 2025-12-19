package pe.cibertec.service;

import org.springframework.stereotype.Service;
import pe.cibertec.model.Usuario;
import pe.cibertec.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    //private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(Usuario u) {
        if (usuarioRepository.existsByUsername(u.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        return usuarioRepository.save(u);
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    public Usuario login(String username) {
        return usuarioRepository.findByUsername(username)
                .orElse(null);
    }

    public Usuario create(String username, String rawPassword, String rol) {
        Usuario u = Usuario.builder()
                .username(username)
                .rol(rol)
                .build();
        return usuarioRepository.save(u);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}