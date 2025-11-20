package com.example.demo.payload.request;

import com.example.demo.model.Role;
import lombok.Data;

@Data
public class RegisterClienteRequest extends RegisterRequest{
    private String telefono;
    private String alergenos;
    private String observaciones;
}
