package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
public class Cliente extends Usuario{
    private String telefono;
    private String observaciones;
    private String alergenos;

    public Cliente(Usuario usuario) {
        this.setNombre_completo(usuario.getNombre_completo());
        this.setEmail(usuario.getEmail());
        this.setContrasena(usuario.getContrasena());
    }

}
