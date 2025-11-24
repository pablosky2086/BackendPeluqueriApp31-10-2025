package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grupos")
public class Grupo extends Usuario{
    private String clase;
    @Enumerated(EnumType.STRING)
    private Turno turno;

    public Grupo(Usuario usuario) {
        this.setNombreCompleto(usuario.getNombreCompleto());
        this.setEmail(usuario.getEmail());
        this.setContrasena(usuario.getContrasena());
    }
}
