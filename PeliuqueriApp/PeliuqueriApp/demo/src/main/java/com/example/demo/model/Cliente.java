package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

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

    //Relacion con citas OneToMany y borrado en cascada
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cita> citas;

    public Cliente(Usuario usuario) {
        this.setNombreCompleto(usuario.getNombreCompleto());
        this.setEmail(usuario.getEmail());
        this.setContrasena(usuario.getContrasena());
    }

}
