package com.example.demo.payload.request;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String nombreCompleto;
    private String email;
    private String password;
    private String role;
}
