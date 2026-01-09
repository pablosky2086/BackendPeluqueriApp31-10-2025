package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clientes")
@ToString(callSuper = true)
public class Cliente extends Usuario{
    private String telefono;
    private String observaciones;
    private String alergenos;

    public Cliente(Usuario usuario) {
        this.setNombreCompleto(usuario.getNombreCompleto());
        this.setEmail(usuario.getEmail());
        this.setContrasena(usuario.getContrasena());
    }

}
