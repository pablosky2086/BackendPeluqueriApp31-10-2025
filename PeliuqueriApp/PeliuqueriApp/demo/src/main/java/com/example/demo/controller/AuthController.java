package com.example.demo.controller;


import com.example.demo.model.Usuario;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.RegisterRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUsuario(@RequestBody LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if (usuarioRepository.existsByEmail(email)){
            Usuario usuarioExistenete = usuarioRepository.findByEmail(email);
            if (passwordEncoder.matches(password,usuarioExistenete.getContrasena())){
                return ResponseEntity.ok(new MessageResponse("Logeado con exito. Hola " + usuarioExistenete.getNombre_completo()));
            }
            else {
                return ResponseEntity.status(401).body(new MessageResponse("Contraseña Incorrecta"));
            }
        }

        return ResponseEntity.badRequest().body(new MessageResponse("No existe un usuario con este email"));

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUsuario(@RequestBody RegisterRequest registerRequest){
        String nombreCompleto = registerRequest.getNombreCompleto();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        if (usuarioRepository.existsByEmail(email)){
            return ResponseEntity.badRequest().body(new MessageResponse("Ya existe un usuario con este email"));
        }

        Usuario usuario = new Usuario();

        usuario.setNombre_completo(nombreCompleto);
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(password));

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new MessageResponse("Email: " + email + " Password: " + password + " NombreCompleto: " + nombreCompleto));
    }

}
