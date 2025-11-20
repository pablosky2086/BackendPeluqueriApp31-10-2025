package com.example.demo.model;

import jakarta.persistence.Entity;
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
    private Turno turno;

    public Grupo(Usuario usuario) {
        this.setNombre_completo(usuario.getNombre_completo());
        this.setEmail(usuario.getEmail());
        this.setContrasena(usuario.getContrasena());
    }
}
