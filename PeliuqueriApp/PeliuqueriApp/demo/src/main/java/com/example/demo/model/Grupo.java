package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grupos")
@ToString(callSuper = true)
public class Grupo extends Usuario{
    private String clase;
    // @Enumerated(EnumType.STRING)
    // private Turno turno;
    /*private String modulo = "";
    private String aula = "";*/

    // Relacion con Agenda OneToMany
    @OneToMany(mappedBy = "grupo")
    private List<Agenda> agendas;

    // Relacion con Servicio ManyToMany
    @ManyToMany
    @JoinTable(
        name = "grupo_servicio",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    private List<Servicio> servicios;

    public Grupo(Usuario usuario) {
        this.setNombreCompleto(usuario.getNombreCompleto());
        this.setEmail(usuario.getEmail());
        this.setContrasena(usuario.getContrasena());
    }

    public void addServicio(Servicio servicio) {
        this.servicios.add(servicio);
    }

}
