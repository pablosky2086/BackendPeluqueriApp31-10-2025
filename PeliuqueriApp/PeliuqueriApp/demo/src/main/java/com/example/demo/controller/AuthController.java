package com.example.demo.controller;


import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUsuario(@RequestBody LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return ResponseEntity.ok("Email: " + email + "\nPassword: " + password);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUsuario(@RequestBody RegisterRequest registerRequest){
        String nombreCompleto = registerRequest.getNombreCompleto();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        return ResponseEntity.ok("Email: " + email + "\nPassword: " + password + "\nNombreCompleto: " + nombreCompleto);
    }

}
