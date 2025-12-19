package pe.cibertec.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.cibertec.dto.LoginRequest;
import pe.cibertec.model.Usuario;
import pe.cibertec.service.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Usuario usuario = usuarioService.findByUsername(request.getUsername())
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuario no existe");
        }

        // Validar la contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return ResponseEntity.status(401).body("Contraseña incorrecta");
        }

        // Crear copia del usuario sin password
        Usuario usuarioSinPassword = new Usuario();
        usuarioSinPassword.setId(usuario.getId());
        usuarioSinPassword.setUsername(usuario.getUsername());
        usuarioSinPassword.setRol(usuario.getRol());
        // No seteamos password

        return ResponseEntity.ok(usuarioSinPassword);
    }

}
