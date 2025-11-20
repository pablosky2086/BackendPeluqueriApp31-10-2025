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
    private final ClienteRepository clienteRepository;
    private final AdminRepository adminRepository;
    private final GrupoRepository grupoRepository;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, ClienteRepository clienteRepository, AdminRepository adminRepository, GrupoRepository grupoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.clienteRepository = clienteRepository;
        this.adminRepository = adminRepository;
        this.grupoRepository = grupoRepository;
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
            grupo.setTurno(registerGrupoRequest.getTurno());
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



}
