package com.example.demo.controller;


import com.example.demo.model.*;
import com.example.demo.payload.request.*;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.GrupoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.UserDetailsImpl;
import com.example.demo.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ClienteRepository clienteRepository;
    private final AdminRepository adminRepository;
    private final GrupoRepository grupoRepository;

    // Propiedades de recuperación de contraseña
    private final EmailService emailService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, ClienteRepository clienteRepository, AdminRepository adminRepository, GrupoRepository grupoRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.clienteRepository = clienteRepository;
        this.adminRepository = adminRepository;
        this.grupoRepository = grupoRepository;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUsuario(@RequestBody LoginRequest loginRequest){
        try {
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
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new MessageResponse("Error de autenticación: " + e.getMessage()));
        }


    }

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registerCliente(@RequestBody RegisterClienteRequest registerClienteRequest) {
        try {
            Usuario usuario = registerUsuario(registerClienteRequest);
            Cliente cliente = new Cliente(usuario);
            cliente.setRole(Role.ROLE_CLIENTE);
            cliente.setAlergenos(registerClienteRequest.getAlergenos());
            cliente.setTelefono(registerClienteRequest.getTelefono());
            cliente.setObservaciones(registerClienteRequest.getObservaciones());
            clienteRepository.save(cliente);
            return ResponseEntity.ok(new MessageResponse("Email: " + cliente.getEmail() + " NombreCompleto: " + cliente.getNombreCompleto()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Ya existe un usuario con este email"));
        }
    }

    @PostMapping("/register/grupo")
    public ResponseEntity<?> registerGrupo(@RequestBody RegisterGrupoRequest registerGrupoRequest) {
        try {
            Usuario usuario = registerUsuario(registerGrupoRequest);
            Grupo grupo = new Grupo(usuario);
            grupo.setRole(Role.ROLE_GRUPO);
            /*
            grupo.setTurno(registerGrupoRequest.getTurno());
            */
            grupo.setClase(registerGrupoRequest.getClase());
            grupoRepository.save(grupo);
            return ResponseEntity.ok(new MessageResponse("Email: " + grupo.getEmail() + " NombreCompleto: " + grupo.getNombreCompleto()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Ya existe un usuario con este email"));
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterAdminRequest registerAdminRequest) {
        try {
            Usuario usuario = registerUsuario(registerAdminRequest);
            Admin admin = new Admin(usuario);
            admin.setRole(Role.ROLE_ADMIN);
            admin.setEspecialidad(registerAdminRequest.getEspecialidad());
            adminRepository.save(admin);
            return ResponseEntity.ok(new MessageResponse("Email: " + admin.getEmail() + " NombreCompleto: " + admin.getNombreCompleto()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Ya existe un usuario con este email"));
        }
    }


    private Usuario registerUsuario(@RequestBody RegisterRequest registerRequest) throws Exception {
        String nombreCompleto = registerRequest.getNombreCompleto();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        if (usuarioRepository.existsByEmail(email)){
            throw new Exception("Ya existe un usuario con este email");
        }

        Usuario usuario = new Usuario();

        usuario.setNombreCompleto(nombreCompleto);
        usuario.setEmail(email);
        usuario.setContrasena(passwordEncoder.encode(password));

        return usuario;
    }


    // Métodos para recuperación de contraseña
    // Endpoint 1: El usuario pide recuperar contraseña
    @PostMapping("/forgot-password")
    public String solicitarReset(@RequestParam String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no encontrado"));

        // Generamos Token para validar luego
        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        // El enlace solo vale por 30 minutos
        usuario.setTokenExpiracion(LocalDateTime.now().plusMinutes(30));

        usuarioRepository.save(usuario);

        // Enviamos CORREO 1
        emailService.enviarEnlaceConfirmacion(email, token);


        return "Correo de confirmación enviado. Revisa tu bandeja.";
    }

    // Endpoint 2: El usuario envía el token y la nueva clave
    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String token) {
// Buscamos al usuario por el token
        System.out.println("Recibiendo petición de reseteo de contraseña con token: " + token);
        Usuario usuario = usuarioRepository.findByTokenRecuperacion(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o no existe"));

        // Verificamos si caducó
        if (usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            return "<h1>Error: El enlace ha caducado. Vuelve a solicitarlo.</h1>";
        }

        // 1. Generar contraseña aleatoria (8 chars)
        String nuevaPass = UUID.randomUUID().toString().substring(0, 8);

        // 2. Guardar en BD (Recuerda encriptar si usas Spring Security)
        usuario.setContrasena(passwordEncoder.encode(nuevaPass));

        // 3. Limpiar el token para que el enlace no se pueda usar dos veces
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);
        usuarioRepository.save(usuario);

        // 4. Enviar CORREO 2
        emailService.enviarNuevaPassword(usuario.getEmail(), nuevaPass);

        // Retornamos un HTML simple para que el usuario vea en su navegador
        return """
            <div style='text-align:center; padding:50px;'>
                <h1 style='color:green;'>¡Éxito!</h1>
                <p>Hemos generado una nueva contraseña.</p>
                <p>Revisa tu correo de nuevo, te la acabamos de enviar.</p>
            </div>
            """;
    }

}
