package com.example.demo.payload.request;

import com.example.demo.model.Role;
import lombok.Data;

@Data
public class RegisterAdminRequest extends RegisterRequest{
    private String especialidad;
}
