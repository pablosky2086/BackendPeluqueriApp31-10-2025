package com.example.demo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUsuario(){
        return ResponseEntity.ok("Logeado");
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUsuario(){
        return ResponseEntity.ok("Registrado");
    }

}
