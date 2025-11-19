package com.example.demo.controller;


import com.example.demo.model.Role;
import com.example.demo.model.Usuario;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.RegisterRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUsuario(@RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream().findFirst().map(g -> g.getAuthority()).orElse(null);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role));

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUsuario(@RequestBody RegisterRequest registerRequest){
        String nombreCompleto = registerRequest.getNombreCompleto();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        String strRole = registerRequest.getRole();

        if (usuarioRepository.existsByEmail(email)){
            return ResponseEntity.badRequest().body(new MessageResponse("Ya existe un usuario con este email"));
        }

        Usuario usuario = new Usuario();

        usuario.setNombre_completo(nombreCompleto);
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(password));

        Role role;

        if (strRole == null || strRole == "") role = Role.ROLE_CLIENTE;
        else if (strRole.equals("admin")) role = Role.ROLE_ADMIN;
        else if (strRole.equals("grupo")) role = Role.ROLE_GRUPO;
        else role = Role.ROLE_CLIENTE;

        usuario.setRole(role);

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new MessageResponse("Email: " + email + " Password: " + password + " NombreCompleto: " + nombreCompleto)); //Quitar Password
    }

}
