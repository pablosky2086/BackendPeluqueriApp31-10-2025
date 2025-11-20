package com.example.demo.payload.request;

import com.example.demo.model.Role;
import com.example.demo.model.Turno;
import lombok.Data;

@Data
public class RegisterGrupoRequest extends RegisterRequest{
    private String clase;
    private Turno turno;
}
